package com.sib.fascommerce.Customer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sib.fascommerce.Common.SessionManager;
import com.sib.fascommerce.DataModels.ProductModel;
import com.sib.fascommerce.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowResults extends AppCompatActivity {
String re1;
TextView re;
RecyclerView rere;
ProAdapter ad;
String[] ty=new String [5];
int re3=0;
    RecyclerView gr;
    String email,email1="",url,name;
    ProAdapter agr;
List<ProductModel> list=new ArrayList<>();
List<ProductModel> list1=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_results);
        ImageView back;
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        re=(TextView) findViewById(R.id.re);
        gr=(RecyclerView) findViewById(R.id.grid);
        rere=(RecyclerView) findViewById(R.id.rere);
        re1=getIntent().getStringExtra("Re");
        SessionManager sh = new SessionManager(getApplicationContext(), SessionManager.USERSESSION);
        HashMap<String, String> hm = sh.returnData();
        email = hm.get(SessionManager.EMAIL);
        name = hm.get(SessionManager.FULLNAME);
        email1 = "";
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@')
                break;
            email1 += email.charAt(i);
        }

        rere.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        ad=new ProAdapter(ShowResults.this,list,"Sh");
        rere.setAdapter(ad);
        re.setText("Showing results for "+re1);
       FirebaseDatabase.getInstance().getReference("AllProducts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s:snapshot.getChildren())
                {
                    ProductModel de=s.getValue(ProductModel.class);
                    re1=re1.toLowerCase();

                    String n=de.getTitle().toLowerCase();
                    String n1=de.getCategory().toLowerCase();
                    String n2=de.getDes().toLowerCase();
                //    Toast.makeText(getApplicationContext(),n+" "+re1,Toast.LENGTH_LONG).show();
                    if(n.contains(re1)||n.equals(re1)||re1.contains(n)||n1.contains(re1)||n1.equals(re1)||re1.contains(n1)||n2.contains(re1)||n2.equals(re1)||re1.contains(n2)) {
                        if(re3<5)
                        {
                            ty[re3]=de.getCategory();
                            re3++;
                        }
                        list.add(de);
                    }
                }
                ad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gr.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        agr=new ProAdapter(ShowResults.this,list,"Us");
        gr.setAdapter(agr);
        for(int i=0;i<re3;i++)
        {

            FirebaseDatabase.getInstance().getReference("AllProductsCategory").child(ty[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot s: snapshot.getChildren())
                    {
                        ProductModel de=s.getValue(ProductModel.class);
                        list1.add(de);
                    }
                    agr.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}