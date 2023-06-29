package com.inikitagricenko.healthy.controller;

import com.inikitagricenko.healthy.model.HealthData;
import com.inikitagricenko.healthy.service.IHealthProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthyController {

	private final IHealthProcessService iHealthProcessService;

	@PostMapping
	public void post(@Valid @RequestBody HealthData healthData) {
		iHealthProcessService.process(healthData);
	}

}
