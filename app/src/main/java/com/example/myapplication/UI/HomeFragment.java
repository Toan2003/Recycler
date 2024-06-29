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
import androidx.fragment.app.Fragment;

import com.example.myapplication.Model.Post;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    MainActivity main;
    Context context;
    ListView listsocial;
    SocialCustomAdapter adapter;
    public static HomeFragment newInstance(String strArg1)
    {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Home fragment", strArg1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main =(MainActivity)getActivity();
        context = getActivity();
        this.context= context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout homelayout =(LinearLayout)inflater.inflate(R.layout.home_layout, null);

        listsocial = (ListView) homelayout.findViewById(R.id.sociallistview);
        List<Post> posts= new ArrayList<>();
        posts.add(new Post("How to go green", "Living a more sustainable and environmentally-friendly life is becoming increasingly important as we face pressing environmental challenges.", R.drawable.img));
        posts.add(new Post("How to go green", "Living a more sustainable and environmentally-friendly life is becoming increasingly important as we face pressing environmental challenges.", R.drawable.img));
        posts.add(new Post("How to go green", "Living a more sustainable and environmentally-friendly life is becoming increasingly important as we face pressing environmental challenges.", R.drawable.img));

        adapter = new SocialCustomAdapter(context, R.layout.social_list_view, posts);
        listsocial.setAdapter(adapter);

        return homelayout;
    }
}
