package com.example.myapplication.UI;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Model.Order;
import com.example.myapplication.Model.OrderPrice;
import com.example.myapplication.R;
import com.example.myapplication.Service.CustomerServiceStatus;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecycleLayout4Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecycleLayout4Fragment extends Fragment implements OnMapReadyCallback {

    MainActivity main;
    Context context;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    public static RecycleLayout4Fragment newInstance(String strArg1) {
        RecycleLayout4Fragment fragment = new RecycleLayout4Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("Recycle fragment", strArg1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity) getActivity();
        context = getActivity();
        this.context = context;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout recyclelayout = (ConstraintLayout) inflater.inflate(R.layout.recycle_layout_4, null);
        OrderPrice orderPrice = CustomerServiceStatus.getInstance().getOrderPrice();
        Order order = CustomerServiceStatus.getInstance().getOrder();
        ((TextView) recyclelayout.findViewById(R.id.earnMoney)).setText(String.valueOf(orderPrice.getTotalPrice()));
        ((TextView) recyclelayout.findViewById(R.id.orderID)).setText(String.valueOf(order.getOrderID() + " is completed"));
        ((Button) recyclelayout.findViewById(R.id.btncomfirm)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    main.sendMessageCompleteOrder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                   main.changeFragmentToIntitial();
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

        return recyclelayout;
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
                            // Logic to handle location object
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        }
                    }
                });
    }
}