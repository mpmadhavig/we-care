package com.project.wecare.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

enum State {
    SUBMISSION_PENDING(0),
    SUBMITTED(1);

    private int value;

    public int getValue() {
        return this.value;
    }

    State (int value) {
        this.value = value;
    }
}

public class Claim implements Parcelable, Serializable {

    private String claimId;

    //claim submission status
    private int state;

    //driver details
    private String driverName,driverNic, driverAddress, driverLicencesNo, driverContactNo ;
    private Date driverLicenseExp;

    //accident details
    private ArrayList<String> ownVehicleDamagedRegions, roadStatus;
    private String roadVisibility;
    private Date date;

    //own vehicle details
    private String ownVehicleRegNumber;
    private ArrayList<Evidence> ownVehicleDamageEvidences;

    //3rd party vehicle damage details
    private Boolean isOtherVehicleDamaged;
    private String otherVehicleRegNumber;
    private String otherPartyDriverName;
    private String otherPartyDriverNumber; //Contact number
    private ArrayList<String> otherVehicleDamagedRegions;
    private Integer otherPartyAccNumber ;
    private String otherPartyBankName, otherPartyBankBranch;
    private ArrayList<Evidence> otherVehicleDamageEvidences;

    // 3rd party property damage
    private Boolean isPropertyDamage;
    private String propertyContactPersonName , propertyContactPersonAddress;
    private String propertyContactPersonNumber;
    private String propertyDamage; // A brief description
    private Integer propertyContactPersonAccNumber ;
    private String propertyContactPersonBankName, propertyContactPersonBankBranch;
    private ArrayList<Evidence> propertyDamageEvidences;


    public Claim(String claimId) {
        this.claimId = claimId;
        this.ownVehicleDamageEvidences = new ArrayList<Evidence>();
        this.otherVehicleDamageEvidences = new ArrayList<Evidence>();
        this.propertyDamageEvidences = new ArrayList<Evidence>();
        this.state = State.SUBMISSION_PENDING.getValue();
    }

    protected Claim(Parcel in) {

        claimId = in.readString();

        //claim submission status
        state = in.readInt();

        //driver details
        driverName = in.readString();
        driverNic = in.readString();
        driverLicencesNo = in.readString();
        driverLicenseExp = new Date(in.readLong());
        driverAddress = in.readString();
        driverContactNo = in.readString();

        //accident details
        ownVehicleDamagedRegions = in.createStringArrayList();
        roadStatus = in.createStringArrayList();
        roadVisibility = in.readString();

        //own vehicle details
        ownVehicleRegNumber = in.readString();
        ownVehicleDamageEvidences = in.readArrayList(Evidence.class.getClassLoader());

        //3rd party vehicle damage details
        isOtherVehicleDamaged = in.readInt() != 0 ;
        if(isOtherVehicleDamaged){
            otherVehicleRegNumber = in.readString();
            otherPartyDriverName = in.readString();
            otherPartyDriverNumber = in.readString(); //Contact number
            otherVehicleDamagedRegions = in.createStringArrayList();
            otherPartyAccNumber = in.readInt();
            otherPartyBankName = in.readString();
            otherPartyBankBranch = in.readString();
            otherVehicleDamageEvidences = in.readArrayList(Evidence.class.getClassLoader());
        }

        // 3rd party property damage
        isPropertyDamage = in.readInt() != 0 ;
        if(isPropertyDamage){
            propertyContactPersonName = in.readString();
            propertyContactPersonNumber = in.readString() ;
            propertyContactPersonAddress = in.readString();
            propertyDamage = in.readString();
            propertyContactPersonAccNumber = in.readInt();
            propertyContactPersonBankName = in.readString();
            propertyContactPersonBankBranch = in.readString();
            propertyDamageEvidences = in.readArrayList(Evidence.class.getClassLoader());
        }

    }

