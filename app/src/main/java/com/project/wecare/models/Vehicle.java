package com.project.wecare.models;

import java.util.Date;
import java.util.HashMap;

public class Vehicle {

    private String regNumber;
    private String model;
    private Integer year;
    private Integer src;
    private Date insuredDate;
    private String insuranceType;
    private HashMap<String, Claim> claims;

    public Vehicle(String regNumber, String model, Integer year) {
        this.regNumber = regNumber;
        this.model = model;
        this.year = year;
    }

    public Vehicle(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getRegNumber() {
        return regNumber;
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

    public Date getInsuredDate() {
        return insuredDate;
    }

    public void setInsuredDate(Date insuredDate) {
        this.insuredDate = insuredDate;
    }

    public String getInsuranceType() {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType) {
        this.insuranceType = insuranceType;
    }

    public HashMap<String, Claim> getClaims() {
        return claims;
    }

    public void setClaims(HashMap<String, Claim> claims) {
        this.claims = claims;
    }
}

