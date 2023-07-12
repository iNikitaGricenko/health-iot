package com.inikitagricenko.healthy.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {

	@NotNull
	@JsonProperty("latitude")
	private double latitude;

	@NotNull
	@JsonProperty("longitude")
	private double longitude;

	@JsonProperty("country_code")
	private String countryCode;

	@JsonProperty("timezone_id")
	private String timezoneId;

	@JsonProperty("map_url")
	private String mapUrl;

}
