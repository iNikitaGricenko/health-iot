package com.inikitagricenko.healthy.repository;

import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Expr;
import com.faunadb.client.query.Language;
import com.faunadb.client.query.Pagination;
import com.faunadb.client.types.FaunaField;
import com.faunadb.client.types.Value;
import com.inikitagricenko.healthy.annotation.FaunaRecord;
import com.inikitagricenko.healthy.model.Coordinates;
import com.inikitagricenko.healthy.model.HealthData;
import com.inikitagricenko.healthy.service.IFaunaService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.faunadb.client.query.Language.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HeathDataFaunaGateway implements HealthRepository {

	private final IFaunaService faunaService;

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class FaunaResponse {

		@FaunaField
		Value.RefV ref;

		@FaunaField
		Long ts;

		@FaunaField
		HealthData data;

		public HealthData convert() {
			data.setRef(ref.getId());
			return data;
		}

	}

	@Override
	public HealthData save(HealthData data) {
		try {
			Coordinates coordinates = data.getCoordinates();
			FaunaClient faunaClient = faunaService.getFaunaClient(coordinates.getCountryCode());

			Expr create = Create(getCollection(), Obj("data", Obj(getInsertValues(data))));
			Value result = faunaClient.query(create).get();

			log.info("Query response received from Fauna: {}", result);

			HealthData healthData = result.at("data").to(HealthData.class).get();
			String ref = result.at("ref").to(Value.RefV.class).get().getId();
			healthData.setRef(ref);

			return healthData;
		} catch (MalformedURLException | ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Optional<HealthData> findById(String id, String countryCode) {
		try {
			FaunaClient faunaClient = faunaService.getFaunaClient(countryCode);

			List<Expr> fieldsToSelect = getFieldsToSelect();

			Expr ref = Ref(getCollection(), Value(id));

			Value result = faunaClient.query(Get(ref)).get();

			return result.at("data").to(FaunaResponse.class).getOptional().map(FaunaResponse::convert);
		} catch (MalformedURLException | ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<HealthData> findAll(String countryCode) {
		try {
			FaunaClient faunaClient = faunaService.getFaunaClient(countryCode);

			String indexName = getCollectionName() + "-all";
			Expr index = Index(indexName);
			Pagination paginate = Paginate(Match(index));

			Expr lambda = Lambda("X", Get(Var("X")));

			Value result = faunaClient.query(Map(paginate, lambda)).get();

			return result.at("data").asCollectionOf(FaunaResponse.class).get().stream().map(FaunaResponse::convert).collect(Collectors.toList());
		} catch (MalformedURLException | ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<HealthData> findAllByUser(String userId, String countryCode) {
		try {
			FaunaClient faunaClient = faunaService.getFaunaClient(countryCode);

			String indexName = getCollectionName() + "-all-by-user";
			Expr index = Index(indexName);

			Pagination paginate = Paginate(Match(index, Value(userId)));
			Expr lambda = Lambda("X", Get(Var("X")));

			Value result = faunaClient.query(Map(paginate, lambda)).get();

			return result.at("data").asCollectionOf(FaunaResponse.class).get().stream().map(FaunaResponse::convert).collect(Collectors.toList());
		} catch (MalformedURLException | ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private Expr getCollection() {
		return Collection(getCollectionName());
	}

	private String getCollectionName() {
		FaunaRecord annotation = HealthData.class.getAnnotation(FaunaRecord.class);
		String index = Optional.of(annotation.index()).filter(Predicate.not(Strings::isEmpty)).orElse(annotation.collection());
		if (index.isEmpty()) {
			index = HealthData.class.getSimpleName().toLowerCase();
		}
		return index;
	}

	private Map<String, Expr> getInsertValues(HealthData data) {
		List<Field> fieldsToInsert = Arrays.stream(data.getClass().getDeclaredFields()).collect(Collectors.toList());
		return fieldsToInsert.stream().collect(Collectors.toMap(Field::getName, field -> {
			Object value = null;
			try {
				field.setAccessible(true);
				value = field.get(data);
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
