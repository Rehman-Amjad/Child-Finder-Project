package com.example.childfinderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPScreen extends AppCompatActivity {

    EditText ed_otp_code;
    Button btn_verify;
    String PhoneNumber;
    String otpId;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_screen);

        btn_verify=findViewById(R.id.btn_verify);
        ed_otp_code=findViewById(R.id.ed_otp_code);

        mAuth = FirebaseAuth.getInstance();

        PhoneNumber = getIntent().getStringExtra("mobile").toString();

        initiateotp();

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ed_otp_code.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(),"Blank Field can not be processed",Toast.LENGTH_LONG).show();
                else if(ed_otp_code.getText().toString().length()!=6)
                    Toast.makeText(getApplicationContext(),"INvalid OTP",Toast.LENGTH_LONG).show();
                else
                {
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpId,ed_otp_code.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

    }

    private void initiateotp()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                PhoneNumber,
                60, TimeUnit.SECONDS, OTPScreen.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
                    {
                       otpId = s;
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
                    {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e)
                    {
                        Toast.makeText(getApplicationContext(),"Number Not Found!",Toast.LENGTH_LONG).show();
                    }
                }
        );


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {

                            Intent nextIntent=new Intent(OTPScreen.this,LoginScreen.class);
                            startActivity(nextIntent);
                            Toast.makeText(OTPScreen.this, "Verified", Toast.LENGTH_SHORT).show();

                        } else {
                            ed_otp_code.setError("invalid Code");
                            ed_otp_code.requestFocus();
                            Toast.makeText(getApplicationContext(),"Signin Code Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}