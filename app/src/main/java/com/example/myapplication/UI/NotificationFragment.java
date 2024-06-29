package com.example.myapplication.UI;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.myapplication.Adapter.MessageAdapter;
import com.example.myapplication.R;

public class NotificationFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button messageBtn;
    private Button notificationBtn;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1) {
        NotificationFragment fragment = new NotificationFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.notification_screen, container, false);

        ListView List = (ListView) view.findViewById(R.id.message_list);

        String[] Data = {"Adam", "Eve", "Christ", "Adam", "Eve", "Christ"};
        MessageAdapter adapter = new MessageAdapter(getContext(), Data);
        List.setAdapter(adapter);

        messageBtn = (Button) view.findViewById(R.id.messageBtn);
        notificationBtn = (Button) view.findViewById(R.id.notificationBtn);

        Drawable active = ContextCompat.getDrawable(getContext(), R.drawable.message_button_active);
        Drawable inactive = ContextCompat.getDrawable(getContext(), R.drawable.message_button);


        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageBtn.setBackground(active);
                notificationBtn.setBackground(inactive);
            }
        });

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationBtn.setBackground(active);
                messageBtn.setBackground(inactive);
            }
        });

        return view;
    }
}