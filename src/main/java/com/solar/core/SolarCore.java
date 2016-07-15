package com.solar.core;

import com.solar.protocol.SearchRequest;
import com.solar.protocol.SolarRequest;
import com.solar.protocol.SolarResponse;

public class SolarCore {

    public void execute(SolarRequest solarRequest, SolarResponse solarResponse) {
        if (solarRequest instanceof SearchRequest) {
            SearchRequest searchRequest = (SearchRequest) solarRequest;
        }
    }
}
