package com.example.myapplication.UI;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.Model.Driver;
import com.example.myapplication.R;
import com.example.myapplication.Service.DriverServiceStatus;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.GeoPoint;

public class DriverFragment extends Fragment implements OnMapReadyCallback {
    MainActivity2 main;
    Context context;
    Button startServiceBtn;
    TextView driverName;
    TextView driverRating;

    boolean isServiceRunning = false;
    Driver driver;
    ImageButton mapbtn;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    TabLayout tabLayout;
    Location driverLocation;

    public static DriverFragment newInstance(String strArg1)
    {
        DriverFragment fragment = new DriverFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Home fragment", strArg1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main =(MainActivity2)getActivity();
        context = getActivity();
        this.context= context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         ConstraintLayout driverlayout =(ConstraintLayout) inflater.inflate(R.layout.driver_layout, null);

         if (main.isServiceConnection) {
             ((Switch) driverlayout.findViewById(R.id.mySwitch)).setChecked(true);
         }
         driver = new Driver("driverABC","Nguyen Van A","5.0/5.0",null);
         main.setDriver(driver);
         driverName = driverlayout.findViewById(R.id.driverName);
         driverRating = driverlayout.findViewById(R.id.driverRating);
         driverName.setText(driver.getDriverName());
         driverRating.setText(driver.getDriverRating());
         startServiceBtn = (Button) driverlayout.findViewById(R.id.mySwitch);
         startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!main.isServiceConnection) {
                    main.isServiceConnection = true;
                    driver.setLon(driverLocation.getLongitude());
                    driver.setLat(driverLocation.getLatitude());
                    main.startDriverService(driver);
                } else {
                    main.stopDriverService();
                }
            }
         });

          mapbtn = (ImageButton) driverlayout.findViewById(R.id.mapbtn);
//          FrameLayout framecontainer = driverlayout.findViewById(R.id.frame_container);
          mapbtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

//                  if(framecontainer.getVisibility()== View.INVISIBLE)
//                  {
//                      framecontainer.setVisibility(View.VISIBLE);
//                  }
//                  else framecontainer.setVisibility(View.INVISIBLE);

//                  showPopup(context,v);
              }
          });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);

        return driverlayout;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getUserLocation();
            // Enable the MyLocation button
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            // Request location permission if not already granted
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            driverLocation = location;
                            // Logic to handle location object
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        }
                    }
                });
    }

    public void setUsername(String username) {

    }


//    public void showPopup(Context context, View v)
//    {
//        LayoutInflater inflater = (LayoutInflater) context.
//                getSystemService(LAYOUT_INFLATER_SERVICE);
//        View popupView = inflater.inflate(R.layout.popup_page, null);
//
//        // create the popup window
//        float density = context.getResources().getDisplayMetrics().density;
////        int width = 350dp;
//        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//
//        final PopupWindow popupWindow = new PopupWindow(popupView, 900, height, true);
//
//        // show the popup window
//        // which view you pass in doesn't matter, it is only used for the window tolken
//        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
//        popupView.setPadding(15,0,15,0);
//        ViewPager2 viewPager = popupView.findViewById(R.id.viewPager);
//        viewPager.setAdapter(new PopupAdapter(this));
//        tabLayout = popupView.findViewById(R.id.tabLayout);
//
//        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
////                    tab.setText("Tab");
//
//            }
//        }).attach();
//    }


}
