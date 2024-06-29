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

public class NotificationAdapter extends ArrayAdapter<String> {
    Context CurrentContext;
    String[] Data;

    public NotificationAdapter(@NonNull Context context, String[] data) {
        super(context, R.layout.message_item, data);
        this.CurrentContext = context;
        this.Data = data;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity) CurrentContext).getLayoutInflater();
        View row;
        if (position < 2) {
            row = inflater.inflate(R.layout.notification_item_new, null);
        }
        else {
            row = inflater.inflate(R.layout.notification_item, null);
        }

        TextView title = (TextView) row.findViewById(R.id.notificationTitle);
        TextView preview = (TextView) row.findViewById(R.id.notificationPreview);

        title.setText("Received new message");
        preview.setText("You got a message from " + Data[position]);

        return row;
    }
}
