package com.inikitagricenko.healthy.model;

import com.inikitagricenko.healthy.annotation.FaunaRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Data
@FaunaRecord(index = "healthy")
@AllArgsConstructor
@RequiredArgsConstructor
public class HealthData {

	@NotNull
	@Size(min = 15)
	private final String userId;

	@NotNull
	private final float temperature;
	@NotNull
	private final float pulseRate;

	@NotNull
	private final int bpSystolic;
	@NotNull
	private final int bpDiastolic;

	@NotNull
	private final Coordinates coordinates;

	private ZonedDateTime timestamp;

}
