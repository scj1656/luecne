package com.solar.handler;

import com.solar.annotation.solarHandler;
import com.solar.protocol.SolarRequest;

@solarHandler(value="updateHandler")
public class UpdateHandler implements Handler {

	public void handler(SolarRequest solarRequest) {
		System.out.println("this is updateHandler");

	}

}
