package com.inikitagricenko.healthy.controller;

import com.inikitagricenko.healthy.model.Coordinates;
import com.inikitagricenko.healthy.model.HealthData;
import com.inikitagricenko.healthy.service.IHealthOutputService;
import com.inikitagricenko.healthy.service.IHealthProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthyController {

	private final IHealthProcessService iHealthProcessService;
	private final IHealthOutputService iHealthOutputService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public String post(@Valid @RequestBody HealthData healthData) {
		return iHealthProcessService.process(healthData);
	}

	@GetMapping(value = "/{ref}", produces = MediaType.APPLICATION_JSON_VALUE)
	public HealthData get(@Valid @NotNull @NotBlank @PathVariable("ref") String ref, @Valid @RequestBody Coordinates coordinates) {
		return iHealthOutputService.responseHealth(ref, coordinates);
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<HealthData> getAll(@Valid @RequestBody Coordinates coordinates) {
		return iHealthOutputService.responseAllHealth(coordinates);
	}

	@GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<HealthData> getAllByUserId(@Valid @NotNull @NotBlank @PathVariable("userId") String userId, @Valid @RequestBody Coordinates coordinates) {
		return iHealthOutputService.responseAllHealth(userId, coordinates);
	}

}
