package com.example.myapplication.UI.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Model.Post;
import com.example.myapplication.R;
import com.example.myapplication.UI.HomeFragment;
import com.example.myapplication.UI.MainActivity;
import com.example.myapplication.UI.SocialCustomAdapter;

import java.util.ArrayList;
import java.util.List;

public class LoginFragment extends Fragment {
    private  static String ARG_PARAM1 = "param1";
    Context context;
    String mParam1 = "user";
    public static LoginFragment newInstance(String strArg1)
    {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM1, strArg1);
        fragment.setArguments(bundle);
        return fragment;
    }
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ConstraintLayout loginLayout = (ConstraintLayout) inflater.inflate(R.layout.login_fragment, container, false);
        if (mParam1.equals("driver")) {
            ImageView imageView = (ImageView) loginLayout.findViewById(R.id.imageView2);
            imageView.setImageResource(R.drawable.logindriver);
            ((TextView)loginLayout.findViewById(R.id.textView3)).setText(R.string.Login_Text_Driver);
        }
        return loginLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    public String getUsername(){
        return ((EditText) getView().findViewById(R.id.username)).getText().toString();
    }
    public String getPassword(){
        return ((EditText) getView().findViewById(R.id.password)).getText().toString();
    }

}
