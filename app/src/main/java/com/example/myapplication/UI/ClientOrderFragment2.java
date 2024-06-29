package com.example.myapplication.UI;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.myapplication.Adapter.TypeAmountAdapter;
import com.example.myapplication.Model.OrderPrice;
import com.example.myapplication.Model.TypeAmount;
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

import java.util.ArrayList;
import java.util.List;

public class ClientOrderFragment2 extends Fragment implements OnMapReadyCallback {
    MainActivity2 main;
    Context context;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    List<TypeAmount> list;
    public static ClientOrderFragment2 newInstance(String strArg1) {
        ClientOrderFragment2 fragment = new ClientOrderFragment2();
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout recyclelayout = (ConstraintLayout) inflater.inflate(R.layout.client_order_layout_2, null);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }
        mapFragment.getMapAsync(this);

        ((Button)recyclelayout.findViewById(R.id.btnComplete)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                try {
                    // recycle view trong dialog
                    dialog.setContentView(R.layout.layout_dialog_completeoder);
                    RecyclerView recyclerView = dialog.findViewById(R.id.recycleViewPopup);

                    //set layout manager
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    //set adapter
                    TypeAmountAdapter typeAmountAdapter = new TypeAmountAdapter(getContext());
                    list = getTypeAmountList();
                    typeAmountAdapter.setTypeAmountList(list);
                    recyclerView.setAdapter(typeAmountAdapter);

                }catch (Exception e) {
                    e.printStackTrace();
                }
                Window window = dialog.getWindow();
                if (window == null) {
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawableResource(android.R.color.transparent);

                WindowManager.LayoutParams windowAttribute = window.getAttributes();
                windowAttribute.gravity = Gravity.CENTER;
                window.setAttributes(windowAttribute);

                dialog.setCancelable(true);
                Button dialogBtn = dialog.findViewById(R.id.dialogBtn);
                dialogBtn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                        OrderPrice orderPrice = new OrderPrice(29000.0,true,true,3.5,1.0);
                         DriverServiceStatus driverServiceStatus = DriverServiceStatus.getInstance();
                         driverServiceStatus.setOrderPrice(orderPrice);
                        main.senCompleteMessage();
                        dialog.dismiss();
                     }
                });
                dialog.show();
            }
        });

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
    private List<TypeAmount> getTypeAmountList(){
        List<TypeAmount> typeAmountList = new ArrayList<>();
        typeAmountList.add(new TypeAmount("Plastic", 3.5, "Example: 3kg"));
        typeAmountList.add(new TypeAmount("Paper", 1.5, "Example: 2kg"));
        return typeAmountList;
    };

}