package com.inikitagricenko.healthy.model;

import com.inikitagricenko.healthy.annotation.FaunaRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Data
@FaunaRecord(index = "healthy")
@AllArgsConstructor
@NoArgsConstructor
public class HealthData {

	@NotNull
	@Size(min = 15)
	private String userId;

	@NotNull
	private float temperature;
	@NotNull
	private float pulseRate;

	@NotNull
	private int bpSystolic;
	@NotNull
	private int bpDiastolic;

	@NotNull
	private Coordinates coordinates;

	private ZonedDateTime timestamp;

}
