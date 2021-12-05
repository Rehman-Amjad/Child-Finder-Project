package com.example.childfinderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.childfinderproject.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class SignupScreen extends AppCompatActivity {

    ImageView img_back_signUp;
    EditText ed_userFullName,ed_username,ed_phoneNumber,ed_password,ed_confirm_pass;
    Button btn_SignNext;

    CountryCodePicker ccp;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        btn_SignNext=findViewById(R.id.btn_SignNext);

        //Image typecasting
        img_back_signUp=findViewById(R.id.img_back_signUp);

        //EditText typecasting
        ed_userFullName=findViewById(R.id.ed_userFullName);
        ed_username=findViewById(R.id.ed_username);
        ed_phoneNumber=findViewById(R.id.ed_phoneNumber);
        ed_password=findViewById(R.id.ed_password);
        ed_confirm_pass=findViewById(R.id.ed_confirm_pass);

        //county Code Picker
        ccp=findViewById(R.id.ccp);

        ccp.registerCarrierNumberEditText(ed_phoneNumber);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference("UserAccountInfo");


        img_back_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(SignupScreen.this,RegisterScreen.class);
                startActivity(backIntent);
                finish();
            }
        });



        btn_SignNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName=ed_username.getText().toString();
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("UserAccountInfo");

                Query query=reference.orderByChild("userName").equalTo(userName);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists())
                        {
                            String checkuser=snapshot.child(userName).child("userName").getValue(String.class);

                            if (checkuser.equals(userName))
                            {
                                ed_username.setError("Username Already Exist!");
                                ed_username.requestFocus();
                                ed_username.setText("");
                                return;

                            }
                        }
                        else
                        {
                            String userFullName=ed_userFullName.getText().toString().trim();
                            String userName=ed_username.getText().toString().trim();
                            String userPhone=ed_phoneNumber.getText().toString().trim();
                            String userPassword=ed_password.getText().toString().trim();
                            String UserConPassword=ed_confirm_pass.getText().toString();

                            if (TextUtils.isEmpty(userFullName))
                            {
                                ed_userFullName.setError("Enter Name!");
                                ed_userFullName.requestFocus();
                                return;
                            }


                            if (TextUtils.isEmpty(userName))
                            {
                                ed_username.setError("Enter userName!");
                                ed_username.requestFocus();
                                return;
                            }

                            if (TextUtils.isEmpty(userPhone) || userPhone.length() < 10)
                            {
                                ed_phoneNumber.setError("invalid Phone Number");
                                ed_phoneNumber.requestFocus();
                                return;
                            }



                            if (TextUtils.isEmpty(userPassword))
                            {
                                ed_password.setError("Enter Password!");
                                ed_password.requestFocus();
                                return;
                            }

                            if (TextUtils.isEmpty(UserConPassword))
                            {
                                ed_confirm_pass.setError("Enter Password!");
                                ed_confirm_pass.requestFocus();
                                return;
                            }

                            if (ed_password.getText().toString().equals(ed_confirm_pass.getText().toString().trim()))
                            {
                                User myuser = new User(userFullName,userName,userPhone,userPassword);
                                String key=myRef.child(userName).getKey();
                                myRef.child(key).setValue(myuser);
                                Intent otpIntent = new Intent(SignupScreen.this,OTPScreen.class);
                                otpIntent.putExtra("mobile",ccp.getFullNumberWithPlus().replace("",""));
                                startActivity(otpIntent);
                                finish();
                            }
                            else
                            {
                                ed_password.setError("Password Doesn't Match");
                                ed_password.requestFocus();
                                ed_password.setText("");
                                ed_confirm_pass.setText("");
                            }


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });




    }
}