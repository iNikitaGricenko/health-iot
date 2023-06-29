package com.inikitagricenko.healthy.service.implement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inikitagricenko.healthy.annotation.AOPLog;
import com.inikitagricenko.healthy.model.Coordinates;
import com.inikitagricenko.healthy.service.IGeoLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class DefaultGeoLocationService implements IGeoLocationService {

	private static final String GEOCODING_RESOURCE_FORMAT = "https://api.wheretheiss.at/v1/coordinates/%s,%s";

	@AOPLog
	@Override
	public Coordinates getCoordinates(double latitude, double longitude) {
		HttpClient httpClient = HttpClient.newHttpClient();

		String requestUri = String.format(GEOCODING_RESOURCE_FORMAT, latitude, longitude);

		HttpRequest geocodingRequest = HttpRequest.newBuilder().GET()
				.uri(URI.create(requestUri))
				.timeout(Duration.ofMillis(2000)).build();

		CompletableFuture<HttpResponse<String>> geocodingResponse = httpClient.sendAsync(geocodingRequest,HttpResponse.BodyHandlers.ofString());

		return extractRegion(geocodingResponse);
	}

	@AOPLog
	private Coordinates extractRegion(CompletableFuture<HttpResponse<String>> geocodingResponse) {
		try {
			String body = geocodingResponse.get().body();

			ObjectMapper objectMapper = new ObjectMapper();

			return objectMapper.convertValue(body, Coordinates.class);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
