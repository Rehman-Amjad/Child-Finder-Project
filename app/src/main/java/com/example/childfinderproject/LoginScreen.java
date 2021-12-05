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
import android.widget.Toast;

import com.example.childfinderproject.Model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginScreen extends AppCompatActivity {

    EditText ed_login_user,ed_login_pass;
    Button btn_login;
    ImageView img_login_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        ed_login_user=findViewById(R.id.ed_login_user);
        ed_login_pass=findViewById(R.id.ed_login_pass);
        btn_login=findViewById(R.id.btn_login);
        img_login_back=findViewById(R.id.img_login_back);


        img_login_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent(LoginScreen.this,RegisterScreen.class);
                startActivity(backIntent);
                finish();
            }
        });






        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference= FirebaseDatabase.getInstance().getReference("UserAccountInfo");
                String user=ed_login_user.getText().toString();
                String pass=ed_login_pass.getText().toString();


                if (TextUtils.isEmpty(user))
                {
                    ed_login_user.setError("Enter Username");
                    ed_login_user.setText("");
                    ed_login_user.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pass))
                {
                    ed_login_pass.setError("Enter Username");
                    ed_login_pass.setText("");
                    ed_login_pass.requestFocus();
                    return;
                }

                Query checkuser=reference.orderByChild("userName").equalTo(user);

                checkuser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists())
                        {
                            String chechUser = snapshot.child(user).child("userName").getValue(String.class);
                            String checkpassword = snapshot.child(user).child("userPassword").getValue(String.class);


                            if (chechUser.equals(user))
                            {
                                if (checkpassword.equals(pass))
                                {
                                    User myuser = snapshot.getValue(User.class);

                                    Intent nextIntent = new Intent(LoginScreen.this,ConfirmScreen.class);
                                    startActivity(nextIntent);
                                    finish();

                                }
                                else
                                {
                                    ed_login_pass.setError("incorrect password");
                                    ed_login_pass.requestFocus();
                                    ed_login_pass.setText("");
                                    return;
                                }
                            }
                            else
                            {
                                ed_login_user.setError("incorrect username");
                                ed_login_user.requestFocus();
                                ed_login_user.setText("");
                                return;
                            }

                        }
                        else
                        {
                            ed_login_user.setError("Username Doesn't exist");
                            ed_login_user.requestFocus();
                            ed_login_user.setText("");
                            return;
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