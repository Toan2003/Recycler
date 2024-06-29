package com.example.myapplication.UI;

import static com.example.myapplication.Ultils.Ultils.getAddressFromLocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Driver;
import com.example.myapplication.Model.Order;
import com.example.myapplication.R;
import com.example.myapplication.Service.CustomerServiceStatus;
import com.example.myapplication.Service.DirectionsApiService;
import com.example.myapplication.Service.DirectionsResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.PolyUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecycleLayout3Fragment extends Fragment implements OnMapReadyCallback {

    MainActivity main;
    Context context;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    Driver driver;
    CustomerServiceStatus customerServiceStatus;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private String API_KEY;

    public static RecycleLayout3Fragment newInstance(String strArg1) {
        RecycleLayout3Fragment fragment = new RecycleLayout3Fragment();
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
        customerServiceStatus = CustomerServiceStatus.getInstance();
        driver = customerServiceStatus.getDriver();

        ApplicationInfo ai = null;
        try {
            ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        Bundle bundle = ai.metaData;
        API_KEY = bundle.getString("com.google.android.geo.API_KEY");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout recyclelayout = (ConstraintLayout) inflater.inflate(R.layout.recycle_layout_3, null);
        ((TextView) recyclelayout.findViewById(R.id.customerName)).setText(driver.getDriverName());
        ((TextView) recyclelayout.findViewById(R.id.driverRating)).setText(driver.getDriverRating());
        Location location = new Location("");
        location.setLatitude(driver.getLat());
        location.setLongitude(driver.getLon());
        ((TextView) recyclelayout.findViewById(R.id.addressRC3)).setText(getAddressFromLocation(location,getContext()));
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

        Driver driver = CustomerServiceStatus.getInstance().getDriver();
        Order order = CustomerServiceStatus.getInstance().getOrder();
        LatLng driverLocation = new LatLng(driver.getLat(), driver.getLon());
        LatLng customerLocation = new LatLng(order.getLat(), order.getLon());

        mMap.addMarker(new MarkerOptions()
                .position(customerLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.addMarker(new MarkerOptions()
                .position(driverLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(driverLocation, 10));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DirectionsApiService service = retrofit.create(DirectionsApiService.class);
        Call<DirectionsResponse> call = service.getDirections(
                driverLocation.latitude + "," + driverLocation.longitude,
                customerLocation.latitude + "," + customerLocation.longitude,
                API_KEY
        );

        call.enqueue(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.isSuccessful()) {
                    DirectionsResponse directionsResponse = response.body();
                    if (directionsResponse != null && !directionsResponse.routes.isEmpty()) {
                        String encodedPoints = directionsResponse.routes.get(0).overviewPolyline.points;
                        List<LatLng> decodedPath = PolyUtil.decode(encodedPoints);
//                        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
                        mMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(0xFF0000FF));
                    } else {
                        Toast.makeText(getActivity(), "Không có tuyến đường nào được tìm thấy", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Lỗi phản hồi từ API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi khi gọi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            // Enable the MyLocation butto
            getUserLocation();
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