package com.sib.fascommerce.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.sib.fascommerce.Admin.AdminHomePage;
import com.sib.fascommerce.Admin.SellerRequest;
import com.sib.fascommerce.Fragments.onGoingFragments;
import com.sib.fascommerce.MainActivity;

import com.sib.fascommerce.R;
import com.sib.fascommerce.VerifyActivity;
import com.sib.fascommerce.databinding.ActivitySellerHomeBinding;

public class SellerHome extends AppCompatActivity {
ActivitySellerHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySellerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.t);

    }

    public void goToAddNew(View view) {
        startActivity(new Intent(this,AddNewActivity.class));
    }

    public void goToProducts(View view) {
        startActivity(new Intent(this,ProductListActivity.class));
    }
    public void goToNotification(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.productsmenu:
                startActivity(new Intent(this,ProductListActivity.class));
                break;
            case R.id.notimenu:
                startActivity(new Intent(this,NotificationActivity.class));
                break;
            case R.id.logoutmenu:
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(this);

                builder = new AlertDialog.Builder(this);

                builder.setMessage("Are you sure you want to log out??")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(SellerHome.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Log Out?");
                alert.show();
                break;
            case R.id.verifymenu:
                startActivity(new Intent(SellerHome.this, VerifyActivity.class));
                break;
            case R.id.premiummenu:
                startActivity(new Intent(SellerHome.this, PremiumActivity.class));
                break;

        }


        return super.onOptionsItemSelected(item);
    }
}