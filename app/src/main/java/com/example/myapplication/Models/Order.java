package com.example.myapplication.Models;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Time;
import java.util.Date;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class Order implements Parcelable {
    public final static int PLACED = 0;
    public final static int RECEIVED= 1;
    public final static int PLACED_AGAIN = 2;
    public final static int DRIVER_COMPLETED= 3;
    public final static int CUSTOMER_ACCEPTED= 4;

    private String OrderID;
    private String ClientID;
    private String DriverID;
    private Double lat;
    private Double lon;
    private long pickupTime;
    private String Amount;


    public Order() {
    }
    public Order(String OrderID, String ClientID, String DriverID, Location location, Timestamp pickupTime, String Amount) {
        this.OrderID = OrderID;
        this.ClientID = ClientID;
        this.DriverID = DriverID;
        this.lat = location.getLatitude();
        this.lon = location.getLongitude();
        this.pickupTime = pickupTime.toDate().getTime();
        this.Amount = Amount;
    }

    protected Order(Parcel in) {
        OrderID = in.readString();
        ClientID = in.readString();
        DriverID = in.readString();
        lat = in.readDouble();
        lon = in.readDouble();
        pickupTime = in.readLong();
        Amount = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getOrderID() {
        return this.OrderID;
    }
    public void setOrderID(String OrderID) {
        this.OrderID = OrderID;
    }
    public String getClientID() {
        return this.ClientID;
    }
    public void setClientID(String ClientID) {
        this.ClientID = ClientID;
    }
    public String getDriverID() {
        return this.DriverID;
    }
    public void setDriverID(String DriverID) {
        this.DriverID = DriverID;
    }
    public String getAmount() {
        return this.Amount;
    }
    public void setAmount(String Amount) {
        this.Amount = Amount;
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

    public void setPickupTime(long pickupTime) {
        this.pickupTime = pickupTime;
    }
    public long getPickupTime() {
        return this.pickupTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(OrderID);
        dest.writeString(ClientID);
        dest.writeString(DriverID);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
        dest.writeLong(pickupTime);
        dest.writeString(Amount);
    }
}