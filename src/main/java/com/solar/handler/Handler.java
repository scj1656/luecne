package com.solar.handler;

import com.solar.core.SolarBean;
import com.solar.protocol.SolarRequest;

public interface Handler extends SolarBean {

    void handler(SolarRequest solarRequest);
}
