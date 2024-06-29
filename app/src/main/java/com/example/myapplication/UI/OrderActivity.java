package com.example.myapplication.UI;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Order;
import com.example.myapplication.R;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Order> orderList;
    OrderApdater orderApdater;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order_page);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.RecyclerView);
        orderList=new ArrayList<>();
        orderList.add(new Order());
        orderList.add(new Order());
        orderList.add(new Order());
        orderList.add(new Order());
        orderList.add(new Order());
        orderList.add(new Order());
        orderList.add(new Order());
        orderList.add(new Order());
        orderApdater=new OrderApdater(getApplicationContext(),orderList);
        recyclerView.setAdapter(orderApdater);
    }
}
