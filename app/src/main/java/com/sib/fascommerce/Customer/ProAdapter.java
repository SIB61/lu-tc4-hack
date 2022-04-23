package com.sib.fascommerce.Customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sib.fascommerce.Common.SessionManager;
import com.sib.fascommerce.DataModels.ProductModel;
import com.sib.fascommerce.R;


import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProAdapter extends RecyclerView.Adapter<ProAdapter.MED> {
    List<ProductModel> list;
    Context c;
    String wo;
    public  ProAdapter(Context c, List<ProductModel> list, String wo)
    {
        this.c=c;
        this.list=list;
        this.wo=wo;
    }

    @NonNull
    @Override
    public MED onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_med_adapter, parent, false);
        return new MED(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MED holder, @SuppressLint("RecyclerView") int i) {
        // Toast.makeText(c,list.size()+"",Toast.LENGTH_LONG).show();
        //  Toast.makeText(c, list.get(i).getURL(),Toast.LENGTH_LONG).show();
        try{
            StorageReference storageReference= FirebaseStorage.getInstance().getReference(list.get(i).getpUrl()+"/"+list.get(i).getpUrl()+"0");
            //Glide.with(holder.itemView.getContext()).load(storageReference).into(imageView);
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                //  Toast.makeText(c, list.get(i).getURL(),Toast.LENGTH_LONG).show();

                try{
                    Glide.with(c.getApplicationContext()).load(uri).into(holder.ph);
                }
                catch (Exception e){

                }

            });}
        catch (Exception e){

        }
        try{
        holder.name.setText(list.get(i).getTitle());
        holder.price.setText(list.get(i).getBasePrice()+"Tk.");
        if(wo.equals("Ow")){
            holder.ed.setVisibility(View.VISIBLE);
        }
      /*  holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.startActivity(new Intent(c, Edit_Medicine.class).putExtra("Ran",list.get(i).getRan()));
            }
        });*/
        SessionManager sh = new SessionManager(c, SessionManager.USERSESSION);

        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!wo.equals("Ow")) {
                  //  Toast.makeText(c.getApplicationContext(), list.get(i).getId()+"Abid",Toast.LENGTH_LONG).show();
                    c.startActivity(new Intent(c, Show_Product.class).putExtra("Id", list.get(i).getId()).putExtra("Uid",list.get(i).getName()));
                }
            }
        });

    }
        catch(Exception e)
        {
            Log.d("TAG",e.getMessage());
        }



    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class MED extends RecyclerView.ViewHolder {
        CircleImageView ph;
        TextView name,price;
        CardView buy;
        LinearLayout ed;
        Button edit,delete;
        public MED(@NonNull View v) {
            super(v);
            ph=v.findViewById(R.id.ph);
            name=v.findViewById(R.id.name);
            price=v.findViewById(R.id.price);
            buy=v.findViewById(R.id.buy);
            ed=v.findViewById(R.id.ed);
            edit=v.findViewById(R.id.edit);
            delete=v.findViewById(R.id.delete);
        }
    }
}
