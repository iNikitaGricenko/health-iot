package com.inikitagricenko.healthy.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaunaRepository<T> {

	T save(T data);

	Optional<T> findById(String id, String countryCode);

	List<T> findAll(String countryCode);
}
