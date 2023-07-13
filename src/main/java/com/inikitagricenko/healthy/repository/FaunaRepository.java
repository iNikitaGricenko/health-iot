package com.inikitagricenko.healthy.repository;

import com.inikitagricenko.healthy.model.HealthData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaunaRepository<T> {

	HealthData save(T data);

	Optional<HealthData> findById(String id, String countryCode);

	List<HealthData> findAll(String countryCode);
}
