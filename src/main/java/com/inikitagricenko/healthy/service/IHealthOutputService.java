package com.inikitagricenko.healthy.service;

import com.inikitagricenko.healthy.model.Coordinates;
import com.inikitagricenko.healthy.model.HealthData;

import java.util.List;

public interface IHealthOutputService {

	HealthData responseHealth(String userId, Coordinates coordinates);

	List<HealthData> responseAllHealth(Coordinates coordinates);
}
