package com.project.wecare.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class User implements Serializable  {
    private String name,nic,licenseNo, contactNo, address, occupation;
    private Date licenseExp;
    private HashMap<String, Vehicle> vehicles;

    private boolean isAuthenticated;
    private String preferredLocale;

    public User(String preferredLocale) {
        this.preferredLocale = preferredLocale;
    }

    public User(String name, String nic, String licenseNo, String contactNo, String address, String occupation, String preferredLocale) {
        this.name = name;
        this.nic = nic;
        this.licenseNo = licenseNo;
        this.contactNo = contactNo;
        this.address = address;
        this.occupation = occupation;
//        this.licenseExp = licenseExp; Todo : Add licenseExp . Look into ways
        this.preferredLocale = preferredLocale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getLicenseNo() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo = licenseNo;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Date getLicenseExp() {
        return licenseExp;
    }

    public void setLicenseExp(Date licenseExp) {
        this.licenseExp = licenseExp;
    }

    public HashMap<String, Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(HashMap<String, Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getPreferredLocale() {
        return preferredLocale;
    }

    public void setPreferredLocale(String preferredLocale) {
        this.preferredLocale = preferredLocale;
    }
}
