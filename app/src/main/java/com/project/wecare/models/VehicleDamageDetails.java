package com.project.wecare.models;

import java.util.ArrayList;

public class VehicleDamageDetails {
    private boolean isVehicleDamaged;
    private boolean headLights;
    private boolean bumpers;
    private boolean hoods;
    private boolean mirrors;
    private boolean radiatorSupports;
    private boolean grilles;
    private boolean tailLights;
    private String details;
    private ArrayList<String> evidence;

    public VehicleDamageDetails() {
    }

    public boolean isVehicleDamaged() {
        return isVehicleDamaged;
    }

    public void setVehicleDamaged(boolean vehicleDamaged) {
        isVehicleDamaged = vehicleDamaged;
    }

    public boolean isHeadLights() {
        return headLights;
    }

    public void setHeadLights(boolean headLights) {
        this.headLights = headLights;
    }

    public boolean isBumpers() {
        return bumpers;
    }

    public void setBumpers(boolean bumpers) {
        this.bumpers = bumpers;
    }

    public boolean isHoods() {
        return hoods;
    }

    public void setHoods(boolean hoods) {
        this.hoods = hoods;
    }

    public boolean isMirrors() {
        return mirrors;
    }

    public void setMirrors(boolean mirrors) {
        this.mirrors = mirrors;
    }

    public boolean isRadiatorSupports() {
        return radiatorSupports;
    }

    public void setRadiatorSupports(boolean radiatorSupports) {
        this.radiatorSupports = radiatorSupports;
    }

    public boolean isGrilles() {
        return grilles;
    }

    public void setGrilles(boolean grilles) {
        this.grilles = grilles;
    }

    public boolean isTailLights() {
        return tailLights;
    }

    public void setTailLights(boolean tailLights) {
        this.tailLights = tailLights;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ArrayList<String> getEvidence() {
        return evidence;
    }

    public void setEvidence(ArrayList<String> evidence) {
        this.evidence = evidence;
    }
}
