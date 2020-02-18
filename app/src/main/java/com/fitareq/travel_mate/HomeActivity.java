package com.fitareq.travel_mate;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements LifecycleObserver {

    BottomNavigationView bottomNavigationView;
    DatabaseReference UserRef;
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private DatabaseReference postRef,userRef, likeRef, mateRef;
    private FirebaseRecyclerOptions<Posts> options;
    private FirebaseRecyclerAdapter<Posts, PostViewHolder> adapter;
    private CircleImageView circleImageView;
    private ImageView gotoChat, chatBadge;
    private ImageView HomepageSearch;
    private ProgressBar progressBar;
    String currentusername;
    int count =0;
    AsyncTask<?,?,?> runningTask;
    ActiveStatus updateActiveStatus;
    Dialog dialog;


    @Override
    protected void onStart() {
        super.onStart();


        /*if (!updateActiveStatus.statusCheck())
        {
            Toast.makeText(this, "Changing to active", Toast.LENGTH_SHORT).show();
            updateActiveStatus.setActive();
        }*/

        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser == null)
        {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else
        {
            CheckUserExistence();
        }


        adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*if (updateActiveStatus.statusCheck())
        {
            Toast.makeText(HomeActivity.this, "Changing to inactive", Toast.LENGTH_SHORT).show();
            updateActiveStatus.setInactive();
        }*/
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        /*if (updateActiveStatus.statusCheck())
        {
            Toast.makeText(HomeActivity.this, "Changing to inactive", Toast.LENGTH_SHORT).show();
            updateActiveStatus.setInactive();
        }*/

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        updateActiveStatus = new ActiveStatus();
        dialog = new Dialog(HomeActivity.this);
        runningTask = new internetCheck();




        circleImageView = findViewById(R.id.home_page_profile_image);
        progressBar = findViewById(R.id.home_progressbar);

        postRef = FirebaseDatabase.getInstance().getReference().child("users_post");
        postRef.keepSynced(true);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        likeRef = FirebaseDatabase.getInstance().getReference().child("users_post_likes_status");
        mateRef = FirebaseDatabase.getInstance().getReference().child("mates_list");


        toolbar=findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Travel Feed");

        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        recyclerView = findViewById(R.id.recycler_view);
        HomepageSearch = findViewById(R.id.home_user_search);
        gotoChat = findViewById(R.id.goto_chat);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postRef,Posts.class).build();

        DisplayPosts();








        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("profileImage"))
                    {
                        String profileimg = dataSnapshot.child("profileImage").getValue().toString();
                        Picasso.get().load(profileimg).into(circleImageView);
                        currentusername = dataSnapshot.child("fullName").getValue().toString();

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        bottomNavigationView = findViewById(R.id.navigation_home);
        bottomNavigationView.setSelectedItemId(R.id.nav_travelfeed);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId())
                {

                    case R.id.nav_travelfeed:
                        finish();
                        startActivity(getIntent());
                        overridePendingTransition(0,0);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case R.id.nav_add_post:
                        intent = new Intent(HomeActivity.this, AddPostActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        break;
                    case R.id.nav_notifitacion:
                        intent = new Intent(HomeActivity.this, NotificationActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.nav_drawer:
                        intent = new Intent(HomeActivity.this, SettingsActivity.class);
                        finish();
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        break;
                }

                return true;
            }
        });

        HomepageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this, UserProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        gotoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(HomeActivity.this, MatesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
    }
















    private void DisplayPosts()
    {
        adapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final PostViewHolder holder, final int position, @NonNull final Posts model)
            {
                final String key = this.getRef(position).getKey();
                final String UId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String publisher = model.getUserId();
                final String publisherimage = model.getProfileImage();
                final int Tlike = model.getTotalLikes();
                final String likes = Tlike+" person interested";
                String timezone = model.getTimeZone();
                String Tcomment = model.getTotalComment()+" comments";


                holder.LikeCount.setText(likes);
                holder.CommentCount.setText(Tcomment);

                //check if this is current users own post

                if (model.getUserId().equals(UId))
                {
                    holder.Chat.setVisibility(View.GONE);
                    holder.RequestMessage.setVisibility(View.GONE);
                    holder.OwnPost.setVisibility(View.VISIBLE);
                }

                if (model.getPostImage()!=null)
                {
                    holder.PostImageHolder.setVisibility(View.VISIBLE);
                    Picasso.get().load(model.getPostImage()).into(holder.PostImage);
                    //Picasso.get().load(model.getProfileImage()).fit().into(holder.PostImage);
                    //Picasso.get().load(model.getProfileImage()).centerCrop().into(holder.PostImage);
                }


                ChekMateStatus(publisher, UId, model.getFullName(), holder.Chat, holder.RequestMessage);
                //loading post publisher profile image
                if (model.getProfileImage()!=null)
                {
                    Picasso.get().load(model.getProfileImage()).into(holder.InpostProfileImage);
                }


                //check if current user liked any post or not and update the like or unlike button
                likeRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            if (dataSnapshot.hasChild(UId) && holder.LikeClicked.getVisibility() ==View.GONE && holder.Like.getVisibility() == View.VISIBLE)
                            {
                                holder.LikeClicked.setVisibility(View.VISIBLE);
                                holder.Like.setVisibility(View.GONE);
                            }
                            if(holder.LikeClicked.getVisibility()==View.VISIBLE && !dataSnapshot.hasChild(UId) && holder.Like.getVisibility() == View.GONE)
                            {
                                holder.Like.setVisibility(View.VISIBLE);
                                holder.LikeClicked.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            holder.Like.setVisibility(View.VISIBLE);
                            holder.LikeClicked.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                //show users traveling date and location
                if (model.getTravelDate()!=null && model.getTravelLocation()!=null)
                {
                    String s = model.getTravelDateAndLocation()+model.getTravelLocation()+" at "+model.getTravelDate();
                    String tdate = model.getTravelDate();
                    holder.TravelDate.setText(s);
                    GetTimeDifference.CalculateTime(tdate, timezone, holder.TravelDayCount,2);

                }
                //post details
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
                        Intent intent = new Intent(HomeActivity.this, ViewFullImageActivity.class);
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
                        Intent intent = new Intent(HomeActivity.this, SinglePostActivity.class);
                        intent.putExtra("publisher",publisher);
                        intent.putExtra("post_key",key);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                });

                holder.Chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                        intent.putExtra("publisher", publisher);
                        intent.putExtra("publisher_image", model.getProfileImage());
                        intent.putExtra("publisher_name", model.getFullName());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                    }
                });
                holder.RequestMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String btnText = holder.RequestMessage.getText().toString().toLowerCase();

                        if (btnText.equalsIgnoreCase("request"))
                        {
                            SendMessageRequest(1,publisher, UId, model.getFullName());
                            ChekMateStatus(publisher, UId, model.getFullName(), holder.Chat, holder.RequestMessage);
                        }
                        else if (btnText.equalsIgnoreCase("cancel"))
                        {
                            SendMessageRequest(2,publisher, UId, model.getFullName());
                            ChekMateStatus(publisher, UId, model.getFullName(), holder.Chat, holder.RequestMessage);
                        }
                        else if (btnText.equalsIgnoreCase("response"))
                        {
                            SendMessageRequest(3,publisher, UId, model.getFullName());
                            ChekMateStatus(publisher, UId, model.getFullName(), holder.Chat, holder.RequestMessage);
                        }


                    }
                });

                holder.Like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int totlikes= Tlike;
                        ++totlikes;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users_post_likes_status").child(key);
                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("notification_style");
                        //if (!model.getFullName().equals(currentusername))
                       // {
                            String id = ref1.child(publisher).push().getKey();
                            String st = currentusername+" likes your post";
                            ref1.child(publisher).child(id).child("message").setValue(st);
                            ref1.child(publisher).child(id).child("usersid").setValue(UId);
                            ref1.child(publisher).child(id).child("post_key").setValue(key);
                        //}

                        holder.LikeClicked.setEnabled(false);
                        ref.child(UId).setValue(true);
                        UserRef.child(publisher).
                                child("posts").
                                child(key).child("totalLikes")
                                .setValue(totlikes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {

                            }
                        });
                        postRef.child(key).
                                child("totalLikes").
                                setValue(totlikes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                holder.LikeClicked.setEnabled(true);
                            }
                        });
                        /*UserRef.child(publisher).
                                child("posts").
                                child(key).
                                child("likes").
                                child(UId).
                                setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });*/
                       /* postRef.child(key).
                                child("likes").
                                child(UId).
                                setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.LikeClicked.setEnabled(true);
                            }
                        });
                        CheckLikes(key, UId, 1);
                        //holder.Like.setVisibility(View.GONE);
                        //holder.LikeClicked.setVisibility(View.VISIBLE);*/
                        holder.LikeCount.setText(totlikes+" person interested");



                    }
                });
                holder.LikeClicked.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        int totlikes= Tlike;
                        --totlikes;
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users_post_likes_status").child(key);
                        holder.Like.setEnabled(false);
                        ref.child(UId).removeValue();
                        UserRef.child(publisher)
                                .child("posts")
                                .child(key)
                                .child("totalLikes")
                                .setValue(totlikes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        postRef.child(key).child("totalLikes").setValue(totlikes).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.Like.setEnabled(true);
                            }
                        });
                        /*UserRef.child(publisher).child("posts").child(key).child("likes").child(UId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        postRef.child(key).child("likes").child(UId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                holder.Like.setEnabled(true);
                            }
                        });
                        CheckLikes(key, UId, 2);*/
                        //holder.LikeClicked.setVisibility(View.GONE);
                        //holder.Like.setVisibility(View.VISIBLE);
                        holder.LikeCount.setText(totlikes+" person interested");


                    }
                });






            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PostViewHolder(LayoutInflater.from(HomeActivity.this).inflate(R.layout.user_post_style,parent,false));
            }



        };




        recyclerView.setAdapter(adapter);
        recyclerView.getItemAnimator().setChangeDuration(0);
        adapter.startListening();
    }

    private void ChekMateStatus(final String publisher, final String uId, final String fullName,
                                          final Button chat, final Button requestMessage)
    {

        if (!publisher.equals(uId))
        {
            UserRef.child(uId).child("mates").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        if (dataSnapshot.hasChild(publisher))
                        {
                            String s = dataSnapshot.child(publisher).getValue().toString();
                            if (s.equalsIgnoreCase("got request"))
                            {
                                requestMessage.setVisibility(View.VISIBLE);
                                requestMessage.setText("response");
                            }
                            else if (s.equalsIgnoreCase("request sent"))
                            {
                                requestMessage.setVisibility(View.VISIBLE);
                                requestMessage.setText("cancel");
                            }
                            else if (s.equalsIgnoreCase("TravelMate"))
                            {
                                requestMessage.setVisibility(View.GONE);
                                chat.setVisibility(View.VISIBLE);
                            }

                        }else
                        {
                            requestMessage.setVisibility(View.VISIBLE);
                            requestMessage.setText("request");
                        }
                    }else
                    {
                        requestMessage.setVisibility(View.VISIBLE);
                        requestMessage.setText("request");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }




    private void CheckUserExistence()
    {
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(userid))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setMessage("Your profile isn't updated, please update your profile.");
                    builder.setTitle("Update Profile");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Update",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(HomeActivity.this, SetupActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void SendMessageRequest(int id, final String ReqTo, final String ReqFrom, String PersonName)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        if (id == 1) {
            builder.setMessage("Send a mate request?");
            builder.setTitle("your'e not in " + PersonName + "'s mate list.");
            builder.setPositiveButton("Request",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserRef.child(ReqTo).child("mates").child(ReqFrom).setValue("got request");
                            UserRef.child(ReqFrom)  .child("mates") .child(ReqTo).setValue("request sent");
                            mateRef.child(ReqTo)    .child(ReqFrom) .child("status").setValue("got request");
                            mateRef.child(ReqFrom)  .child(ReqTo)   .child("status").setValue("request sent");
                            mateRef.child(ReqTo)    .child(ReqFrom) .child("mate").setValue(ReqFrom);
                            mateRef.child(ReqFrom)  .child(ReqTo)   .child("mate").setValue(ReqTo);
                            Toast.makeText(HomeActivity.this, "Your request has been sent...", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        else if (id == 2)
        {
            builder.setTitle("You will not be able to chat with "+PersonName);
            builder.setMessage("Cancel request?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserRef.child(ReqTo).child("mates").child(ReqFrom).removeValue();
                            UserRef.child(ReqFrom).child("mates").child(ReqTo).removeValue();
                            mateRef.child(ReqTo)  .child(ReqFrom) .child("status").removeValue();
                            mateRef.child(ReqFrom).child(ReqTo)   .child("status").removeValue();
                            mateRef.child(ReqTo)  .child(ReqFrom) .child("mate").removeValue();
                            mateRef.child(ReqFrom).child(ReqTo)   .child("mate").removeValue();
                            dialog.dismiss();
                        }
                    });
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        }
        else if (id == 3)
        {
            builder.setTitle(PersonName+" will be able to chat with you");
            builder.setMessage("Response request?");
            builder.setPositiveButton("Accept",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserRef.child(ReqTo).child("mates").child(ReqFrom).setValue("TravelMate");
                            UserRef.child(ReqFrom).child("mates").child(ReqTo).setValue("TravelMate");
                            mateRef.child(ReqTo)  .child(ReqFrom) .child("status").setValue("TravelMate");
                            mateRef.child(ReqFrom).child(ReqTo)   .child("status").setValue("TravelMate");
                            mateRef.child(ReqTo)  .child(ReqFrom) .child("mate").setValue(ReqFrom);
                            mateRef.child(ReqFrom).child(ReqTo)   .child("mate").setValue(ReqTo);
                            dialog.dismiss();
                        }
                    });
            builder.setNegativeButton("Delete",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserRef.child(ReqTo).child("mates").child(ReqFrom).removeValue();
                            UserRef.child(ReqFrom).child("mates").child(ReqTo).removeValue();
                            mateRef.child(ReqTo)  .child(ReqFrom) .child("status").removeValue();
                            mateRef.child(ReqFrom).child(ReqTo)   .child("status").removeValue();
                            mateRef.child(ReqTo)  .child(ReqFrom) .child("mate").removeValue();
                            mateRef.child(ReqFrom).child(ReqTo)   .child("mate").removeValue();
                            dialog.dismiss();
                        }
                    });
        }

        AlertDialog alert = builder.create();
        alert.show();
    }
















        protected final class internetCheck extends AsyncTask<Void, Void, String> {

            @Override
            protected String doInBackground(Void... voids) {
                ConnectivityManager connec =
                        (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);


                if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                        connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
                    return "true";
                } else if (
                        connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
                    return "true";
                } else {
                    return "false";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                if (s.equals("false")) {
                    findViewById(R.id.main_screen).setVisibility(View.GONE);
                    Toast.makeText(HomeActivity.this, "internet is not on", Toast.LENGTH_SHORT).show();
                }
            }

        }


    @Override
    public void onBackPressed() {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Do you really want to exit?");
        builder.setPositiveButtonIcon(getResources().getDrawable(R.drawable.icon_accept_green));
        builder.setNegativeButtonIcon(getResources().getDrawable(R.drawable.icon_delete_red));
        builder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateActiveStatus.setInactive();
                        finish();
                        dialog.dismiss();
                    }
                });

        builder.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/
        //super.onBackPressed();
        ImageView btnYes, btnNo;
        dialog.setContentView(R.layout.exit_popup);
        btnYes = dialog.findViewById(R.id.dialog_yes);
        btnNo = dialog.findViewById(R.id.dialog_no);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (updateActiveStatus.statusCheck())
                {
                    Toast.makeText(HomeActivity.this, "Changing to inactive", Toast.LENGTH_SHORT).show();
                    updateActiveStatus.setInactive();
                }*/
                finish();
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
