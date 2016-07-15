package com.solar.handler;

import com.solar.annotation.SolarHandler;
import com.solar.protocol.UpdateRequest;
import com.solar.protocol.UpdateResponse;

@SolarHandler(value = "updateHandler")
public class UpdateHandler extends RequestHandler {

    public void handler(UpdateRequest updateRequest, UpdateResponse updateResponse) {

    }

    public String getName() {
        return null;
    }

}
