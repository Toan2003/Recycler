package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Customer implements Parcelable {
    String customerName;
    String customerAddress;
    String customerEmail;
    String customerPhone;

    protected Customer(Parcel in) {
        customerName = in.readString();
        customerAddress = in.readString();
        customerEmail = in.readString();
        customerPhone = in.readString();
    }
    public Customer(){}
    public Customer(String customerName, String customerAddress, String customerPhone, String customerEmail) {
        this.customerAddress = customerAddress;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(customerName);
        dest.writeString(customerAddress);
        dest.writeString(customerEmail);
        dest.writeString(customerPhone);
    }
}
