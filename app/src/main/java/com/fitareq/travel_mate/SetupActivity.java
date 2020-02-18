package com.fitareq.travel_mate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private static final int IMAGE_BACK = 1;
    private CircleImageView ProfileImage;
    private ImageView ProfileImageAddBtn;
    private Button UserDataSaveBtn, nextButton;
    private EditText FullName, Address1, Address2, DateOfBirth, UserIdentity,Email;
    private Spinner Gender;
    private CountryCodePicker Country;
    private Toolbar toolbar;
    private StorageReference profileImageRef;
    private String profileImageUrl;
    private DatabaseReference userRef;
    private LinearLayout progressbar;

    private final static int REQUEST_PERMISSION_READ_EXTERNAL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        profileImageRef = FirebaseStorage.getInstance().getReference().child("profile_image");
        userRef = FirebaseDatabase.getInstance().getReference("users");
        progressbar = findViewById(R.id.setup_progressbar);


        ProfileImage = findViewById(R.id.setup_user_profile_image);
        ProfileImageAddBtn = findViewById(R.id.setup_add_profile_image);
        UserDataSaveBtn = findViewById(R.id.setup_save_button);
        FullName = findViewById(R.id.setup_fullname);
        Email = findViewById(R.id.setup_email);
        Address1 = findViewById(R.id.setup_address1);
        Address2 = findViewById(R.id.setup_address2);
        DateOfBirth = findViewById(R.id.setup_date_of_birth);
        UserIdentity = findViewById(R.id.setup_nid_passport_driving_license);
        Gender = findViewById(R.id.setup_user_gender);
        Country = findViewById(R.id.setup_ccp);
        nextButton = findViewById(R.id.setup_next_button);

        toolbar = findViewById(R.id.setup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Profile");


        ProfileImageAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int readExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    String[] requirePermission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(SetupActivity.this, requirePermission, REQUEST_PERMISSION_READ_EXTERNAL);
                }
                progressbar.setVisibility(View.VISIBLE);
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_BACK);*/
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setMinCropWindowSize(1000,1000)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setAllowFlipping(false)
                        .start(SetupActivity.this);




            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches())
                {
                    Email.setError("Email is not valid");
                }
                else
                {
                    findViewById(R.id.linear_layout).setVisibility(View.GONE);
                    findViewById(R.id.scroll_view).setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.GONE);
                    UserDataSaveBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        UserDataSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUserDataToDatabase();
            }
        });


        DateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String format = "dd-MM-yyyy";
                setDate fromDate = new setDate(DateOfBirth, SetupActivity.this, format);
                fromDate.onFocusChange(DateOfBirth, true);
            }
        });

    }

    private void SaveUserDataToDatabase() {


        String email = Email.getText().toString();
        String fullname = FullName.getText().toString();
        String address = Address1.getText().toString() + "," + Address2.getText().toString();
        String dop = DateOfBirth.getText().toString();
        String useridentiy = UserIdentity.getText().toString();
        String gender = Gender.getSelectedItem().toString();
        String country = Country.getSelectedCountryName();
        if (TextUtils.isEmpty(fullname)) {
            FullName.setError("Required field");
        } else if (TextUtils.isEmpty(address)) {
            Address1.setError("Required field");
        } else if (TextUtils.isEmpty(dop)) {
            DateOfBirth.setError("Required field");
        } else if (TextUtils.isEmpty(useridentiy)) {
            UserIdentity.setError("Required field");
        } else {
            progressbar.setVisibility(View.VISIBLE);
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String phone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
            SavingUserInfo userInfo = new SavingUserInfo(fullname, address, dop, useridentiy, gender, country, profileImageUrl, phone, email);
            userRef.child(userid).setValue(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent intent = new Intent(SetupActivity.this, HomeActivity.class);
                    startActivity(intent);
                    progressbar.setVisibility(View.GONE);
                    finish();
                }
            });

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




            //final StorageReference imageRef = profileImageRef.child(FirebaseAuth.getInstance().getUid()).child(UUID.randomUUID().toString());
                /*imageRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                profileImageUrl = uri.toString();
                                Picasso.get().load(profileImageUrl).into(ProfileImage);
                                progressbar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
            else
                progressbar.setVisibility(View.GONE);
        }*/

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK)
        {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            String cuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String random = UUID.randomUUID().toString();
            final StorageReference imageRef = profileImageRef.child(cuser).child(random);

            imageRef.putFile(result.getUri()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            profileImageUrl = uri.toString();
                            Picasso.get().load(uri).into(ProfileImage);
                            progressbar.setVisibility(View.GONE);
                        }
                    });
                }
            });



        }
        else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
        {
            Toast.makeText(this, CropImage.getActivityResult(data).getError().toString(),  Toast.LENGTH_SHORT).show();
            progressbar.setVisibility(View.GONE);
        }
        else
            progressbar.setVisibility(View.GONE);
    }
}
