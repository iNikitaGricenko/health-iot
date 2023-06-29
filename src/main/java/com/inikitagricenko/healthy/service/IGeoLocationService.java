package com.inikitagricenko.healthy.service;

import com.inikitagricenko.healthy.model.Coordinates;

public interface IGeoLocationService {

	Coordinates getCoordinates(double latitude, double longitude);

}
