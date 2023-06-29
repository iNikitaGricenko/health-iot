package com.inikitagricenko.healthy.service;

import com.faunadb.client.FaunaClient;

import java.net.MalformedURLException;

public interface IFaunaService {

	FaunaClient getFaunaClient(String region) throws MalformedURLException;

}
