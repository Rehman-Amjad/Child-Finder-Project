package com.example.childfinderproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ParentMenuScreen extends AppCompatActivity {

    ImageView img_parent_upData,img_parent_searchBaby,img_parent_menu_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_menu_screen);

        img_parent_upData=findViewById(R.id.img_parent_upData);
        img_parent_searchBaby=findViewById(R.id.img_parent_searchBaby);
        img_parent_menu_back=findViewById(R.id.img_parent_menu_back);



        img_parent_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(ParentMenuScreen.this,ConfirmScreen.class);
                startActivity(uploadIntent);
                finish();
            }
        });


        img_parent_upData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(ParentMenuScreen.this,UploadParentDataScreen.class);
                startActivity(uploadIntent);
            }
        });


        img_parent_searchBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent uploadIntent = new Intent(ParentMenuScreen.this,ParentSearchDataScreen.class);
                startActivity(uploadIntent);
            }
        });


    }
}