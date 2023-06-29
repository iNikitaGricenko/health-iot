package com.inikitagricenko.healthy.service.implement;

import com.faunadb.client.FaunaClient;
import com.inikitagricenko.healthy.annotation.AOPLog;
import com.inikitagricenko.healthy.service.IFaunaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaunaService implements IFaunaService {

	@Value("#{${fauna-connections}}")
	private Map<String, String> faunaConnections = new HashMap<>();

	@Value("#{${fauna-secrets}}")
	private Map<String, String> faunaSecrets = new HashMap<>();

	@AOPLog
	@Override
	public FaunaClient getFaunaClient(String region) throws MalformedURLException {

		String faunaUrl = faunaConnections.get(region);
		String faunaSecret = faunaSecrets.get(region);

		log.info("Creating Fauna Client for Region: {} with URL: {}", region, faunaUrl);

		return FaunaClient.builder()
				.withEndpoint(faunaUrl)
				.withSecret(faunaSecret)
				.build();
	}

}
