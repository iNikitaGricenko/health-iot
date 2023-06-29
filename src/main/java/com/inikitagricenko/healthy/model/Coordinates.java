package com.inikitagricenko.healthy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Coordinates {

	@NotNull
	@JsonProperty("latitude")
	private final double latitude;

	@NotNull
	@JsonProperty("longitude")
	private final double longitude;

	@JsonProperty("country_code")
	private String countryCode;

	@JsonProperty("timezone_id")
	private String timezoneId;

}
