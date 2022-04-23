package com.sib.fascommerce.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sib.fascommerce.Common.SessionManager;
import com.sib.fascommerce.DataModels.BidModel;
import com.sib.fascommerce.DataModels.BidO;
import com.sib.fascommerce.DataModels.ProductModel;
import com.sib.fascommerce.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Show_Product extends AppCompatActivity {
    SliderView sliderView;
    Button bi;
    EditText enter;
    TextView mname,con,bidRange,model,des,name,price1;
    CircleImageView bprofile;
    ProAdapter agr;
    ImageView search;
   LinearLayout gon;
    HashMap<String, String> hm;
    String email,phone,points,url,token,name1;
    BidAdapter ba;
    RecyclerView grid,bids;
ImageView img;

    String pid,title,Des,Ran,price,pri,cat,pUrl,na;
    String ds="";
    String fg="";




    List<ProductModel> list=new ArrayList<>();
    List<BidO> list1=new ArrayList<>();
    String points3;

  ArrayList<Uri> images=new ArrayList<android.net.Uri>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
try {
    bi=(Button) findViewById(R.id.bi);
    TextInputLayout abc=(TextInputLayout)findViewById(R.id.abc);
    bi.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText editText=findViewById(R.id.bidtk);
            Toast.makeText(getApplicationContext(),"Abid",Toast.LENGTH_LONG).show();

            CardView wer=(CardView)findViewById(R.id.wer);
            wer.setVisibility(View.VISIBLE);
            TextView name1=(TextView)findViewById(R.id.name1);
            TextView points=(TextView)findViewById(R.id.points);
            TextView bid=(TextView)findViewById(R.id.bid);
            bid.setText(editText.getText().toString());
            name1.setText(fg);
            points.setText(points3);
            editText.setText("");
            editText.setVisibility(View.GONE);
            bi.setVisibility(View.GONE);
            abc.setVisibility(View.GONE);


            //     startActivity(new Intent(getApplicationContext(),Bid.class).putExtra("Pr",pri).putExtra("PID",pid));

            Toast.makeText(getApplicationContext(), "Your bid is placed suceessfully",Toast.LENGTH_LONG).show();

        }
    });
    pid=getIntent().getStringExtra("Id");
    na=getIntent().getStringExtra("Uid");
    name=(TextView) findViewById(R.id.name);
    ds="Abid Ahmed";
    con=(TextView) findViewById(R.id.con);

    model=(TextView) findViewById(R.id.model);
    bidRange=(TextView) findViewById(R.id.bidRange);
    mname=(TextView) findViewById(R.id.mname);
    img=(ImageView) findViewById(R.id.img);
    des=(TextView) findViewById(R.id.des);
    SessionManager sh = new SessionManager(getApplicationContext(), SessionManager.USERSESSION);
    HashMap<String, String> hm = sh.returnData();
    // email = hm.get(SessionManager.EMAIL);
    points3= hm.get(SessionManager.POINTS);

Toast.makeText(getApplicationContext(),pid+"",Toast.LENGTH_LONG).show();
    bprofile = (CircleImageView) findViewById(R.id.bprofile);
    grid = (RecyclerView) findViewById(R.id.grid);
    bids = (RecyclerView) findViewById(R.id.bids);
    fg=ds;
    FirebaseDatabase.getInstance().getReference("AllProducts").child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot s) {
            if(s.hasChildren()) {
               int bs=s.child("basePrice").getValue(Integer.class);
                    des.setText(s.child("des").getValue(String.class));
                    bidRange.setText("Base Price: "+bs+"");
                    pri=bs+"";
                   mname.setText(s.child("title").getValue(String.class));
                   model.setText("Category: "+s.child("category").getValue(String.class));
                   con.setText("Condition: New");
                try{
                    StorageReference storageReference= FirebaseStorage.getInstance().getReference(s.child("pUrl").getValue(String.class)+"/"+s.child("pUrl").getValue(String.class)+"0");
                    //Glide.with(holder.itemView.getContext()).load(storageReference).into(imageView);
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        //  Toast.makeText(c, list.get(i).getURL(),Toast.LENGTH_LONG).show();

                        try{
                            Glide.with(getApplicationContext()).load(uri).into(img);
                        }
                        catch (Exception e){

                        }

                    });}
                catch (Exception e){

                }
                    Toast.makeText(getApplicationContext(),"Abid34",Toast.LENGTH_LONG).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Abid",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
name.setText(na);
    grid.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

    agr = new ProAdapter(Show_Product.this, list, "Us");
    grid.setAdapter(agr);

    bids.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    ba = new BidAdapter(Show_Product.this, list1);
    bids.setAdapter(ba);
try {

    FirebaseDatabase.getInstance().getReference("AllProductsBids").child(pid).child("Bids").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot s : snapshot.getChildren()) {


                    BidO b3 = s.getValue(BidO.class);


                    list1.add(b3);


            }
            ba.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });
}
catch(Exception e)
{
    Log.d("TAG",e.getMessage());
}




}
catch(Exception e)
{
    Log.d("TAG",e.getMessage());
}







    }
}