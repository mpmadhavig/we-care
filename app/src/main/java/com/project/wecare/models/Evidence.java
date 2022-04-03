package com.project.wecare.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;

public class Evidence implements Parcelable, Serializable {
    private String evidenceID;
    private Date date;
    private double latitude;
    private double longitude;
    private String imagePath;

    public static final int IMAGE_COMPRESSION_RATIO = 80;

    public Evidence(String evidenceID, Date date, double latitude, double longitude, String photoPath) {
        this.evidenceID = evidenceID;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagePath = photoPath;
    }

    protected Evidence(Parcel in) {
        evidenceID = in.readString();
        date = new Date(in.readLong());
        latitude = in.readDouble();
        longitude = in.readDouble();
        imagePath = in.readString();
    }

    public static final Creator<Evidence> CREATOR = new Creator<Evidence>() {
        @Override
        public Evidence createFromParcel(Parcel in) {
            return new Evidence(in);
        }

        @Override
        public Evidence[] newArray(int size) {
            return new Evidence[size];
        }
    };

    public String getEvidenceID() { return evidenceID; }

    public Date getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImagePath() { return imagePath; }

    public void setEvidenceID(String evidenceID) {
        this.evidenceID = evidenceID;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(evidenceID);
        dest.writeLong(date.getTime());
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(imagePath);
    }

    public byte[] getEvidenceImage() {
        Bitmap bitmap = BitmapFactory.decodeFile(this.imagePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, IMAGE_COMPRESSION_RATIO, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void postEvidenceToBackend(Context context) {

    }
}