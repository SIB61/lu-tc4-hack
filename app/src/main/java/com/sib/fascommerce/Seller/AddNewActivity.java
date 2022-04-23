package com.sib.fascommerce.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sib.fascommerce.Authentication.Registration;
import com.sib.fascommerce.Common.SessionManager;
import com.sib.fascommerce.Customer.SliderAdapter;
import com.sib.fascommerce.DataModels.ProductModel;
import com.sib.fascommerce.R;
import com.sib.fascommerce.databinding.ActivityAddNewBinding;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.zfdang.multiple_images_selector.ImagesSelectorActivity;
import com.zfdang.multiple_images_selector.SelectorSettings;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class AddNewActivity extends AppCompatActivity {
private ActivityAddNewBinding binding;
private static final int REQUEST_CODE = 123;
private Calendar c;
private DatePickerDialog datePicker;
private int y,m,d;
private ArrayList<String> mResults = new ArrayList<>();
private ProductModel productModel;
private StorageReference storageReference;
private String productID ;
private String[] options = {"automatic","manual"};
    HashMap<String, String> hm1;
    String buy="",email,token,phone,fname,wh,points,uid,url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.materialToolbarAN);
        SessionManager sh = new SessionManager(AddNewActivity.this, SessionManager.USERSESSION);
        hm1 = sh.returnData();
        buy=hm1.get(SessionManager.BUY);
        url=hm1.get(SessionManager.URL);
        fname=hm1.get(SessionManager.FULLNAME);
        uid=hm1.get(SessionManager.UID);
        points=hm1.get(SessionManager.POINTS);
        email=hm1.get(SessionManager.EMAIL);
        wh=hm1.get(SessionManager.WHAT);
        token=hm1.get(SessionManager.TOKEN);
        phone=hm1.get(SessionManager.PHONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Fresco.initialize(getApplicationContext());
        Random ne=new Random();
        long by=ne.nextInt(1000000000);
        productID= FirebaseAuth.getInstance().getUid()+by;
      /*  FirebaseDatabase.getInstance().getReference("Users").child("Sellers").child(uid).child("Buy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buy=snapshot.getValue().toString();
                int b=Integer.parseInt(buy);
                b++;

                SessionManager shy=new SessionManager(AddNewActivity.this,SessionManager.USERSESSION);
                shy.loginSession(fname,email,phone,url,points,token,uid,wh,b+"");
                HashMap mp=new HashMap();

                mp.put("Buy",b+"");

                FirebaseDatabase.getInstance().getReference("Users").child("Sellers").child(uid).child("Buy").updateChildren(mp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_LONG).show();
        storageReference = FirebaseStorage.getInstance().getReference(productID);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.What,R.layout.support_simple_spinner_dropdown_item);
        binding.categoryAN.setAdapter(arrayAdapter);
        ArrayAdapter arrayAdapter1=new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,options);
        binding.optionAN.setAdapter(arrayAdapter1);
        binding.dateLayoutAN.setEndIconOnClickListener(v -> {
         setDate();
        });
    }

    public void takePhotos(View view) {


// start multiple photos selector
        Intent intent = new Intent(AddNewActivity.this, ImagesSelectorActivity.class);
// max number of images to be selected
        intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 5);
// min size of image which will be shown; to filter tiny images (mainly icons)
        intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
// show camera or not
        intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
// pass current selected images as the initial value
        intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
// start the selector
        startActivityForResult(intent, REQUEST_CODE);

    }

    public void setDate() {
        c= Calendar.getInstance();
        y=c.get(Calendar.YEAR);
        m=c.get(Calendar.MONTH);
        d=c.get(Calendar.DAY_OF_MONTH);
        datePicker=new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            binding.dateAN.setText(dayOfMonth+"-"+month+"-"+year);
        },y,m,d);
        datePicker.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // get selected images from selector
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                mResults = data.getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
                assert mResults != null;
                SliderAdapter sliderAdapter = new SliderAdapter(AddNewActivity.this,mResults);

                binding.imgAN.setSliderAdapter(sliderAdapter);
                binding.imgAN.setIndicatorAnimation(IndicatorAnimationType.WORM);
                binding.imgAN.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                binding.imgAN.startAutoCycle();
                // show results in textview
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("Totally %d images selected:", mResults.size())).append("\n");
                for(String result : mResults) {
                    sb.append(result).append("\n");

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        binding.imgAN.setVisibility(View.VISIBLE);
        binding.takephotoAN.setVisibility(View.GONE);
    }

    public void addNew(View view) {
        String title = binding.titleAN.getText().toString().trim();
        String category = binding.categoryAN.getText().toString().trim();
        String description = binding.descriptionAN.getText().toString().trim();
        int price = Integer.parseInt(binding.priceAN.getText().toString().trim());
        String winOption = binding.optionAN.getText().toString();
        String date = binding.dateAN.getText().toString().trim();

        if(title.isEmpty()||category.isEmpty()||description.isEmpty()||winOption.isEmpty()||date.isEmpty()||mResults.size()==0||price==1)
        {
            Toast.makeText(this, "Any field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            for(int i = 0; i < mResults.size() ;i++)
            {
               Uri uri = Uri.fromFile(new File(mResults.get(i)));
               storageReference.child(productID+i).putFile(uri);
            }

            ProductModel pd=new ProductModel(category,productID,productID,title,description,winOption,date,uid,email,fname,url,price);
            FirebaseDatabase.getInstance().getReference("AllProducts").child(productID).setValue(pd);
            FirebaseDatabase.getInstance().getReference("AllProductsCategory").child(category).child(productID).setValue(pd);
            FirebaseDatabase.getInstance().getReference("Users").child("Sellers").child(uid).child("Products").child(productID).setValue(pd);
            FirebaseDatabase.getInstance().getReference("Users").child("Sellers").child(uid).child("Buy").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    buy=snapshot.getValue().toString();

                    int b=Integer.parseInt(buy);
                    b++;

                    SessionManager shy=new SessionManager(AddNewActivity.this,SessionManager.USERSESSION);
                    shy.loginSession(fname,email,phone,url,points,token,uid,wh,b+"");
                    HashMap mp=new HashMap();

                    mp.put("Buy",b+"");

                    FirebaseDatabase.getInstance().getReference("Users").child("Sellers").child(uid).updateChildren(mp);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
            Toast.makeText(AddNewActivity.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
            onBackPressed();
    }
}

}
