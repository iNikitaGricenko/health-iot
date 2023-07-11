package com.inikitagricenko.healthy.repository;

import com.inikitagricenko.healthy.model.HealthData;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRepository extends FaunaRepository<HealthData> {
}
