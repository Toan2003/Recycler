package com.example.myapplication.UI;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class PopupPage1Fragment extends Fragment {
    MainActivity2 main;
    Context context;
    public static PopupPage1Fragment newInstance(String strArg1)
    {
        PopupPage1Fragment fragment = new PopupPage1Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("Home fragment", strArg1);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main =(MainActivity2)getActivity();
        context = getActivity();
        this.context= context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout page = (ConstraintLayout) inflater.inflate(R.layout.popup_layout, null);
        return page;
    }
}
