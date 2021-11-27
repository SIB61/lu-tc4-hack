package com.sib.fascommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sib.fascommerce.Admin.AdminHomePage;
import com.sib.fascommerce.Authentication.Login;
import com.sib.fascommerce.Authentication.Registration;
import com.sib.fascommerce.Common.SessionManager;
import com.sib.fascommerce.Customer.User_Section1;
import com.sib.fascommerce.First.LoginActivity;
import com.sib.fascommerce.Seller.SellerHome;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    HashMap<String, String> hm1;
    String wh="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SessionManager sh = new SessionManager(MainActivity.this, SessionManager.USERSESSION);
        hm1 = sh.returnData();
        wh=hm1.get(SessionManager.EMAIL);


        textView = findViewById(R.id.testing);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wh.contains("to"))
                {
                    startActivity(new Intent(MainActivity.this, User_Section1.class));

                }
                else if(wh.contains("ll"))
                {
                    startActivity(new Intent(MainActivity.this, SellerHome.class));
                }
                else if(wh.contains("mi"))
                {
                    startActivity(new Intent(MainActivity.this, AdminHomePage.class));
                }
                else
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}