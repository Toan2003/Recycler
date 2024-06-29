package com.example.myapplication.UI;

import static com.example.myapplication.Ultils.Ultils.getAddressFromLocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.Order;
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

import java.time.LocalTime;
import java.util.Date;

public class ClientOrderFragment1 extends Fragment implements OnMapReadyCallback {
    MainActivity2 main;
    Context context;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    Order order;
    Customer customer;
    public static ClientOrderFragment1 newInstance(String strArg1) {
        ClientOrderFragment1 fragment = new ClientOrderFragment1();
        Bundle bundle = new Bundle();
        bundle.putString("Recycle fragment", strArg1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity2) getActivity();
        context = getActivity();
        this.context = context;
        DriverServiceStatus driverServiceStatus =DriverServiceStatus.getInstance();
        order = driverServiceStatus.getOrder();
        customer = driverServiceStatus.getCustomer();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout recyclelayout = (ConstraintLayout) inflater.inflate(R.layout.client_order_layout_1, null);
        ((TextView) recyclelayout.findViewById(R.id.customerName)).setText(customer.getCustomerName());
        ((TextView) recyclelayout.findViewById(R.id.orderID)).setText(order.getOrderID());
        ((TextView) recyclelayout.findViewById(R.id.pickupTime)).setText(new Date(order.getPickupTime()).toString());
        ((TextView) recyclelayout.findViewById(R.id.amountView)).setText(order.getAmount());
        Location orderLocation = new Location("");
        orderLocation.setLatitude(order.getLat());
        orderLocation.setLongitude(order.getLon());
        ((TextView) recyclelayout.findViewById(R.id.address)).setText(getAddressFromLocation(orderLocation,main));
        ((Button) recyclelayout.findViewById(R.id.completeBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.sendAcceptMessage();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ClientOrderFragment2 clientOrderFragment2 = ClientOrderFragment2.newInstance("clientOrderFragment2");
                ft.replace(R.id.main, clientOrderFragment2);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        ((Button) recyclelayout.findViewById(R.id.denyBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.sendDenyMessage();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                DriverFragment driverFragment = DriverFragment.newInstance("driver_fragment");
                ft.replace(R.id.main, driverFragment);
                ft.addToBackStack(null);
                ft.commit();
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