package com.inikitagricenko.healthy.repository;

import com.inikitagricenko.healthy.model.Coordinates;
import com.inikitagricenko.healthy.model.HealthData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HealthRepository {

	HealthData save(HealthData data);

	Optional<HealthData> findById(String userId, String countryCode);

	List<HealthData> findAll();

}
