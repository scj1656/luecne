package com.solar.common.constants;

public enum Relation {

                      MUST("MUST"),

                      MUST_NOT("MUST_NOT"),

                      SHOULD("SHOULD");

    private final String value;

    private Relation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
