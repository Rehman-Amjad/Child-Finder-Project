package com.example.childfinderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.childfinderproject.Model.ParentImage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentLocationScreen extends AppCompatActivity {

    String ID;
    String img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_location_screen);







         DatabaseReference imgRef = FirebaseDatabase.getInstance().getReference("parentImage");

        imgRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                SharedPreferences preferences = getSharedPreferences("PARENTIMAGE",MODE_PRIVATE);
                ID = preferences.getString("PARENTID","");
                img = preferences.getString("IMAGE","");
                ParentImage image = new ParentImage(ID,img);
                imgRef.child(ID).setValue(image);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}