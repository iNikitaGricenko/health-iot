package com.inikitagricenko.healthy.service.implement;

import com.faunadb.client.FaunaClient;
import com.faunadb.client.query.Expr;
import com.faunadb.client.types.Value;
import com.inikitagricenko.healthy.annotation.AOPLog;
import com.inikitagricenko.healthy.model.Coordinates;
import com.inikitagricenko.healthy.model.HealthData;
import com.inikitagricenko.healthy.service.IFaunaService;
import com.inikitagricenko.healthy.service.IGeoLocationService;
import com.inikitagricenko.healthy.service.IHealthOutputService;
import com.inikitagricenko.healthy.service.IHealthProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.faunadb.client.query.Language.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthService implements IHealthProcessService, IHealthOutputService {

	private final IGeoLocationService geoLocationService;
	private final IFaunaService faunaService;

	@AOPLog
	@Override
	public void process(HealthData healthData) {
		Coordinates coordinates = healthData.getCoordinates();
		coordinates = geoLocationService.getCoordinates(coordinates.getLatitude(), coordinates.getLongitude());

		store(healthData, coordinates);
	}

	@AOPLog
	@Override
	public HealthData responseHealth(String userId, Coordinates coordinates) {
		coordinates = geoLocationService.getCoordinates(coordinates.getLatitude(), coordinates.getLongitude());

		try {
			FaunaClient faunaClient = faunaService.getFaunaClient(coordinates.getCountryCode());

			Expr query = Get(Ref(Collection("healthdata"), userId));

			Value result = faunaClient.query(
					Let("data", query).in(
							Obj("data", Var("data"),
									"userId", Get(
											Select(
													Arr(
															Value("temperature"),
															Value("pulseRate"),
															Value("bpSystolic"),
															Value("bpDiastolic"),
															Value("latitude"),
															Value("longitude"),
															Value("country_code"),
															Value("time_zone_id")),
													Var("data")))))).get();
			return result.get(HealthData.class);
		} catch (MalformedURLException | ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@AOPLog
	private void store(HealthData healthData, Coordinates coordinates) {
		try {
			FaunaClient faunaClient = faunaService.getFaunaClient(coordinates.getCountryCode());

			Value queryResponse = faunaClient.query(
					Create(
							Collection("healthdata"), Obj("data",
									Obj(
											Map.of(
													"userId", Value(healthData.getUserId()),
													"temperature", Value(healthData.getTemperature()),
													"pulseRate", Value(healthData.getPulseRate()),
													"bpSystolic", Value(healthData.getBpSystolic()),
													"bpDiastolic", Value(healthData.getBpDiastolic()),
													"latitude", Value(coordinates.getLatitude()),
													"longitude", Value(coordinates.getLongitude()),
													"country_code", Value(coordinates.getCountryCode()),
													"time_zone_id", Value(coordinates.getTimezoneId()),
													"timestamp", Now())))
					      )).get();
			log.info("Query response received from Fauna: {}", queryResponse);
		} catch (MalformedURLException | ExecutionException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
