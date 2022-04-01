package com.project.wecare.models;

import java.util.ArrayList;

public class ThirdPartyPropertyDetails {
    private boolean isVehicle;
    private String description;
    private ArrayList<String> evidence;

    public ThirdPartyPropertyDetails() {
    }

    public boolean isVehicle() {
        return isVehicle;
    }

    public void setVehicle(boolean vehicle) {
        isVehicle = vehicle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getEvidence() {
        return evidence;
    }

    public void setEvidence(ArrayList<String> evidence) {
        this.evidence = evidence;
    }
}
