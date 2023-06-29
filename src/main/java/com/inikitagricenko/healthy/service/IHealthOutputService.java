package com.inikitagricenko.healthy.service;

import com.inikitagricenko.healthy.model.Coordinates;
import com.inikitagricenko.healthy.model.HealthData;

public interface IHealthOutputService {

	HealthData responseHealth(String userId, Coordinates coordinates);

}
