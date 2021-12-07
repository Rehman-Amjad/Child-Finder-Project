package com.example.childfinderproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {



    Animation topanim,bottomAnim;
    TextView tv_sp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_sp1=findViewById(R.id.tv_sp1);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        topanim = AnimationUtils.loadAnimation(this,R.anim.myanimation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        tv_sp1.startAnimation(bottomAnim);


        Thread mythread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                    Intent intent=new Intent(MainActivity.this,RegisterScreen.class);
                    startActivity(intent);
                    finish();  //No return back close screen
                }


            }
        });
        mythread.start();


    }
}