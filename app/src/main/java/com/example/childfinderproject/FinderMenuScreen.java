package com.example.childfinderproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FinderMenuScreen extends AppCompatActivity {

    ImageView img_finder_upData,img_finder_searchBaby,img_back_finder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_menu_screen);



        img_finder_upData=findViewById(R.id.img_finder_upData);
        img_finder_searchBaby=findViewById(R.id.img_finder_searchBaby);
        img_back_finder=findViewById(R.id.img_back_finder);


        img_back_finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(FinderMenuScreen.this,ConfirmScreen.class);
                startActivity(backIntent);
                finish();
            }
        });


        img_finder_upData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(FinderMenuScreen.this,FinderUploadDataScreen.class);
                startActivity(backIntent);

            }
        });

        img_finder_searchBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(FinderMenuScreen.this,FinderSearchDataScreen.class);
                startActivity(backIntent);
            }
        });



    }
}