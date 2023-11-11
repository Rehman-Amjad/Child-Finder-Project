package com.example.childfinderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ParentSetPathMapScreen extends AppCompatActivity {

    String lon=null;
    String lat=null;
    String finder_id=null;

    String sDesLat;
    String  sDesLong;

    Button btn_getParentDirection;

    //initilize variable
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    double currentLat = 0, currentLong = 0;
    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_set_path_map_screen);



        btn_getParentDirection=findViewById(R.id.btn_getParentDirection);

//        SharedPreferences preferences = getSharedPreferences("CUURENTDATA", Context.MODE_PRIVATE);
//        String babyImage = preferences.getString("BABY_IMAGE","");
//        finder_id = preferences.getString("FINDER_ID","");
//
//        DatabaseReference latRef = FirebaseDatabase.getInstance().getReference("FinderImage");
//
//        latRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                sDesLat = snapshot.child(finder_id).child("currentLatitude").getValue(String.class);
//                sDesLong = snapshot.child(finder_id).child("currentLongitude").getValue(String.class);
//                //  Toast.makeText(ParentShowInfoScreen.this, ""+sDesLat, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        //Assign variable
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        //initilize fused location
        client = LocationServices.getFusedLocationProviderClient(ParentSetPathMapScreen.this);

        //check permission
        if (ActivityCompat.checkSelfPermission(ParentSetPathMapScreen.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            //when permission granted
            //Call Method
            getCurrentLocation();

        }
        else
        {
            //when permission denied
            //requst permission
            ActivityCompat.requestPermissions(ParentSetPathMapScreen.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }

        getCurrentLocation();

        btn_getParentDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sSource = lat+","+lon;

                String sDestination = "31.41276"+","+"73.11557";
                DisplayTrack(sSource,sDestination);
            }
        });
    }

    private void getCurrentLocation() {

        //initilize task location
        @SuppressLint("MissingPermission")
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                //when Success
                if (location != null)
                {
                    //when location in not equal to null
                    //Get current Latitude
                    currentLat = location.getLatitude();

                    //get Current Longitude
                    currentLong = location.getLongitude();

                    //synMap
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            //when map is ready

                            map = googleMap;


                            //    Just show current location
                            //initilize lat lag
                            LatLng latLng = new LatLng(location.getLatitude(),
                                    location.getLongitude());

                            //craete marker option
                            MarkerOptions options = new MarkerOptions().position(latLng)
                                    .title("i am there");

                            //zoom map
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(currentLat,currentLong),10));

                            //add marker on map
                            googleMap.addMarker(options);

                            lon=String.valueOf(currentLong);
                            lat=String.valueOf(currentLat);

                        }
                    });
                }
            }
        });


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //when permission granted
                //call method
                getCurrentLocation();
            }
        }
    }

    private void DisplayTrack(String sSource, String sDestination)
    {
        //if the device dost not have a map installed, thne directed

        try {
            //when google map is installed
            //initilzied uri
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sSource +"/"
                    + sDestination);

            //initilize intent action view
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);

            //set package
            intent.setPackage("com.google.android.apps.maps");

            //set Flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
        catch (ActivityNotFoundException e)
        {
            //when google map is not installed
            //initilize uri
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            // initlize intentt
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            //set Flags
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
    }
}