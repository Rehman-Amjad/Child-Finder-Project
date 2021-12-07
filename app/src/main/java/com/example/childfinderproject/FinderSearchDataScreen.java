package com.example.childfinderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.childfinderproject.Model.FinderAdapter;
import com.example.childfinderproject.Model.FinderUploadDataClass;
import com.example.childfinderproject.Model.ParentUploadDataClass;
import com.example.childfinderproject.Model.UserAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FinderSearchDataScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FinderAdapter mUserAdapter;
    private List<ParentUploadDataClass> mDatalist;

    FirebaseDatabase database;
    DatabaseReference myRef;

    ImageView img_parent_search_back;

    private ChildEventListener MyChildEventListener;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.removeEventListener(MyChildEventListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_search_data_screen);


        img_parent_search_back=findViewById(R.id.img_parent_search_back);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("parentUploadData");



        img_parent_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent backIntent= new Intent(FinderSearchDataScreen.this,FinderMenuScreen.class);
                startActivity(backIntent);
                finish();


            }
        });

        mDatalist=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUserAdapter=new FinderAdapter(this,mDatalist);
        recyclerView.setAdapter(mUserAdapter);

        MyChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {

              if (snapshot.exists())
              {
                  ParentUploadDataClass user=snapshot.getValue(ParentUploadDataClass.class);
            /*Log.d(TAG,"User Name :" + user.getUserName());
            Log.d(TAG,"User Name :" + user.getUserPassword());*/
                  mDatalist.add(user);
                  mUserAdapter.notifyDataSetChanged();
              }
              else
              {
                  Toast.makeText(FinderSearchDataScreen.this, "No Image Found", Toast.LENGTH_SHORT).show();
              }


            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @com.google.firebase.database.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        myRef.addChildEventListener(MyChildEventListener);


    }
}