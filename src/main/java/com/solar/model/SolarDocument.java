package com.solar.model;

import java.util.ArrayList;
import java.util.List;

public class SolarDocument {

    private static final String ID = "id";

    private List<SolarField>    fields;

    public void setField(String fieldName, Object value) {
        IndexType indexType = null;
        if (ID.equals(fieldName)) {
            indexType = new IndexType(true, true, true);
        } else {
            indexType = new IndexType(true, true, false);
        }
        fields.add(new SolarField(fieldName, new ByteValue(value), indexType));
    }

    public List<SolarField> getField() {
        if (fields == null) {
            fields = new ArrayList<SolarField>();
        }
        return fields;
    }
}
