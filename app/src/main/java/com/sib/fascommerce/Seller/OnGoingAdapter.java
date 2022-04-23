package com.sib.fascommerce.Seller;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.sib.fascommerce.Customer.Pro;
import com.sib.fascommerce.DataModels.ProductModel;
import com.sib.fascommerce.DataModels.ProductModel;
import com.sib.fascommerce.R;

import java.util.List;

public class OnGoingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ProductModel> models;
    public OnGoingAdapter(List<ProductModel> models) {
        this.models=models;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expireditem,parent,false)) {

        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.imageItem);
        TextView textView = holder.itemView.findViewById(R.id.itemTitle);
        TextView textView1=holder.itemView.findViewById(R.id.desEX);
        try{
           FirebaseStorage.getInstance().getReference(models.get(position).getpUrl()+"/"+models.get(position).getpUrl()+"0").getDownloadUrl().addOnSuccessListener(uri -> {
               try{
                   Glide.with(holder.itemView.getContext()).load(uri).into(imageView);
               }
               catch (Exception e){

               }
           });

        }
        catch (Exception e)
        {

        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
