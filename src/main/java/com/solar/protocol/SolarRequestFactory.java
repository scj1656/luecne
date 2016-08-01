package com.solar.protocol;

import com.solar.common.constants.RequestType;

public class SolarRequestFactory {

    private static SolarRequestFactory solarRequestFactory;

    private SolarRequest               solarRequest;

    private SolarRequestFactory() {

    }

    public static SolarRequestFactory getInstance() {
        synchronized (SolarRequestFactory.class) {
            if (solarRequestFactory == null) {
                solarRequestFactory = new SolarRequestFactory();
            }
            return solarRequestFactory;
        }
    }

    public SolarRequest createDifferentRequest(String requestName) {
        if (RequestType.UPDATE.getValue().equals(requestName)) {
            solarRequest = new UpdateRequest();
        } else if (RequestType.SEARCH.getValue().equals(requestName)) {
            solarRequest = new SearchRequest();
        }
        return solarRequest;
    }
}
