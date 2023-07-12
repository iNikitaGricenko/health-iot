package com.inikitagricenko.healthy.service.implement;

import com.inikitagricenko.healthy.annotation.AOPLog;
import com.inikitagricenko.healthy.exception.DataNotFound;
import com.inikitagricenko.healthy.model.Coordinates;
import com.inikitagricenko.healthy.model.HealthData;
import com.inikitagricenko.healthy.repository.HealthRepository;
import com.inikitagricenko.healthy.service.IGeoLocationService;
import com.inikitagricenko.healthy.service.IHealthOutputService;
import com.inikitagricenko.healthy.service.IHealthProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthService implements IHealthProcessService, IHealthOutputService {

	private final IGeoLocationService geoLocationService;
	private final HealthRepository healthRepository;

	@AOPLog
	@Override
	public void process(HealthData healthData) {
		Coordinates coordinates = healthData.getCoordinates();
		coordinates = geoLocationService.getCoordinates(coordinates.getLatitude(), coordinates.getLongitude());
		healthData.setCoordinates(coordinates);
		healthData.setTimestamp(ZonedDateTime.now(ZoneId.of(coordinates.getTimezoneId())));

		healthRepository.save(healthData);
	}

	@AOPLog
	@Override
	public HealthData responseHealth(String userId, Coordinates coordinates) {
		String countryCode = geoLocationService.getCoordinates(coordinates.getLatitude(), coordinates.getLongitude()).getCountryCode();
		return healthRepository.findById(userId, countryCode).orElseThrow(DataNotFound::new);
	}
}
