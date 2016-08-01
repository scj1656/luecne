package com.solar.handler;

import com.solar.lucene.SolarWriter;
import com.solar.protocol.SolarRequest;
import com.solar.protocol.SolarResponse;
import com.solar.protocol.UpdateRequest;

public class UpdateHandler extends RequestHandler {

    private static final String UPDATE_HANDLER = "update";

    private SolarWriter         solarWriter;

    public String getName() {
        return UPDATE_HANDLER;
    }

    @Override
    public void handler(SolarRequest solarRequest, SolarResponse solarResponse) {
        UpdateRequest updateRequest = (UpdateRequest) solarRequest;
        //        solarWriter = new LuceneWriter(directory, updateRequest.get)

    }

}
