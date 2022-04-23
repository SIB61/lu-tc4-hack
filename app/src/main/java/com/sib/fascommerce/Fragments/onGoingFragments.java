package com.sib.fascommerce.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sib.fascommerce.Common.SessionManager;
import com.sib.fascommerce.Customer.Pro;
import com.sib.fascommerce.Customer.TipAdapter;
import com.sib.fascommerce.Customer.User_Section1;
import com.sib.fascommerce.DataModels.ProductModel;
import com.sib.fascommerce.R;
import com.sib.fascommerce.Seller.OnGoingAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class onGoingFragments extends Fragment {

    public onGoingFragments() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_going_fragments, container, false);
        ArrayList<ProductModel> li=new ArrayList<>();
       RecyclerView rt= view.findViewById(R.id.rt);
        OnGoingAdapter og=new OnGoingAdapter(li);

        SessionManager sh = new SessionManager(getContext(), SessionManager.USERSESSION);
        HashMap<String, String> hm = sh.returnData();
        String uid=hm.get(SessionManager.UID);
        rt.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rt.setAdapter(og);
        FirebaseDatabase.getInstance().getReference("AllProducts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren())
                {
                    ProductModel pd=s.getValue(ProductModel.class);
                    Toast.makeText(getContext(), pd.getUid(), Toast.LENGTH_SHORT).show();
                    if(pd.getUid()== FirebaseAuth.getInstance().getUid())
                    li.add(pd);
                }
                OnGoingAdapter og=new OnGoingAdapter(li);
                rt.setAdapter(og);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}