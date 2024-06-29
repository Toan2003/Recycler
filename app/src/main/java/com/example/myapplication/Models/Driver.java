package com.example.myapplication.Models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

public class Driver implements Parcelable {
    String driverID;
    String driverRating;
    String driverName;

    Double lat;
    Double lon;

    protected Driver(Parcel in) {
        driverID = in.readString();
        driverRating = in.readString();
        driverName = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(driverID);
        dest.writeString(driverRating);
        dest.writeString(driverName);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Driver> CREATOR = new Creator<Driver>() {
        @Override
        public Driver createFromParcel(Parcel in) {
            return new Driver(in);
        }

        @Override
        public Driver[] newArray(int size) {
            return new Driver[size];
        }
    };

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public String getDriverRating() {
        return driverRating;
    }

    public void setDriverRating(String driverRating) {
        this.driverRating = driverRating;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Driver() {

    }
    public Driver(String driverID, String driverName, String driverRating, Location driverLocation) {
        this.driverID = driverID;
        this.driverName = driverName;
        this.driverRating = driverRating;
        this.lat = driverLocation.getLatitude();
        this.lon = driverLocation.getLongitude();
    }

}