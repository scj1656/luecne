package com.solar.protocol;

import java.util.HashMap;
import java.util.Map;

public class SearchRequest extends SolarRequest {

    private Map<String, String> query = new HashMap<String, String>();

    public SearchRequest() {

    }

    public SearchRequest(String name) {
        setName(name);
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    @Override
    protected String getName() {
        return name;
    }

    @Override
    protected void setName(String name) {
        super.name = name;
    }
}
