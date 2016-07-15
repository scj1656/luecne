package com.solar.handler.component;

import com.solar.protocol.SolarResponseBuilder;

public interface Component {

    void prepare(SolarResponseBuilder responseBuilder);

    void process(SolarResponseBuilder responseBuilder);

    void finish(SolarResponseBuilder responseBuilder);
}
