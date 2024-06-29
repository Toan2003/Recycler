package com.example.myapplication.UI;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.Order;
import com.example.myapplication.R;
import com.example.myapplication.Service.CustomerService;
import com.example.myapplication.Service.CustomerServiceStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.Manifest;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    HomeFragment homeFragment;
    NotificationFragment notificationFragment;
    FragmentTransaction ft;
    static BottomNavigationView navigation;
    LinearLayout homelayout;
    ConstraintLayout recyclelayout;
    RecycleLayoutFragment recyclelayoutfragment;
    ProfileFragment profileFragment;
    OrderFragment orderFragment;
    // SERVICE
    Order order;
    private Messenger mMessenger;
    private boolean isServiceConnection = false;
    private CustomerServiceStatus customerServiceStatus;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            isServiceConnection = true;
            sendMessagePlaceAndFindDriver();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger = null;
            isServiceConnection = false;
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ft =getSupportFragmentManager().beginTransaction();
        homeFragment = HomeFragment.newInstance("home_fragment");
        ft.replace(R.id.main, homeFragment);
        ft.commit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.homenavigation);
//        navigation.setItemIconTintList(null);
//        View item = navigation.getChildAt(0).findViewById(R.id.recyclenavigation);
//
//        item.setScaleX(1.5f);
//        item.setScaleY(1.5f);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        customerServiceStatus = CustomerServiceStatus.getInstance();
        customerServiceStatus.getStatus().observe(this, status -> {
            if (status == CustomerServiceStatus.FOUND_DRIVER) {
                if (customerServiceStatus.getDriver() != null) {
                    Log.d("CustomerServiceStatus", "FOUND DRIVER");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    RecycleLayout3Fragment recycle3Fragment = RecycleLayout3Fragment.newInstance("recycle_fragment");
                    ft.replace(R.id.main, recycle3Fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            } else if (status == CustomerServiceStatus.DRIVER_COMPLETE) {
                Log.d("CustomerServiceStatus", "DRIVER COMPLETE ORDER");
                try {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    RecycleLayout4Fragment recycle4Fragment = RecycleLayout4Fragment.newInstance("recycle_fragment");
                    ft.replace(R.id.main, recycle4Fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                } catch (Exception e) {
                    Log.d("CustomerServiceStatus", "DRIVER COMPLETE ORDER: ", e);
                }

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.homenavigation) {
                ft = getSupportFragmentManager().beginTransaction();
                homeFragment = HomeFragment.newInstance("home_fragment");
                ft.replace(R.id.main, homeFragment);
                ft.commit();
                return true;
            } else if (itemId == R.id.mesagenavigation) {
                ft = getSupportFragmentManager().beginTransaction();
                notificationFragment = NotificationFragment.newInstance("message_fragment");
                ft.replace(R.id.main, notificationFragment);
                ft.commit();
                return true;
            } else if (itemId == R.id.recyclenavigation) {
                ft = getSupportFragmentManager().beginTransaction();
                recyclelayoutfragment = RecycleLayoutFragment.newInstance("recycle");
                ft.replace(R.id.main, recyclelayoutfragment);
                ft.commit();
                return true;
            } else if (itemId == R.id.historynavigation) {
                ft = getSupportFragmentManager().beginTransaction();
                orderFragment = OrderFragment.newInstance("history_fragment");
                ft.replace(R.id.main, orderFragment);
                ft.commit();

                return true;
            } else if (itemId == R.id.profilenavigation) {
                ft = getSupportFragmentManager().beginTransaction();
                profileFragment = ProfileFragment.newInstance("profile_fragment");
                ft.replace(R.id.main, profileFragment);
                ft.commit();
                return true;
            }
            return false;
        }
    };
    // SERVICE
    public void startCustomerService(Order order, Customer customer){
        if (order != null) {
            this.order = order;
            Intent intent = new Intent(this, CustomerService.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("order", order);
            bundle.putParcelable("customer", customer);
            intent.putExtras(bundle);
            bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
        }
    }

    public void stopCustomerService(){
       if (isServiceConnection) {
           order = null;
           isServiceConnection = false;
           unbindService(mServiceConnection);
       }
    }
    private void sendMessagePlaceAndFindDriver() {
        Message message = Message.obtain(null,CustomerService.PLACE_ORDER_FIND_DRIVER,0,0);
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            Log.d("CustomerService1", "sendMessagePlaceAndFindDriver: ", e);
            throw new RuntimeException(e);
        }
    }


    public void sendMessageCompleteOrder() {
        Message message = Message.obtain(null,CustomerService.COMPLETE_ORDER,0,0);
        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            Log.d("CustomerService", "sendMessageCompleteOrder: ", e);
            throw new RuntimeException(e);
        }
    }
    public void changeFragmentToIntitial() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        RecycleLayoutFragment recycleFragment = RecycleLayoutFragment.newInstance("recycle_fragment");
        ft.replace(R.id.main, recycleFragment);
        ft.addToBackStack(null);
        ft.commit();
    }


}