    public static final Creator<Claim> CREATOR = new Creator<Claim>() {
        @Override
        public Claim createFromParcel(Parcel in) {
            return new Claim(in);
        }

        @Override
        public Claim[] newArray(int size) {
            return new Claim[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(claimId);

        //claim submission status
        dest.writeInt(state);

        //driver details
        dest.writeString(driverName);
        dest.writeString(driverNic);
        dest.writeString(driverLicencesNo);
        dest.writeLong(driverLicenseExp.getTime());
        dest.writeString(driverAddress);
        dest.writeString(driverContactNo);

        //accident details
        dest.writeList(ownVehicleDamagedRegions);
        dest.writeList(roadStatus);
        dest.writeString(roadVisibility);

        //own vehicle details
        dest.writeString(ownVehicleRegNumber);
        dest.writeList(ownVehicleDamageEvidences);

        //3rd party vehicle damage details
        dest.writeInt(isOtherVehicleDamaged ? 1 : 0);
        if(isOtherVehicleDamaged){
            dest.writeString(otherVehicleRegNumber);
            dest.writeString(otherPartyDriverName);
            dest.writeString(otherPartyDriverNumber); //Contact number
            dest.writeList(otherVehicleDamagedRegions);
            dest.writeInt(otherPartyAccNumber);
            dest.writeString(otherPartyBankName);
            dest.writeString(otherPartyBankBranch);
            dest.writeList(otherVehicleDamageEvidences);
        }

        // 3rd party property damage
        dest.writeInt(isPropertyDamage ? 1 : 0);
        if(isPropertyDamage){
            dest.writeString(propertyContactPersonName);
            dest.writeString(propertyContactPersonNumber) ;
            dest.writeString(propertyContactPersonAddress);
            dest.writeString(propertyDamage);
            dest.writeInt(propertyContactPersonAccNumber);
            dest.writeString(propertyContactPersonBankName);
            dest.writeString(propertyContactPersonBankBranch);
            dest.writeList(propertyDamageEvidences);
        }

    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverNic() {
        return driverNic;
    }

    public void setDriverNic(String driverNic) {
        this.driverNic = driverNic;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getDriverLicencesNo() {
        return driverLicencesNo;
    }

    public void setDriverLicencesNo(String driverLicencesNo) {
        this.driverLicencesNo = driverLicencesNo;
    }

    public String getDriverContactNo() {
        return driverContactNo;
    }

    public void setDriverContactNo(String driverContactNo) {
        this.driverContactNo = driverContactNo;
    }

    public Date getDriverLicenseExp() {
        return driverLicenseExp;
    }

    public void setDriverLicenseExp(Date driverLicenseExp) {
        this.driverLicenseExp = driverLicenseExp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<String> getOwnVehicleDamagedRegions() {
        return ownVehicleDamagedRegions;
    }

    public void setOwnVehicleDamagedRegions(ArrayList<String> ownVehicleDamagedRegions) {
        this.ownVehicleDamagedRegions = ownVehicleDamagedRegions;
    }

    public ArrayList<String> getRoadStatus() {
        return roadStatus;
    }

    public void setRoadStatus(ArrayList<String> roadStatus) {
        this.roadStatus = roadStatus;
    }

    public String getRoadVisibility() {
        return roadVisibility;
    }

    public void setRoadVisibility(String roadVisibility) {
        this.roadVisibility = roadVisibility;
    }

    public String getOwnVehicleRegNumber() {
        return ownVehicleRegNumber;
    }

    public void setOwnVehicleRegNumber(String ownVehicleRegNumber) {
        this.ownVehicleRegNumber = ownVehicleRegNumber;
    }

    public ArrayList<Evidence> getOwnVehicleDamageEvidences() {
        return ownVehicleDamageEvidences;
    }

    public void setOwnVehicleDamageEvidences(ArrayList<Evidence> ownVehicleDamageEvidences) {
        this.ownVehicleDamageEvidences = ownVehicleDamageEvidences;
    }

    public Boolean getOtherVehicleDamaged() {
        return isOtherVehicleDamaged;
    }

    public void setOtherVehicleDamaged(Boolean otherVehicleDamaged) {
        isOtherVehicleDamaged = otherVehicleDamaged;
    }

    public String getOtherVehicleRegNumber() {
        return otherVehicleRegNumber;
    }

    public void setOtherVehicleRegNumber(String otherVehicleRegNumber) {
        this.otherVehicleRegNumber = otherVehicleRegNumber;
    }

    public String getOtherPartyDriverName() {
        return otherPartyDriverName;
    }

    public void setOtherPartyDriverName(String otherPartyDriverName) {
        this.otherPartyDriverName = otherPartyDriverName;
    }

    public String getOtherPartyDriverNumber() {
        return otherPartyDriverNumber;
    }

    public void setOtherPartyDriverNumber(String otherPartyDriverNumber) {
        this.otherPartyDriverNumber = otherPartyDriverNumber;
    }

    public ArrayList<String> getOtherVehicleDamagedRegions() {
        return otherVehicleDamagedRegions;
    }

    public void setOtherVehicleDamagedRegions(ArrayList<String> otherVehicleDamagedRegions) {
        this.otherVehicleDamagedRegions = otherVehicleDamagedRegions;
    }

    public Integer getOtherPartyAccNumber() {
        return otherPartyAccNumber;
    }

    public void setOtherPartyAccNumber(Integer otherPartyAccNumber) {
        this.otherPartyAccNumber = otherPartyAccNumber;
    }

    public String getOtherPartyBankName() {
        return otherPartyBankName;
    }

    public void setOtherPartyBankName(String otherPartyBankName) {
        this.otherPartyBankName = otherPartyBankName;
    }

    public String getOtherPartyBankBranch() {
        return otherPartyBankBranch;
    }

    public void setOtherPartyBankBranch(String otherPartyBankBranch) {
        this.otherPartyBankBranch = otherPartyBankBranch;
    }

    public ArrayList<Evidence> getOtherVehicleDamageEvidences() {
        return otherVehicleDamageEvidences;
    }

    public void setOtherVehicleDamageEvidences(ArrayList<Evidence> otherVehicleDamageEvidences) {
        this.otherVehicleDamageEvidences = otherVehicleDamageEvidences;
    }

    public boolean isPropertyDamage() {
        return this.isPropertyDamage;
    }

    public void setIsPropertyDamage(boolean isPropertyDamage) {
        this.isPropertyDamage = isPropertyDamage;
    }

    public String getPropertyDamage() {
        return this.propertyDamage;
    }

    public void setPropertyDamage(String propertyDamage) {
        this.propertyDamage = propertyDamage;
    }

    public Integer getPropertyContactPersonAccNumber() {
        return propertyContactPersonAccNumber;
    }

    public void setPropertyContactPersonAccNumber(Integer propertyContactPersonAccNumber) {
        this.propertyContactPersonAccNumber = propertyContactPersonAccNumber;
    }

    public String getPropertyContactPersonBankName() {
        return propertyContactPersonBankName;
    }

    public void setPropertyContactPersonBankName(String propertyContactPersonBankName) {
        this.propertyContactPersonBankName = propertyContactPersonBankName;
    }

    public String getPropertyContactPersonBankBranch() {
        return propertyContactPersonBankBranch;
    }

    public void setPropertyContactPersonBankBranch(String propertyContactPersonBankBranch) {
        this.propertyContactPersonBankBranch = propertyContactPersonBankBranch;
    }

    public ArrayList<Evidence> getPropertyDamageEvidences() {
        return propertyDamageEvidences;
    }

    public void setPropertyDamageEvidences(ArrayList<Evidence> propertyDamageEvidences) {
        this.propertyDamageEvidences = propertyDamageEvidences;
    }

    public void setPropertyDamage(Boolean propertyDamage) {
        isPropertyDamage = propertyDamage;
    }

    public String getPropertyContactPersonName() {
        return propertyContactPersonName;
    }

    public void setPropertyContactPersonName(String propertyContactPersonName) {
        this.propertyContactPersonName = propertyContactPersonName;
    }

    public String getPropertyContactPersonAddress() {
        return propertyContactPersonAddress;
    }

    public void setPropertyContactPersonAddress(String propertyContactPersonAddress) {
        this.propertyContactPersonAddress = propertyContactPersonAddress;
    }

    public String getPropertyContactPersonNumber() {
        return propertyContactPersonNumber;
    }

    public void setPropertyContactPersonNumber(String propertyContactPersonNumber) {
        this.propertyContactPersonNumber = propertyContactPersonNumber;
    }

    public void postClaimToBackend (Context context){

    }
}
