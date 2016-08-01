package com.solar.handler;

import com.solar.core.SolarBean;
import com.solar.protocol.SolarRequest;
import com.solar.protocol.SolarResponse;

public interface Handler extends SolarBean {

    public void handler(SolarRequest solarRequest, SolarResponse solarResponse);
}
