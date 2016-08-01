package com.solar.model;

public class SolarField {

    private String    name;

    private ByteValue value;

    private IndexType indexType;

    public SolarField() {

    }

    public SolarField(String name, ByteValue value, IndexType indexType) {
        this.name = name;
        this.value = value;
        this.indexType = indexType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ByteValue getValue() {
        return value;
    }

    public void setValue(ByteValue value) {
        this.value = value;
    }

    public IndexType getIndexType() {
        return indexType;
    }

    public void setIndexType(IndexType indexType) {
        this.indexType = indexType;
    }

}
