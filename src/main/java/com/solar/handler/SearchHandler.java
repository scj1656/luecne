package com.solar.handler;

import com.solar.annotation.solarHandler;
import com.solar.protocol.SolarRequest;

@solarHandler
public class SearchHandler implements Handler {

	public void handler(SolarRequest solarRequest) {
		System.out.println("this is searchHandler");

	}
	
}
