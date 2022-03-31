package com.project.wecare.models;

public class Vehicle {

    private String regNumber;
    private String model;
    private Integer year;
    private Integer src;

    public Vehicle(String regNumber, String model, Integer year, Integer src) {
        this.regNumber = regNumber;
        this.model = model;
        this.year = year;
        this.src = src;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSrc() {
        return src;
    }

    public void setSrc(Integer src) {
        this.src = src;
    }



}

