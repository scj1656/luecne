package com.solar.model;

import com.solar.annotation.Index;

public class SkuIndex {

    @Index
    private String name;

    @Index
    private String brand;

    public SkuIndex() {
    }

    public SkuIndex(String name, String brand) {
        this.name = name;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

}
