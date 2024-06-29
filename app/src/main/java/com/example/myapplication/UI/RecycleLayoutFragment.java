package com.example.myapplication.UI;

import static com.example.myapplication.Ultils.Ultils.getAddressFromLocation;

import android.app.TimePickerDialog;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.Order;
import com.example.myapplication.R;
import com.example.myapplication.Service.CustomerService;
import com.example.myapplication.Service.CustomerServiceStatus;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;

import java.util.Calendar;

public class RecycleLayoutFragment extends Fragment implements OnMapReadyCallback {
    MainActivity main;
    Context context;
    Button btnrecycle;
    EditText address;
    TextView pickuptime, amount;
    ImageButton amountbtn;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    Location currentLocation;
    String Amount;
    Timestamp pickupTime;
    public static RecycleLayoutFragment newInstance(String strArg1) {
        RecycleLayoutFragment fragment = new RecycleLayoutFragment();
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
        ConstraintLayout recyclelayout = (ConstraintLayout) inflater.inflate(R.layout.recycle_layout, null);
        btnrecycle = (Button) recyclelayout.findViewById(R.id.btnrecycle);
        address = (EditText) recyclelayout.findViewById(R.id.address);
        pickuptime = (TextView) recyclelayout.findViewById(R.id.pickuptime);
        amount = (TextView) recyclelayout.findViewById(R.id.amount);
        amountbtn = (ImageButton) recyclelayout.findViewById(R.id.amountbtn);
        pickuptime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });



        btnrecycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Amount == null) {
                    Amount = " >3kg";
                }
                if (pickupTime == null) {
                    pickupTime = Timestamp.now();
                }
                Customer customer = new Customer("ClientABC", null,"toanphamphu2003@gmail.com","0829365927");
                Order order = new Order("Order1903#2", "ClientABC",null, currentLocation,  pickupTime, Amount);
                main.setCustomer(customer);
                CustomerServiceStatus.getInstance().setOrder(order);
                main.startCustomerService(order,customer);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                RecycleLayout2Fragment recycle2Fragment = RecycleLayout2Fragment.newInstance("home_fragment");

                ft.replace(R.id.main, recycle2Fragment);
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


        amountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsDialog();
            }
        });
        return recyclelayout;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }
    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//    }


    private void showTimePickerDialog() {

        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(main, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        pickuptime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }
    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle("Choose an Option");

        final CharSequence[] options = {"0-2kg", "3-5kg", "Over 5kg"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                amount.setText(options[which]);
                Amount = String.valueOf(options[which]);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
                            currentLocation = location;
                            String address1 = getAddressFromLocation(location,getContext());
                            address.setText(address1);
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        }
                    }
                });
    }

}
