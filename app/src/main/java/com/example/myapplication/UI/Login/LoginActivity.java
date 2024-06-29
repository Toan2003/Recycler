package com.example.myapplication.UI.Login;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.UI.MainActivity;
import com.example.myapplication.UI.MainActivity2;

public class LoginActivity extends AppCompatActivity {
    LoginFragment loginFragment;
    FragmentTransaction ft;
    Context context;
    int currentPage;
    boolean isDriver = false;
    Button btnlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        this.currentPage = 0;
        setContentView(R.layout.activity_login);
        btnlogin = findViewById(R.id.btnLogin);
//        Enable len khi la driver
//        isDriver = true;
        if (isDriver) {
            ((ConstraintLayout) findViewById(R.id.loginFragmentContainer)).removeAllViews();
            loginFragment = LoginFragment.newInstance("driver");
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.loginFragmentContainer, loginFragment);
            ft.commit();
            btnlogin.setText("Login");
        }
        EdgeToEdge.enable(this);
        setUpBtnLoginListener();
    }


    private void setUpBtnLoginListener() {
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDriver) {
                    String username = loginFragment.getUsername();
                    String password = loginFragment.getPassword();
//                    if (username == "toan" && password == "123456") {
//                        startActivity(new Intent(context, MainActivity2.class));
//                    }
//                    Toast.makeText(context, "Username: " + username + " Password: " + password, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, MainActivity2.class));
                } else {
                    if (currentPage == 2) {
                        // handle login
                        String username = loginFragment.getUsername();
                        String password = loginFragment.getPassword();
//                        if (username == "toan" && password == "123456") {
//                            startActivity(new Intent(context, MainActivity.class));
//                        }
//                        Toast.makeText(context, "Username: " + username + " Password: " + password, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, MainActivity.class));
                    } else if (currentPage == 1) {
                        loginFragment = LoginFragment.newInstance("user");
                        ((ConstraintLayout) findViewById(R.id.loginFragmentContainer)).removeAllViews();
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.loginFragmentContainer, loginFragment);
                        ft.commit();
                        btnLogin.setText("Login");
                        ++currentPage;
                    } else if (currentPage == 0) {
                        ImageView ivLogin = findViewById(R.id.ivLogin);
                        ivLogin.setImageResource(R.drawable.login2);
                        TextView header = findViewById(R.id.LoginHeader);
                        TextView quote = findViewById(R.id.quote);
                        header.setText(R.string.Login_Headline_1);
                        quote.setText(R.string.Login_Text_1);
                        btnLogin.setText("Next");
                        ++currentPage;
                    }
                }
            }
        });
    }
}
