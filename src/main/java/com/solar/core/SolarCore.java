package com.solar.core;

import java.util.Map;

import com.solar.handler.Handler;
import com.solar.protocol.SolarRequest;
import com.solar.protocol.SolarResponse;

public class SolarCore {

    public void execute(SolarRequest solarRequest, SolarResponse solarResponse) {
        Map<String, SolarBean> beanMap = BeanFactory.getInstence().getBeanMap();
        if (beanMap == null || beanMap.isEmpty()) {
            return;
        }
        Handler handler = (Handler) beanMap.get(solarRequest.getHandlerName());
        handler.handler(solarRequest, solarResponse);
    }
}
