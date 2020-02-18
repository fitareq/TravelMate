package com.fitareq.travel_mate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    private static final int IMAGE_BACK = 1;
    private CircleImageView ProfileImage, ProfileImageAddButton;
    private ImageButton BackButton, CoverImageAddButton;
    private ImageView CoverImage;
    private TextView UserName, UserAddress;
    private RecyclerView UserPosts;
    private String coverImage, userName, userAddress= "", coverImageUrl, currentuser;
    private LinearLayout progressBar;


    private DatabaseReference postRef,postRef1,userRef;
    private StorageReference coverImageRef;
    private FirebaseRecyclerOptions<Posts> options;
    private FirebaseRecyclerAdapter<Posts, PostViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        progressBar = findViewById(R.id.user_profile_progressbar);
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        coverImageRef = FirebaseStorage.getInstance().getReference().child("cover_image").child(currentuser);

        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        postRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentuser).child("posts");
        postRef.keepSynced(true);
        postRef1 = FirebaseDatabase.getInstance().getReference().child("users_post");
        ProfileImage = findViewById(R.id.user_profile_image);
        BackButton = findViewById(R.id.user_profile_back_button);
        ProfileImageAddButton = findViewById(R.id.user_profile_add_profile_image);
        CoverImageAddButton = findViewById(R.id.user_profile_add_cover_image);
        CoverImage = findViewById(R.id.user_cover_photo);
        UserName = findViewById(R.id.user_profile_username);
        UserAddress = findViewById(R.id.user_profile_address);
        UserPosts = findViewById(R.id.user_profile_posts);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserProfileActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        UserPosts.setLayoutManager(linearLayoutManager);
        UserPosts.setHasFixedSize(true);
        options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postRef,Posts.class).build();

        adapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostViewHolder holder, final int position, @NonNull final Posts model)
            {
                final String key = this.getRef(position).getKey();
                final String UId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final int Tlike = model.getTotalLikes();
                final String likes = Tlike+" person interested";
                String timezone = model.getTimeZone();

                holder.LikeCount.setText(likes);
                holder.Chat.setVisibility(View.GONE);
                holder.RequestMessage.setVisibility(View.GONE);
                holder.OwnPost.setVisibility(View.VISIBLE);

                if (model.getProfileImage()!=null)
                {
                    Picasso.get().load(model.getProfileImage()).into(holder.InpostProfileImage);
                }

                if (model.getPostImage()!=null)
                {
                    holder.PostImageHolder.setVisibility(View.VISIBLE);
                    Picasso.get().load(model.getPostImage()).into(holder.PostImage);
                    //Picasso.get().load(model.getProfileImage()).fit().into(holder.PostImage);
                    //Picasso.get().load(model.getProfileImage()).centerCrop().into(holder.PostImage);
                }

                postRef.child(key).child("likes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            if (dataSnapshot.hasChild(UId))
                            {
                                holder.LikeClicked.setVisibility(View.VISIBLE);
                                holder.Like.setVisibility(View.GONE);
                            }
                            else
                            {
                                holder.Like.setVisibility(View.VISIBLE);
                                holder.LikeClicked.setVisibility(View.GONE);
                            }
                        }else
                        {
                            holder.Like.setVisibility(View.VISIBLE);
                            holder.LikeClicked.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                if (model.getTravelDate()!=null && model.getTravelLocation()!=null)
                {
                    String s = model.getTravelDateAndLocation()+model.getTravelLocation()+" at "+model.getTravelDate();
                    String tdate = model.getTravelDate();
                    holder.TravelDate.setText(s);
                    GetTimeDifference.CalculateTime(tdate, timezone, holder.TravelDayCount,2);

                }
                if (model.getTravelPlan()!=null)
                {
                    holder.TravelPlan.setText(model.getTravelPlan());
                }
                if (model.getFullName()!=null)
                {
                    holder.FullName.setText(model.getFullName());

                }
                if (model.getCurrentDateAndTime()!=null)
                {
                    GetTimeDifference.CalculateTime(model.getCurrentDateAndTime(), timezone,holder.CurrentDateAndTime,1);

                    //holder.CurrentDateAndTime.setText(s);
                }



                holder.PostImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(UserProfileActivity.this, ViewFullImageActivity.class);
                        intent.putExtra("imageLink", model.getPostImage());
                        intent.putExtra("position", position);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                });


                holder.Comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(UserProfileActivity.this, SinglePostActivity.class);
                        intent.putExtra("publisher",currentuser);
                        intent.putExtra("post_key",key);
                        intent.putExtra("publishername",model.getFullName());
                        intent.putExtra("profileimage",model.getProfileImage());
                        intent.putExtra("intro",model.getTravelDateAndLocation()+model.getTravelLocation()+" at "+model.getTravelDate());
                        intent.putExtra("plan",model.getTravelPlan());
                        intent.putExtra("totallikes",likes);
                        intent.putExtra("posttime",model.getCurrentDateAndTime());
                        intent.putExtra("timezone",model.getTimeZone());
                        intent.putExtra("position",position);
                        if (model.getPostImage()!=null)
                            intent.putExtra("postimage",model.getPostImage());
                        else
                            intent.putExtra("postimage","null");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });

                holder.Like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int totlikes= Tlike;
                        ++totlikes;
                        holder.LikeClicked.setEnabled(false);
                        postRef.child(key)
                                .child("totalLikes")
                                .setValue(totlikes)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                        postRef1.child(key)
                                .child("totalLikes")
                                .setValue(totlikes)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                        postRef.child(key)
                                .child("likes")
                                .child(UId)
                                .setValue(true)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                        postRef1.child(key)
                                .child("likes")
                                .child(UId)
                                .setValue(true)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        holder.LikeClicked.setEnabled(true);
                                    }
                                });
                        //holder.Like.setVisibility(View.GONE);
                        //holder.LikeClicked.setVisibility(View.VISIBLE);
                        //postRef1.child(key).child("totalLikes").setValue(totlikes);
                        //postRef.child(key).child("totalLikes").setValue(totlikes);
                        //postRef1.child(key).child("likes").child(UId).setValue(true);
                        //postRef.child(key).child("likes").child(UId).setValue(true);
                        holder.LikeCount.setText(totlikes+" person interested");
                    }
                });
                holder.LikeClicked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int totlikes= Tlike;
                        --totlikes;
                        holder.Like.setEnabled(false);
                        postRef.child(key)
                                .child("totalLikes")
                                .setValue(totlikes)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        postRef1.child(key)
                                .child("totalLikes")
                                .setValue(totlikes)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                        postRef.child(key)
                                .child("likes")
                                .child(UId)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                        postRef1.child(key)
                                .child("likes")
                                .child(UId)
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        holder.Like.setEnabled(true);
                                    }
                                });
                        //holder.LikeClicked.setVisibility(View.GONE);
                        //holder.Like.setVisibility(View.VISIBLE);
                        //postRef1.child(key).child("totalLikes").setValue(totlikes);
                        //postRef.child(key).child("totalLikes").setValue(totlikes);
                        //postRef1.child(key).child("likes").child(UId).removeValue();
                        //postRef.child(key).child("likes").child(UId).removeValue();
                        holder.LikeCount.setText(totlikes+" person interested");
                    }
                });



                }


            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PostViewHolder(LayoutInflater.from(UserProfileActivity.this).inflate(R.layout.user_post_style,parent,false));
            }
        };

        UserPosts.setAdapter(adapter);
        UserPosts.getItemAnimator().setChangeDuration(0);
        adapter.startListening();

        userRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    progressBar.setVisibility(View.VISIBLE);

                    if (dataSnapshot.hasChild("profileImage"))
                    {
                        String profile = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.get().load(profile).into(ProfileImage);
                    }
                    if (dataSnapshot.hasChild("fullName"))
                    {
                        userName = dataSnapshot.child("fullName").getValue().toString();
                        UserName.setText(userName);
                    }
                    if (dataSnapshot.hasChild("coverImage"))
                    {
                        coverImage = dataSnapshot.child("coverImage").getValue().toString();
                        Picasso.get().load(coverImage).fit().into(CoverImage);
                    }
                    if (dataSnapshot.hasChild("address"))
                    {
                        if (dataSnapshot.hasChild("country"))
                        {
                            userAddress = dataSnapshot.child("address").getValue().toString()+dataSnapshot.child("country").getValue().toString();
                            UserAddress.setText(userAddress);
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoBack();
            }
        });

        CoverImageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_BACK);
            }
        });
    }

    private void ShowCountDown(@org.jetbrains.annotations.NotNull TextView travelDayCount)
    {
        travelDayCount.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_BACK)
        {
            if (resultCode == RESULT_OK)
            {

                Uri ImageUri = data.getData();
                UUID.randomUUID().toString();
                coverImageRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        coverImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                coverImageUrl = uri.toString();
                                Picasso.get().load(coverImageUrl).fit().into(CoverImage);
                                userRef.child(FirebaseAuth.getInstance().getUid()).child("coverImage").setValue(coverImageUrl);
                                progressBar.setVisibility(View.GONE);

                            }
                        });
                    }
                });
            }
            else
            {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }
        else
        {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GoBack();
    }

    private void GoBack()
    {
        Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
