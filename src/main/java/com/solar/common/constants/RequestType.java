package com.solar.common.constants;

public enum RequestType {

                         UPDATE("update"), SEARCH("search");

    private final String value;

    RequestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
