package com.fitareq.travel_mate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.r0adkll.slidr.Slidr;

public class MainActivity extends AppCompatActivity {

    private Button SignIn;
    private int backPreesed=0;
    private FirebaseAuth mAuth;
    ActiveStatus updateActiveStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        updateActiveStatus = new ActiveStatus();

        mAuth = FirebaseAuth.getInstance();

        SignIn = findViewById(R.id.start_page_signin);


        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent signinIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(signinIntent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuser = mAuth.getCurrentUser();
        if(currentuser!=null)
        {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }


    }


    @Override
    public void onBackPressed() {
        backPreesed+=1;

        if (backPreesed>1)
        {
            super.onBackPressed();
        }

        Toast.makeText(this, "Press back again to exit...", Toast.LENGTH_LONG).show();
    }
}
