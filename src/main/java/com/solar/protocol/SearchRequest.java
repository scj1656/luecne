package com.solar.protocol;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends SolarRequest {

    private Map<String, String> query = new HashMap<String, String>();

    public SearchRequest() {

    }

    public Map<String, String> getQuery() {
        return query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

}
