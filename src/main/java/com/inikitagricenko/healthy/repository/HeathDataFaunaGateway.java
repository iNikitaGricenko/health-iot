package com.inikitagricenko.healthy.repository;

import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Expr;
import com.faunadb.client.query.Language;
import com.faunadb.client.query.Pagination;
import com.faunadb.client.types.Value;
import com.inikitagricenko.healthy.annotation.FaunaRecord;
import com.inikitagricenko.healthy.model.Coordinates;
import com.inikitagricenko.healthy.model.HealthData;
import com.inikitagricenko.healthy.service.IFaunaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.faunadb.client.query.Language.*;

@Slf4j
@RequiredArgsConstructor
public class HeathDataFaunaGateway implements HealthRepository {

	private final IFaunaService faunaService;

	@Override
	public HealthData save(HealthData data) {
		try {
			Coordinates coordinates = data.getCoordinates();
			FaunaClient faunaClient = faunaService.getFaunaClient(coordinates.getCountryCode());

			Expr create = Create(getCollection(), Obj("data", Obj(getInsertValues(data))));
			Value queryResponse = faunaClient.query(create).get();

			log.info("Query response received from Fauna: {}", queryResponse);

			return queryResponse.get(HealthData.class);
		} catch (MalformedURLException | ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<HealthData> findById(String id, String countryCode) {
		try {
			FaunaClient faunaClient = faunaService.getFaunaClient(countryCode);

			List<Expr> fieldsToSelect = getFieldsToSelect();

			Expr ref = Ref(getCollection(), id);

			Expr select = Select(Arr(fieldsToSelect), Var("data"));
			Expr obj = Obj("data", Var("data"), "userId", Get(select));
			Expr let = Let("data", Get(ref)).in(obj);

			Value result = faunaClient.query(let).get();
			return Optional.ofNullable(result.get(HealthData.class));
		} catch (MalformedURLException | ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<HealthData> findAll(String countryCode) {
		try {
			FaunaClient faunaClient = faunaService.getFaunaClient(countryCode);

			Expr documents = Documents(getCollection());

			Pagination paginate = Paginate(documents);

			Expr map = Map(paginate, Lambda(Arr(Value("extra"), Value("ref")), Obj("healthdata", Get(Var("ref")))));
			Value result = faunaClient.query(map).get();

			return new ArrayList<>(result.at("data").asCollectionOf(HealthData.class).get());
		} catch (MalformedURLException | ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private Expr getCollection() {
		return Collection(getCollectionName());
	}

	private String getCollectionName() {
		FaunaRecord annotation = HealthData.class.getAnnotation(FaunaRecord.class);
		String index = Optional.of(annotation.index()).filter(Strings::isEmpty).orElse(annotation.collection());
		if (index.isEmpty()) {
			index = HealthData.class.getSimpleName().toLowerCase();
		}
		return index;
	}

	private Map<String, Expr> getInsertValues(HealthData data) {
		List<Field> fieldsToInsert = Arrays.stream(data.getClass().getDeclaredFields())
				.collect(Collectors.toList());
		return fieldsToInsert.stream().collect(Collectors.toMap(Field::getName, field -> {
			Object value = null;
			try {
				value = field.get(field.getType());
			} catch (IllegalAccessException ignore) { }
			return Value(value);
		}));
	}

	private List<Expr> getFieldsToSelect() {
		return Arrays.stream(HealthData.class.getDeclaredFields())
				.map(Field::getName)
				.map(Language::Value)
				.collect(Collectors.toList());
	}
}
