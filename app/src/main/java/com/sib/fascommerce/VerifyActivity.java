package com.sib.fascommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.sib.fascommerce.databinding.ActivityVerifyBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class VerifyActivity extends AppCompatActivity {
private Uri resultUri;
ActivityVerifyBinding binding ;
private  int  i = 0 ;
private Uri uri1,uri2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityVerifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void takeFront(View view) {
        i=1;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    public void takeBack(View view) {
        i=2;
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                try{
                    if(i==1) {
                        uri1=resultUri;
                        Glide.with(this).load(resultUri).into(binding.nidfront);
                        binding.nidfront.setVisibility(View.VISIBLE);
                        binding.takenidfront.setVisibility(View.GONE);
                    }

                    else if ( i == 2) {
                        uri2=resultUri;
                        Glide.with(this).load(resultUri).into(binding.nidback);
                        binding.nidback.setVisibility(View.VISIBLE);
                        binding.takenidback.setVisibility(View.GONE);
                    }

                }catch (Exception e){

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void sendRequest(View view) {
        String nid = binding.nidNumber.getText().toString().trim();
        if(nid.isEmpty()||uri1==null||uri2==null)
        {
            Toast.makeText(this, "Request send successfully", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
}