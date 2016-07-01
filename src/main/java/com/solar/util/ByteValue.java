package com.solar.util;

import java.util.Arrays;

public class ByteValue {

    private byte[]    value;
    private ValueType valueType;

    public static enum ValueType {
                                  INT, LONG, DOUBLE, FLOAT, STRING
    }

    public ByteValue() {

    }

    /**
     * 多态区分基本类型
     * @param value
     */
    public ByteValue(Object value) {

        if (value instanceof Integer) {
            this.value = ByteUtil.getBytes((Integer) value);
            this.valueType = ValueType.INT;
        } else if (value instanceof Long) {
            this.value = ByteUtil.getBytes((Long) value);
            this.valueType = ValueType.LONG;
        } else if (value instanceof Float) {
            this.value = ByteUtil.getBytes((Float) value);
            this.valueType = ValueType.FLOAT;
        } else if (value instanceof Double) {
            this.value = ByteUtil.getBytes((Double) value);
            this.valueType = ValueType.DOUBLE;
        } else if (value instanceof String) {
            this.value = ByteUtil.getBytes(value.toString());
            this.valueType = ValueType.STRING;
        } else {
            this.value = ByteUtil.getBytes(value.toString());
            this.valueType = ValueType.STRING;
        }

    }

    public String getStringValue() {
        if (valueType != ValueType.STRING) {
            return null;
        }
        return ByteUtil.getString(value);
    }

    public int getIntValue() {
        if (valueType != ValueType.INT) {
            return 0;
        }
        return ByteUtil.getInt(value);
    }

    public long getLongValue() {
        if (valueType != ValueType.LONG) {
            return 0;
        }
        return ByteUtil.getLong(value);
    }

    public float getFloatValue() {
        if (valueType != ValueType.FLOAT) {
            return 0;
        }
        return ByteUtil.getFloat(value);
    }

    public double getDoubleValue() {
        if (valueType != ValueType.DOUBLE) {
            return 0;
        }
        return ByteUtil.getDouble(value);
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public ValueType getValueType() {
        return valueType;
    }

    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ByteValue) {
            ByteValue objValue = (ByteValue) obj;
            return Arrays.equals(this.value, objValue.value);
        } else {
            return super.equals(obj);
        }
    }
}
