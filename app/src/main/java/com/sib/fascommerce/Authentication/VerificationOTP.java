package com.sib.fascommerce.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sib.fascommerce.Admin.AdminHomePage;
import com.sib.fascommerce.Common.SessionManager;
import com.sib.fascommerce.Customer.User_Section1;
import com.sib.fascommerce.MainActivity;
import com.sib.fascommerce.R;
import com.sib.fascommerce.Seller.SellerHome;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerificationOTP extends AppCompatActivity {

    private int accountType;
    private String phoneNumber, phoneNumber1;
    String codeBySystem;
    private TextView smsNumber;

    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;

    private String verificationId;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_otp);

        editTextCode = findViewById(R.id.verification_activity_get_opt);
        smsNumber = findViewById(R.id.verification_activity_sms);


        mAuth = FirebaseAuth.getInstance();

        accountType = getIntent().getExtras().getInt("ACCOUNT_TYPE");
        phoneNumber = getIntent().getExtras().getString("PHONE_NUMBER");
        phoneNumber1 = "+88" + phoneNumber;
        Log.d("TAG", phoneNumber);

        smsNumber.setText("You will get an OPT via SMS \n" + phoneNumber1);

        sendVerificationCode(phoneNumber1);

//
//        findViewById(R.id.verification_activity_Btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String code = editTextCode.getText().toString().trim();
//                if (code.isEmpty() || code.length() < 6) {
//                    editTextCode.setError("Enter valid code");
//                    editTextCode.requestFocus();
//                    return;
//                }
//
//                //verifying the code entered manually
//                verifyVerificationCode(code);
//            }
//        });

    }


    public void ver(View v) {
        verifyCode(editTextCode.getText().toString());

    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.

                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(task4 -> {
                                        if (task4.isSuccessful()) {
                                            String wh = "";
                                            if (accountType == 0)
                                                wh = "Customer";
                                            else if (accountType == 1)
                                                wh = "Seller";
                                            else
                                                wh = "Admin";
                                            String uid = mAuth.getUid();
                                            String token = Objects.requireNonNull(task4.getResult()).getToken();
                                            SessionManager sh = new SessionManager(VerificationOTP.this, SessionManager.USERSESSION);
                                            sh.loginSession("NO", "No", phoneNumber1, "No", "0", token, uid, wh, "0");
                                            HashMap hm5 = new HashMap();
                                            hm5.put("Phone", phoneNumber1);
                                            hm5.put("What", wh);
                                            hm5.put("Uid", uid);
                                            hm5.put("Token", token);
                                            hm5.put("Points", "0");
                                            hm5.put("Buy", "0");
                                            FirebaseDatabase.getInstance().getReference("Users").child(wh+"s").child(uid).setValue(hm5);
                                             switch (accountType) {
                                                 case 0:
                                                 startActivity(new Intent(VerificationOTP.this, User_Section1.class));
                                                  break;
                                                 case 1:
                                                     startActivity(new Intent(VerificationOTP.this, SellerHome.class));
                                                     break;
                                                 case 2:
                                                     startActivity(new Intent(VerificationOTP.this, AdminHomePage.class ));
                                                     break;

                                             }
                                             finish();

                                             } else {
                                            Log.d("TAG", "signUp: " + task4.getException());
                                        }
                                    });

                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(VerificationOTP.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("TAG", task.getException().getMessage());
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                editTextCode.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(VerificationOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        if (code != null || !code.isEmpty()) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

            // after getting credential we are
            // calling sign in method.
            signInWithCredential(credential);
        }
    }
}
