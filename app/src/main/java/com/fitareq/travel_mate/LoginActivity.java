package com.fitareq.travel_mate;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.hbb20.CountryCodePicker;
import com.r0adkll.slidr.Slidr;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private PhoneAuthProvider.ForceResendingToken ResendingToken;
    private EditText PhoneNumber,OtpCode;
    private Button SignIn, SendOtpCode;
    private String phonenum,code,verificationId;
    private CountryCodePicker countryCodePicker;

    private DatabaseReference userRef;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.signin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Sign In");


        PhoneNumber = findViewById(R.id.signin_phone_number);
        SignIn = findViewById(R.id.signin_btn);
        SendOtpCode = findViewById(R.id.verify_btn);
        OtpCode = findViewById(R.id.otp_edit_text);
        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(PhoneNumber);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        SendOtpCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (isInternetOn())
                {
                    phonenum = countryCodePicker.getFullNumberWithPlus();
                    SendCode(phonenum);
                    final TextView c = findViewById(R.id.countdown);
                    c.setVisibility(View.VISIBLE);
                    new CountDownTimer(60000,1000){


                        @Override
                        public void onTick(long millisUntilFinished) {

                            c.setText((millisUntilFinished/1000)+ "s");
                        }

                        @Override
                        public void onFinish() {
                            c.setText("didn't receive code yet? RESEND");

                        }
                    }.start();
                    findViewById(R.id.phone_container).setVisibility(View.GONE);
                    SendOtpCode.setVisibility(View.GONE);
                    SignIn.setVisibility(View.VISIBLE);
                    OtpCode.setVisibility(View.VISIBLE);
                    OtpCode.requestFocus();
                }
                else
                {
                    TextView c = findViewById(R.id.countdown);
                    c.setVisibility(View.VISIBLE);
                    c.setText("There is a problem in internet connection, please check the internet connection");
                }


            }
        });






    }




    //sending otp code
    private void SendCode(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks
        );
    }




    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential)
        {

            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e)
        {
            Toast.makeText(LoginActivity.this, e.getMessage().toUpperCase(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            ResendingToken = forceResendingToken;
            verificationId = s;

        }
    };



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            SendUserToHomepage();
                        } else {

                            Toast.makeText(LoginActivity.this, task.getException().getMessage().toUpperCase(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    public Boolean isInternetOn()
    {
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);


        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED )
        {
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  )
        {
            return false;
        }
        return false;
    }

    private void SendUserToHomepage()
    {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
