package com.sib.fascommerce.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sib.fascommerce.Authentication.Registration;
import com.sib.fascommerce.Common.SessionManager;
import com.sib.fascommerce.DataModels.Alt;
import com.sib.fascommerce.DataModels.BidModel;
import com.sib.fascommerce.R;

import java.util.HashMap;

public class Bid extends AppCompatActivity {
TextView base;
Button sub;
EditText vs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);
        try {
            base = (TextView) findViewById(R.id.base);
            sub = (Button) findViewById(R.id.sub);
            vs = (EditText) findViewById(R.id.vs);

            //    base.setText("The lowest you can bid on this product is "+b+"Tk.");
            sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   int b = Integer.parseInt(getIntent().getStringExtra("Pr"));

                   int ty = Integer.parseInt(vs.getText().toString());

                    Toast.makeText(getApplicationContext(),vs.getText().toString(),Toast.LENGTH_LONG).show();

                    if (ty < b) {
                        vs.setError("The bid has to be bigger or equal than the same price");
                        vs.requestFocus();
                    } else {
                        SessionManager sh = new SessionManager(Bid.this, SessionManager.USERSESSION);
                        HashMap<String, String> hm1 = sh.returnData();
                        String UID = hm1.get(SessionManager.UID);
                        String phone = hm1.get(SessionManager.PHONE);
                        String wh = hm1.get(SessionManager.WHAT);
                        String token = hm1.get(SessionManager.TOKEN);
                        String email = hm1.get(SessionManager.EMAIL);
                        String url = hm1.get(SessionManager.URL);
                        String uid = hm1.get(SessionManager.UID);
                        String name = hm1.get(SessionManager.FULLNAME);
                        FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChildren()) {
                                    int po = Integer.parseInt(snapshot.child("Points").getValue(String.class));
                                    BidModel bd = new BidModel(ty, po, phone, email, name, url, uid, getIntent().getStringExtra("PID"), "true");
                                    FirebaseDatabase.getInstance().getReference("AllProductsBids").child(getIntent().getStringExtra("PID")).child("Bids").child(uid).setValue(bd);
                                    FirebaseDatabase.getInstance().getReference("Users").child("Customers").child(uid).child("Bids").child(uid).setValue(bd);
                                    startActivity(new Intent(getApplicationContext(), User_Section1.class));

                                    Alt al=new Alt(name,email,ty+"");
                                    FirebaseDatabase.getInstance().getReference("Users").child("Sellers").child("Bids").child(getIntent().getStringExtra("PID")).child(uid).setValue(al);


                                    Toast.makeText(getApplicationContext(),"Abid234",Toast.LENGTH_LONG).show();
                                }
                                else
                                {   Toast.makeText(getApplicationContext(),"Abid",Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                }
            });
        }
        catch (Exception e)
        {
            Log.d("TAG",e.getMessage());
        }
    }
}