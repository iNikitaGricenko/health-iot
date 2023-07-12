package com.inikitagricenko.healthy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.faunadb.client.types.FaunaConstructor;
import com.faunadb.client.types.FaunaField;
import com.faunadb.client.types.Value;
import com.inikitagricenko.healthy.annotation.FaunaRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

@Data
@FaunaRecord(index = "healthy")
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class HealthData {

	String ref;

	@NotNull
	@FaunaField
	@Size(min = 8)
	private String userId;

	@NotNull
	@FaunaField
	private float temperature;

	@NotNull
	@FaunaField
	private float pulseRate;

	@NotNull
	@FaunaField
	private int bpSystolic;

	@NotNull
	@FaunaField
	private int bpDiastolic;

	@NotNull
	@FaunaField
	private Coordinates coordinates;

	@FaunaField
	private Instant timestamp;

}
