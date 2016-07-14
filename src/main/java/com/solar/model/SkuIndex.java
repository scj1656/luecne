package com.solar.model;

import com.solar.annotation.Index;

public class SkuIndex {

    @Index
    private int    id;

    @Index
    private String name;

    @Index
    private String brand;

    public SkuIndex() {
    }

    public SkuIndex(int id, String name, String brand) {
        this.id = id;
        this.name = name;
        this.brand = brand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
