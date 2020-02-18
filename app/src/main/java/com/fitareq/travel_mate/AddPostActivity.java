package com.fitareq.travel_mate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddPostActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private EditText TLocation, TDate;
    private EditText TPlan;
    private Button AddPostBtn;
    private ImageButton BtnOk, BtnCancel;
    private Toolbar AddPostToolbar;
    DatabaseReference PostRef, userRef, userRef1;
    StorageReference imageRef;
    String FullName, ProfileImage, CurrentDateAndTime, currrent_user, postImageUrl= "first";
    private LinearLayout progressBar, FullImageView ;
    private RelativeLayout mainViewToolbar;
    private FrameLayout mainView;
    private ImageView AddImageBtn, PostImage, FullImage;

    String id;
    AddPost usersposts = new AddPost();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

       initialiZation();

        gatherCurrentUserNameandProfileImage();





        AddPostToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onBackPressed();
            }
        });


        TDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String format = "dd-MM-yyyy";
                setDate fdate = new setDate(TDate,AddPostActivity.this, format);
                fdate.onFocusChange(TDate,true);
            }
        });
        AddPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                AddPostToDatabase();
            }
        });





    }//oncreate method








    private void gatherCurrentUserNameandProfileImage() {
        userRef1.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("fullName"))
                    {
                        FullName = dataSnapshot.child("fullName").getValue().toString();
                    }
                    else
                        Toast.makeText(AddPostActivity.this, "no fullname", Toast.LENGTH_LONG).show();
                    if (dataSnapshot.hasChild("profileImage"))
                    {
                        ProfileImage = dataSnapshot.child("profileImage").getValue().toString();
                    }else
                        Toast.makeText(AddPostActivity.this, "no profileimage", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(AddPostActivity.this, "datasnapshot dosen't exist", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }









    private void initialiZation()
    {
        progressBar = findViewById(R.id.progressbar);

        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        PostRef = FirebaseDatabase.getInstance().getReference("users_post");
        userRef1 = userRef = FirebaseDatabase.getInstance().getReference().child("users");

        id = PostRef.push().getKey();
        userRef1 = userRef = FirebaseDatabase.getInstance().getReference().child("users");
        TLocation = findViewById(R.id.add_post_travel_location);
        TDate = findViewById(R.id.add_post_travel_date);
        TPlan = findViewById(R.id.add_post_travel_plan);
        AddPostBtn = findViewById(R.id.add_post_button);
        AddPostToolbar = findViewById(R.id.add_post_toolbar);
        mainViewToolbar = findViewById(R.id.add_post_top);
        mainView = findViewById(R.id.main_view);
        currrent_user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        setSupportActionBar(AddPostToolbar);
        getSupportActionBar().setTitle("Travel Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void AddPostToDatabase()
    {


        String travellocation = TLocation.getText().toString();
        String traveldate = TDate.getText().toString();
        String travelplan = TPlan.getText().toString();
        int totallikes = 0, totalcomment=0;
        String locale = Locale.getDefault().toString();
        SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sd.setTimeZone(TimeZone.getTimeZone(locale));
        CurrentDateAndTime = sd.format(new Date());



        if (TextUtils.isEmpty(travellocation))
        {
            TLocation.setError("You must set a location");
        }
        else if (TextUtils.isEmpty(traveldate))
        {
            TDate.setError("You must set a date");
        }
        else if (TextUtils.isEmpty(travelplan))
        {
            TPlan.setError("You must describe your plan");
        }
        else
        {
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            usersposts.setTravelLocation(travellocation);
            usersposts.setTravelDate(traveldate);
            usersposts.setTravelPlan(travelplan);
            usersposts.setUserId(userid);
            usersposts.setFullName(FullName);
            usersposts.setProfileImage(ProfileImage);
            usersposts.setCurrentDateAndTime(CurrentDateAndTime);
            usersposts.setTotalLikes(totallikes);
            usersposts.setTimeZone(locale);
            usersposts.setTotalComments(totalcomment);


            userRef = userRef.child(userid);

            //AddPost usersposts = new AddPost(travellocation, traveldate, travelplan, userid, FullName, ProfileImage, CurrentDateAndTime, totallikes, locale);
            PostRef.child(id).setValue(usersposts);
            userRef.child("posts").child(id).setValue(usersposts);
            Toast.makeText(this, "Successfully saved", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AddPostActivity.this,HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            progressBar.setVisibility(View.GONE);
        }

    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddPostActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
