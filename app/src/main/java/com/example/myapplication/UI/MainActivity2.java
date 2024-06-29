package com.example.myapplication.UI;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Model.Driver;
import com.example.myapplication.R;
import com.example.myapplication.Service.DriverService;
import com.example.myapplication.Service.DriverServiceStatus;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity2 extends AppCompatActivity {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    static BottomNavigationView navigation;
    FragmentTransaction ft;
    DriverFragment driverFragment;
    NotificationFragment notificationFragment;
    ProfileFragment profileFragment;
    private Messenger mMessenger;
    public boolean isServiceConnection = false;
    DriverServiceStatus driverServiceStatus;

    private Driver driver;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMessenger = new Messenger(service);
            isServiceConnection = true;
            sendReadyToReceivedOrderMessage();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger = null;
            isServiceConnection = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Service
        driverServiceStatus = driverServiceStatus.getInstance();
        driverServiceStatus.getStatus().observe(this, status -> {
            if (status == DriverServiceStatus.FOUND_ORDER) {
                if (driverServiceStatus.getOrder() != null && driverServiceStatus.getCustomer() != null) {
                    try {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ClientOrderFragment1 clientOrderFragment1 = ClientOrderFragment1.newInstance("client_fargment");
                        ft.replace(R.id.main, clientOrderFragment1);
                        ft.addToBackStack(null);
                        ft.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // ----------------------
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.recyclenavigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        ft =getSupportFragmentManager().beginTransaction();
        driverFragment = DriverFragment.newInstance("driver_fragment");
        ft.replace(R.id.main, driverFragment);
        ft.commit();
        // test
//        try {
//            ft =getSupportFragmentManager().beginTransaction();
//            ClientOrderFragment2 clientOrderFragment2 = ClientOrderFragment2.newInstance("ClientOrderFragMent2");
//            ft.replace(R.id.main, clientOrderFragment2);
//            ft.commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


//        ViewPager2 viewPager = findViewById(R.id.viewPager);
//        viewPager.setAdapter(new PopupAdapter(this));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.mesagenavigation) {
                ft = getSupportFragmentManager().beginTransaction();
                notificationFragment = NotificationFragment.newInstance("message_fragment");
                ft.replace(R.id.main, notificationFragment);
                ft.commit();
                return true;
            } else if (itemId == R.id.recyclenavigation) {
                ft =getSupportFragmentManager().beginTransaction();
                driverFragment = DriverFragment.newInstance("driver_fragment");
                ft.replace(R.id.main, driverFragment);
                ft.commit();

                return true;
            } else if (itemId == R.id.historynavigation) {


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
    // xu ly service
    public void startDriverService(Driver driver) {
        Intent intent = new Intent(this, DriverService.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("Driver", driver);
        intent.putExtras(bundle);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }
    public void stopDriverService() {
        if (isServiceConnection){
            Intent intent = new Intent(this, DriverService.class);
            isServiceConnection = false;
            unbindService(mServiceConnection);
        }
    }

    public void sendAcceptMessage() {
        if (mMessenger != null) {
            Message message = Message.obtain(null, DriverService.ACCEPT_ORDER);
            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendDenyMessage() {
        if (mMessenger != null) {
            Message message = Message.obtain(null, DriverService.DENY_ORDER);
            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            }
    }
    public  void sendReadyToReceivedOrderMessage() {
        if (mMessenger != null) {
            isServiceConnection = true;
            Message message = Message.obtain(null, DriverService.READY_TO_RECEIVED_ORDER);
            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void senCompleteMessage() {
        if (mMessenger != null) {
            Message message = Message.obtain(null, DriverService.COMPLETE_ORDER);
            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
