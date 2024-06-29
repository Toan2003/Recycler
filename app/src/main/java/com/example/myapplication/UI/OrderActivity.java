package com.example.myapplication.UI;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.OrderAdapter;
import com.example.myapplication.R;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    ListView listView;
    OrderAdapter orderApdater;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.order_page);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);

        orderApdater= new OrderAdapter(getApplicationContext(), new String[]{"1", "2", "3", "4"});
        listView.setAdapter((ListAdapter) orderApdater);
    }
}
