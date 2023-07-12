package com.inikitagricenko.healthy.repository;

import com.inikitagricenko.healthy.model.HealthData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthRepository extends FaunaRepository<HealthData> {
	List<HealthData> findAllByUser(String userId, String countryCode);
}
