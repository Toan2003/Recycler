package com.example.myapplication.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.Driver;
import com.example.myapplication.Model.Order;
import com.example.myapplication.Model.OrderPrice;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerService extends Service {
    public static final int PLACE_ORDER_FIND_DRIVER = 1;
    public static final int COMPLETE_ORDER = 2;
    Order order;
    Customer customer;
    public int radius = 1;
    public Boolean isDriverFound = false;
    DatabaseReference orderRef;
    DatabaseReference driverRef;
    DatabaseReference locationRef;
    DatabaseReference availableDrivers;
    CustomerServiceStatus status;
    private Messenger mMessenger;
    public class MyHandler extends Handler {
        private Context applicationContext;
        public MyHandler(Context c) {
            applicationContext = c.getApplicationContext();
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case PLACE_ORDER_FIND_DRIVER:
                    Log.d("CustomerService", "PLACE_ORDER_FIND_DRIVER");
                    placeAnOrder();
                    addListenerForOrder();
                    findDriver();
                    break;
                case COMPLETE_ORDER:
                    completeOrder();
                    break;

            }
        }
    }

    public void placeAnOrder() {
        orderRef = FirebaseDatabase.getInstance().getReference()
                .child("ORDER_REQUEST")
                .child(order.getClientID());

        orderRef.child("STATUS").setValue(Order.PLACED);
        orderRef.child("DRIVER").setValue(null);

        locationRef = FirebaseDatabase.getInstance().getReference().child("CUSTOMER_LOCATION");
        GeoFire geoFire = new GeoFire(locationRef);
        geoFire.setLocation(order.getClientID(), new GeoLocation(order.getLat(), order.getLon()));
    }

    public void completeOrder() {
        if (locationRef != null) {
            locationRef = FirebaseDatabase.getInstance().getReference().child("CUSTOMER_LOCATION");
            GeoFire geoFire = new GeoFire(orderRef);
            geoFire.removeLocation(order.getClientID());
        }
        if (orderRef != null) {
            orderRef.removeValue();
        }
    }

    public void findDriver() {
        availableDrivers = FirebaseDatabase.getInstance().getReference().child("AVAILABLE_DRIVERS");
        GeoFire geoFire = new GeoFire(availableDrivers);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(order.getLat(), order.getLon()), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                if (!isDriverFound) {
                    Log.d("CustomerService radius", Integer.toString(radius));
                    isDriverFound = true;
                    order.setDriverID(dataSnapshot.getKey());
                    Log.d("CustomerService foundriver", dataSnapshot.getKey());
                    informDriver();
                    geoQuery.removeAllListeners();
                }
            }

            @Override
            public void onDataExited(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!isDriverFound) {
                    radius++;
                    findDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    void addListenerForOrder() {
        if (orderRef != null) {
            orderRef.child("STATUS").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        switch (snapshot.getValue(Integer.class)) {
                            case Order.PLACED:
                                break;
                            case Order.RECEIVED:
                                // xu ly trong driver order listener
                                break;
                            case Order.PLACED_AGAIN:
                                findDriver();
                                break;
                            case Order.DRIVER_COMPLETED:
                                // xu ly trong driver order listener
                                break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            orderRef.child("DRIVER").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        try {
                            Driver driver = snapshot.getValue(Driver.class);
                            status.setDriver(driver);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        status.getStatus().setValue(CustomerServiceStatus.FOUND_DRIVER);
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            orderRef.child("PRICE").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        try {
                           OrderPrice orderPrice = snapshot.getValue(OrderPrice.class);
                           status.setOrderPrice(orderPrice);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        status.getStatus().setValue(CustomerServiceStatus.DRIVER_COMPLETE);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    void informDriver() {
        if (order.getDriverID() != null && !order.getDriverID().isEmpty()) {
            driverRef = FirebaseDatabase.getInstance().getReference().child("DRIVER_ONLINE").child(order.getDriverID());
            try {
                driverRef.child("NEW_ORDER").setValue(order);
                driverRef.child("CUSTOMER").setValue(customer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("CustomerService", "onBind");
        order = (Order) intent.getParcelableExtra("order");
        customer = (Customer) intent.getParcelableExtra("customer");
        status = CustomerServiceStatus.getInstance();
        mMessenger = new Messenger(new MyHandler(this));
        return mMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public  int onStartCommand(Intent intent, int flags, int startId) {
        order = (Order) intent.getParcelableExtra("order");
        return START_NOT_STICKY;
    }

}
