package com.solar.handler;

import com.solar.common.config.SolarConfig;
import com.solar.core.SolarBean;
import com.solar.protocol.SolarRequest;
import com.solar.protocol.SolarResponse;

public abstract class RequestHandler implements Handler, SolarBean {

    protected SolarConfig solarConfig = SolarConfig.DEFAULT;

    public abstract void handler(SolarRequest solarRequest, SolarResponse solarResponse);
}
