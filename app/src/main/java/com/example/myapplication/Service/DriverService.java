package com.example.myapplication.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.Driver;
import com.example.myapplication.Model.Order;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.location.Location;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class DriverService extends Service {
    public static final int READY_TO_RECEIVED_ORDER = 1;
    public static final int ACCEPT_ORDER = 2;
    public static final int DENY_ORDER = 3;
    public static final int COMPLETE_ORDER = 4;

    Driver driver;
    Order order;
    DatabaseReference orderRef;
    DatabaseReference driverRef;
    DatabaseReference locationRef;
    DriverServiceStatus driverServiceStatus;
    private Messenger mMessenger;
    public class MyHandler extends Handler {
        private Context applicationContext;
        public MyHandler(Context c) {
            applicationContext = c.getApplicationContext();
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case READY_TO_RECEIVED_ORDER:
                    Log.d("DriverService", "READY_TO_RECEIVED_ORDER");
                    AvailableToReceivedOrder();
                    RegisterDriver();
                    addDriverListener();
                    break;
                case ACCEPT_ORDER:
                    acceptOrder();
                    break;
                case DENY_ORDER:
                    denyOrder();
                    break;
                case COMPLETE_ORDER:
                    completeOrder();
                    AvailableToReceivedOrder();
                    break;
            }
        }
    }
    public void AvailableToReceivedOrder() {
        locationRef = FirebaseDatabase.getInstance().getReference().child("AVAILABLE_DRIVERS");
        GeoFire geoFire = new GeoFire(locationRef);
        geoFire.setLocation(driver.getDriverID(), new GeoLocation(driver.getLat(), driver.getLon()));
    }
    public void UnavailableToReceivedOrder() {
        if (locationRef != null) {
            locationRef = FirebaseDatabase.getInstance().getReference().child("AVAILABLE_DRIVERS");
            GeoFire geoFire = new GeoFire(locationRef);
            geoFire.removeLocation(driver.getDriverID());
        }
    }
    public void RegisterDriver() {
        driverRef = FirebaseDatabase.getInstance().getReference()
                .child("DRIVER_ONLINE")
                .child(driver.getDriverID());
        driverRef.setValue(null);
    }
    public void unRegisterDriver() {
        driverRef.removeValue();
    }
    void completeOrder() {
        orderRef.child("STATUS").setValue(Order.DRIVER_COMPLETED);
        orderRef.child("PRICE").setValue(driverServiceStatus.getOrderPrice());
        driverRef.child("NEW_ORDER").removeValue();
        driverRef.child("CUSTOMER").removeValue();
    }
    void acceptOrder() {
        orderRef.child("DRIVER").setValue(driver);
        orderRef.child("STATUS").setValue(Order.RECEIVED);
    }

    void denyOrder() {
        driverRef.child("NEW_ORDER").removeValue();
        driverRef.child("CUSTOMER").removeValue();
        orderRef.child("STATUS").setValue(Order.PLACED_AGAIN);
//        AvailableToReceivedOrder();
    }
    void addDriverListener() {
        driverRef.child("NEW_ORDER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    try{
                        order = snapshot.getValue(Order.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    driverServiceStatus.setOrder(order);
                    driverServiceStatus.getStatus().setValue(DriverServiceStatus.FOUND_ORDER);
                    try {
                        orderRef = FirebaseDatabase.getInstance().getReference()
                                .child("ORDER_REQUEST")
                                .child(order.getClientID());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//              Xoa khoi available driver
                    UnavailableToReceivedOrder();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        driverRef.child("CUSTOMER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Customer customer = null;
                if(snapshot.exists()) {
                    try{
                       customer = snapshot.getValue(Customer.class);
                       driverServiceStatus.setCustomer(customer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    driverServiceStatus.getStatus().setValue(DriverServiceStatus.FOUND_ORDER);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("DriverService", "onBind");
        driver = (Driver) intent.getParcelableExtra("Driver");
        mMessenger = new Messenger(new MyHandler(this));
        driverServiceStatus = DriverServiceStatus.getInstance();
        return mMessenger.getBinder();
    }
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("DriverService", "onUnbind");
        UnavailableToReceivedOrder();
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
