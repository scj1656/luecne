package com.solar.handler;

import com.solar.annotation.SolarHandler;
import com.solar.protocol.SolarRequest;

@SolarHandler
public class SearchHandler extends RequestHandler {

    public void handler(SolarRequest solarRequest) {
        System.out.println("this is searchHandler");
    }

    public String getName() {
        return null;
    }

}
