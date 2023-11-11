package com.example.childfinderproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.childfinderproject.Model.FinderUploadDataClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FinderUploadDataScreen extends AppCompatActivity {

    ImageView img_Finder_menu_back,img_upload_finder;
    Button btn_FinderUploadImage,btn_FinderUploadNext;
    EditText ed_Finder_name,ed_Finder_phoneNumber,ed_baby_name,ed_baby_Age,ed_Finder_Address;

    Bitmap galbitmap_st;
    Uri imageuri2;

    String sImage2;
    String parentcounter;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_upload_data_screen);

        img_Finder_menu_back=findViewById(R.id.img_Finder_menu_back);
        img_upload_finder=findViewById(R.id.img_upload_finder);

        btn_FinderUploadImage=findViewById(R.id.btn_FinderUploadImage);
        btn_FinderUploadNext=findViewById(R.id.btn_FinderUploadNext);

        ed_Finder_name=findViewById(R.id.ed_Finder_name);
        ed_Finder_phoneNumber=findViewById(R.id.ed_Finder_phoneNumber);
        ed_baby_name=findViewById(R.id.ed_baby_name);
        ed_baby_Age=findViewById(R.id.ed_baby_Age);
        ed_Finder_Address=findViewById(R.id.ed_Finder_Address);


        img_Finder_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(FinderUploadDataScreen.this,FinderMenuScreen.class);
                startActivity(backIntent);
                finish();
            }
        });


        if (ContextCompat.checkSelfPermission(FinderUploadDataScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FinderUploadDataScreen.this, new String[]{
                    Manifest.permission.CAMERA


            }, 100);
        }


        DatabaseReference counter = FirebaseDatabase.getInstance().getReference("counterCheck");

        counter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parentcounter = snapshot.child("userCounter").getValue(String.class);

                count = Integer.parseInt(parentcounter);
                count=count+1;
                parentcounter = String.valueOf(count);
                counter.child("userCounter").setValue(parentcounter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn_FinderUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 16);

                btn_FinderUploadNext.setVisibility(View.VISIBLE);
            }
        });


        btn_FinderUploadNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String FinderName = ed_Finder_name.getText().toString().trim();
                String FinderMobileNumber = ed_Finder_phoneNumber.getText().toString().trim();
                String babyFullName = ed_baby_name.getText().toString().trim();
                String babyAge = ed_baby_Age.getText().toString();
                String FinderAddress = ed_Finder_Address.getText().toString();


                if (TextUtils.isEmpty(FinderName))
                {
                    ed_Finder_name.setError("Enter Parent Name");
                    ed_Finder_name.setText("");
                    ed_Finder_name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(FinderMobileNumber) || FinderMobileNumber.length() < 10 || FinderMobileNumber.length() > 10)
                {
                    ed_Finder_phoneNumber.setError("invalid Number");
                    ed_Finder_phoneNumber.setText("");
                    ed_Finder_phoneNumber.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(FinderAddress))
                {
                    ed_Finder_Address.setError("Enter Parent Name");
                    ed_Finder_Address.setText("");
                    ed_Finder_Address.requestFocus();
                    return;
                }

                DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("FinderUploadData");

                FinderUploadDataClass dataClass = new FinderUploadDataClass(FinderName,FinderMobileNumber,babyFullName,babyAge,FinderAddress,sImage2,parentcounter);
                myRef.child(String.valueOf(count)).setValue(dataClass);

                SharedPreferences preferences = getSharedPreferences("FINDERIMAGE",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("FINDER_ID",String.valueOf(count));
                editor.putString("FINDER_IMAGE",sImage2);
                editor.apply();

                Intent intent = new Intent(FinderUploadDataScreen.this,FinderLocationScreen.class);
                startActivity(intent);
                finish();

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==16 && resultCode==RESULT_OK && data!=null) {
            imageuri2 = data.getData();
            try {
                galbitmap_st = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri2);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                galbitmap_st.compress(Bitmap.CompressFormat.JPEG,100,stream);
                byte[] bytes = stream.toByteArray();
                sImage2 = Base64.encodeToString(bytes,Base64.DEFAULT);
                img_upload_finder.setImageBitmap(galbitmap_st);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

}