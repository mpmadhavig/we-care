package com.project.wecare.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class User implements Serializable  {
    private String name,nic,licenseNo, contactNo, address, occupation, email;
    private Date licenseExp;
    private ArrayList<String> vehiclesRegNumber;

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
//        this.email = email;
        this.preferredLocale = preferredLocale;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public ArrayList<String> getVehiclesRegNumber() {
        return vehiclesRegNumber;
    }

    public void setVehiclesRegNumber(ArrayList<String> vehiclesRegNumber) {
        this.vehiclesRegNumber = vehiclesRegNumber;
    }
}
