package com.solar.handler;

import com.solar.annotation.SolarHandler;
import com.solar.protocol.SolarRequest;
import com.solar.protocol.SolarResponse;

@SolarHandler
public class SearchHandler extends RequestHandler {

    private static final String SEARCH_NANDLER = "search";

    public String getName() {
        return SEARCH_NANDLER;
    }

    @Override
    public void handler(SolarRequest solarRequest, SolarResponse solarResponse) {
    }

}
