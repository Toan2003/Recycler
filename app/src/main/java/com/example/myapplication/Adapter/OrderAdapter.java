package com.example.myapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class OrderAdapter extends ArrayAdapter<String> {
    Context CurrentContext;
    String[] Data;

    public OrderAdapter(@NonNull Context context, String[] data) {
        super(context, R.layout.message_item, data);
        this.CurrentContext = context;
        this.Data = data;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) CurrentContext).getLayoutInflater();
        View row = inflater.inflate(R.layout.order_card, null);
        return row;
    }
}
