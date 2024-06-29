package com.example.myapplication.UI;

import static com.example.myapplication.Ultils.Ultils.getAddressFromLocation;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapter.TypeAmountAdapter;
import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.Driver;
import com.example.myapplication.Model.Order;
import com.example.myapplication.Model.OrderPrice;
import com.example.myapplication.Model.TypeAmount;
import com.example.myapplication.R;
import com.example.myapplication.Service.CustomerServiceStatus;
import com.example.myapplication.Service.DirectionsApiService;
import com.example.myapplication.Service.DirectionsResponse;
import com.example.myapplication.Service.DriverServiceStatus;
import com.example.myapplication.Service.TotalLiveData;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientOrderFragment2 extends Fragment implements OnMapReadyCallback {
    MainActivity2 main;
    Context context;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    List<TypeAmount> list;
    LatLng customerLocation;
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private String API_KEY;
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
        ConstraintLayout recyclelayout = (ConstraintLayout) inflater.inflate(R.layout.client_order_layout_2, null);
        Customer customer = DriverServiceStatus.getInstance().getCustomer();
        Order order = DriverServiceStatus.getInstance().getOrder();
        customerLocation = order.getCustomerLocation();
        ((TextView) recyclelayout.findViewById(R.id.customerName)).setText(customer.getCustomerName());
        Date date = new Date(order.getPickupTime());
        ((TextView) recyclelayout.findViewById(R.id.pickupTimeCO2)).setText(String.valueOf(date));
        Location location = new Location("");
        location.setLatitude(order.getLat());
        location.setLongitude(order.getLon());
        ((TextView) recyclelayout.findViewById(R.id.addressView)).setText(getAddressFromLocation(location,main));
        ((TextView) recyclelayout.findViewById(R.id.amountView)).setText(order.getAmount());

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
                RecyclerView recyclerView;
                TypeAmountAdapter typeAmountAdapter;
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                try {
                    // recycle view trong dialog
                    dialog.setContentView(R.layout.layout_dialog_completeoder);
                    recyclerView = dialog.findViewById(R.id.recycleViewPopup);

                    //set layout manager
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    //set adapter
                    typeAmountAdapter = new TypeAmountAdapter(getContext());
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
                TotalLiveData.getInstance().getIsEdit().observe(main, status ->{
                    if(status){
                        List<TypeAmount> typeAmountList = TotalLiveData.getInstance().getList();
                        Double totalMoney = countPrice(typeAmountList);
                        ((TextView)dialog.findViewById(R.id.totalView)).setText(String.valueOf("Total: "+ totalMoney));
                    } else {
                        dialogBtn.setText("Complete");
                    }
                });
                dialogBtn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         RecyclerView recyclerView = dialog.findViewById(R.id.recycleViewPopup);
                         TypeAmountAdapter typeAmountAdapter = (TypeAmountAdapter) recyclerView.getAdapter();
                         List<TypeAmount> typeAmountList = typeAmountAdapter.getTypeAmountList();
                        OrderPrice orderPrice = countOrderPrice(typeAmountList);
                         DriverServiceStatus driverServiceStatus = DriverServiceStatus.getInstance();
                         driverServiceStatus.setOrderPrice(orderPrice);
                        main.senCompleteMessage();
                        dialog.dismiss();
                         FragmentTransaction ft = getFragmentManager().beginTransaction();
                         DriverFragment driverFragment = DriverFragment.newInstance("driver_fragment");
                         ft.replace(R.id.main, driverFragment);
                         ft.addToBackStack(null);
                         ft.commit();
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
        Driver driver = main.getDriver();
        LatLng driverLocation = new LatLng (driver.getLat(),driver.getLon());

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
            getUserLocation();
            // Enable the MyLocation button
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            // Request location permission if not already granted
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    private LatLng getUserLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        final LatLng[] currentLocation = {null};
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            currentLocation[0] = userLocation;
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                        }
                    }
                });
        return currentLocation[0];
    }
    private List<TypeAmount> getTypeAmountList(){
        List<TypeAmount> typeAmountList = new ArrayList<>();
        typeAmountList.add(new TypeAmount("Plastic", 0.0, "Example: 3kg"));
        typeAmountList.add(new TypeAmount("Paper", 0.0, "Example: 2kg"));
        return typeAmountList;
    };

    private OrderPrice countOrderPrice(List<TypeAmount> typeAmountList){
        Double totalMoney =0.0;
        int paper = 5000;
        int plastic = 12000;
        boolean isPlastic = false;
        boolean isPaper = false;
        Double weightPapper =0.0;
        Double weightPlastic =0.0;
        for (int i = 0; i < typeAmountList.size(); i++) {
            if(typeAmountList.get(i).getType().equals("Paper")){
                isPaper = true;
                weightPapper += typeAmountList.get(i).getAmount();
                totalMoney += paper * typeAmountList.get(i).getAmount();
            } else if (typeAmountList.get(i).getType().equals("Plastic")){
                isPlastic = true;
                weightPlastic += typeAmountList.get(i).getAmount();
                totalMoney += plastic * typeAmountList.get(i).getAmount();
            }
        }
        totalMoney = Math.round(totalMoney * 10.0) / 10.0;
        return new OrderPrice(totalMoney,isPaper,isPlastic,weightPapper,weightPlastic);
    }

    private Double countPrice(List<TypeAmount> typeAmountList){
        Double totalMoney =0.0;
        int paper = 5000;
        int plastic = 12000;

        for (int i = 0; i < typeAmountList.size(); i++) {
            if(typeAmountList.get(i).getType().equals("Paper")){

                totalMoney += paper * typeAmountList.get(i).getAmount();
            } else if (typeAmountList.get(i).getType().equals("Plastic")){

                totalMoney += plastic * typeAmountList.get(i).getAmount();
            }
        }
        return Math.round(totalMoney * 10.0) / 10.0;
    }

}