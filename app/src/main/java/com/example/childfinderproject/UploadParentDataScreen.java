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

import com.example.childfinderproject.Model.ParentUploadDataClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadParentDataScreen extends AppCompatActivity {

    ImageView img_upload;
    Button btn_parentUploadImage,btn_ParentUploadNext;
    EditText ed_parent_name,ed_phoneNumber,ed_baby_name,ed_baby_Age,ed_parent_Address;

    Bitmap galbitmap_st;
    Uri imageuri2;

    String sImage2;
    String parentcounter;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_parent_data_screen);

        img_upload=findViewById(R.id.img_upload);

        btn_parentUploadImage=findViewById(R.id.btn_parentUploadImage);
        btn_ParentUploadNext=findViewById(R.id.btn_ParentUploadNext);

        ed_parent_name=findViewById(R.id.ed_parent_name);
        ed_phoneNumber=findViewById(R.id.ed_phoneNumber);
        ed_baby_name=findViewById(R.id.ed_baby_name);
        ed_baby_Age=findViewById(R.id.ed_baby_Age);
        ed_parent_Address=findViewById(R.id.ed_parent_Address);


        if (ContextCompat.checkSelfPermission(UploadParentDataScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadParentDataScreen.this, new String[]{
                    Manifest.permission.CAMERA


            }, 100);
        }


        DatabaseReference counter = FirebaseDatabase.getInstance().getReference("counterCheck");


        counter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                parentcounter = snapshot.child("parentCounter").getValue(String.class);

                count = Integer.parseInt(parentcounter);
                count=count+1;
                parentcounter =String.valueOf(count);
                counter.child("parentCounter").setValue(String.valueOf(parentcounter));

                Toast.makeText(UploadParentDataScreen.this, ""+parentcounter, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        btn_parentUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 16);

                btn_ParentUploadNext.setVisibility(View.VISIBLE);
            }
        });



        btn_ParentUploadNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String parentName = ed_parent_name.getText().toString().trim();
                String parentMobileNumber = ed_phoneNumber.getText().toString().trim();
                String babyFullName = ed_baby_name.getText().toString().trim();
                String babyAge = ed_baby_Age.getText().toString();
                String parentAddress = ed_parent_Address.getText().toString();


                if (TextUtils.isEmpty(parentName))
                {
                    ed_parent_name.setError("Enter Parent Name");
                    ed_parent_name.setText("");
                    ed_parent_name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(parentMobileNumber) || parentMobileNumber.length() < 10 || parentMobileNumber.length() > 10)
                {
                    ed_phoneNumber.setError("invalid Number");
                    ed_phoneNumber.setText("");
                    ed_phoneNumber.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(babyFullName))
                {
                    ed_baby_name.setError("Enter Baby Name");
                    ed_baby_name.setText("");
                    ed_baby_name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(babyAge))
                {
                    ed_baby_Age.setError("Enter Parent Name");
                    ed_baby_Age.setText("");
                    ed_baby_Age.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(parentAddress))
                {
                    ed_parent_Address.setError("Enter Parent Name");
                    ed_parent_Address.setText("");
                    ed_parent_Address.requestFocus();
                    return;
                }


                DatabaseReference myRef= FirebaseDatabase.getInstance().getReference("parentUploadData");
                ParentUploadDataClass dataClass = new ParentUploadDataClass(parentName,parentMobileNumber,babyFullName,babyAge,parentAddress,sImage2);
                myRef.child(String.valueOf(count)).setValue(dataClass);

                SharedPreferences preferences = getSharedPreferences("PARENTIMAGE",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("PARENTID",String.valueOf(count));
                editor.putString("IMAGE",sImage2);
                editor.apply();

                Intent nextIntent = new Intent(UploadParentDataScreen.this,ParentLocationScreen.class);
                startActivity(nextIntent);
                finish();


                Toast.makeText(UploadParentDataScreen.this, "Data Uploaded Successful", Toast.LENGTH_SHORT).show();


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
                img_upload.setImageBitmap(galbitmap_st);

            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

}