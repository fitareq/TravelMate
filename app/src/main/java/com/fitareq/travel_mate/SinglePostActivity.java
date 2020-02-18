package com.fitareq.travel_mate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class SinglePostActivity extends AppCompatActivity {

    private Button Like, Liked, Comment, Message, MessageOwn, MessageRequest, PostComment;
    private TextView UserName, PostTime, TravelingDate, TextBeforeTravelPlan, TravelPlan, TotalLikes, TotalComments;
    private EditText UserComment;
    private RecyclerView AllCommentView;
    private CircleImageView UserProfileImage;
    private String publisher, post_key,profileimage,profilename, currentuser, commentid,publishername;
    private ImageView PostImage;
    private LinearLayout PostImageHolder;
    private int position, Tlike, Tcomment=0;
    private FirebaseRecyclerOptions<Comments> options;
    private FirebaseRecyclerAdapter<Comments, CommentHolder> adapter;
    private DatabaseReference commentRef, commentRef1, commentRef2, postRef, likeStatusRef, commentStatusRef, userRef;
    Toolbar toolbar;


    @Override
    protected void onStart() {
        super.onStart();
        //adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
       // adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);
        toolbar = findViewById(R.id.single_post_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        publisher = intent.getStringExtra("publisher");
        post_key = intent.getStringExtra("post_key");
        publishername = intent.getStringExtra("publishername");

        commentRef = FirebaseDatabase.getInstance().getReference();
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postRef = FirebaseDatabase.getInstance().getReference().child("users_post").child(post_key);
        likeStatusRef = FirebaseDatabase.getInstance().getReference().child("users_post_likes_status").child(post_key);
        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentuser);


        Like = findViewById(R.id.single_post_like);
        Liked = findViewById(R.id.single_post_like_clicked);
        Comment = findViewById(R.id.single_post_comment);
        Message = findViewById(R.id.single_post_share);
        MessageOwn = findViewById(R.id.single_post_own);
        MessageRequest = findViewById(R.id.single_post_chat);
        PostComment = findViewById(R.id.post_comment);
        UserName = findViewById(R.id.single_post_username);
        PostTime = findViewById(R.id.single_post_time);
        TravelingDate = findViewById(R.id.single_post_traveling_date);
        TextBeforeTravelPlan = findViewById(R.id.single_post_textview_before_travel_plan);
        TravelPlan = findViewById(R.id.single_post_travel_plan);
        TotalLikes = findViewById(R.id.single_post_like_count);
        TotalComments = findViewById(R.id.single_post_comment_count);
        UserComment = findViewById(R.id.add_comment);
        AllCommentView = findViewById(R.id.comment_list);
        UserProfileImage = findViewById(R.id.single_post_profile_image);
        PostImage = findViewById(R.id.single_post_image);
        PostImageHolder = findViewById(R.id.singl_post_image_holder);


        commentRef1 = commentRef.child("users_post").child(post_key).child("comment");
        commentRef2 = commentRef.child("users").child(publisher).child("posts").child(post_key).child("comment");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SinglePostActivity.this);
        linearLayoutManager.setReverseLayout(true);
        AllCommentView.setLayoutManager(linearLayoutManager);
        AllCommentView.setHasFixedSize(true);


        options = new FirebaseRecyclerOptions.Builder<Comments>().setQuery(commentRef1, Comments.class).build();
        adapter = new FirebaseRecyclerAdapter<Comments, CommentHolder>(options) {

            @NonNull
            @Override
            public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new CommentHolder(LayoutInflater.from(SinglePostActivity.this).inflate(R.layout.comment_style, parent, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull CommentHolder holder, int position, @NonNull Comments model) {
                /*commentRef.child("users").child(publisher).child("posts").child(post_key).child("totalComment").setValue(adapter.getItemCount());
                commentRef.child("users_post").child(post_key).child("totalComment").setValue(adapter.getItemCount());
                String s = adapter.getItemCount() + " comments";
                TotalComments.setText(s);*/
                String profile_image = model.getProfileImage();
                String user_name = model.getFullName();
                String user_comment = model.getComment();

                Picasso.get().load(profile_image).into(holder.ProfileImage);
                holder.FullName.setText(user_name);
                holder.Comment.setText(user_comment);
            }
        };


        AllCommentView.setAdapter(adapter);
        AllCommentView.getItemAnimator().setChangeDuration(0);
        adapter.startListening();



        commentRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild(currentuser))
                    {
                        profilename = dataSnapshot.child(currentuser).child("fullName").getValue().toString();
                        profileimage = dataSnapshot.child(currentuser).child("profileImage").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        postRef.addValueEventListener(listener);
        LikeStatusCheck();


        Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Liked.setVisibility(View.VISIBLE);
                Like.setVisibility(View.GONE);
                Liked.setEnabled(false);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("notification_style");
                String id = ref1.child(publisher).push().getKey();
                ref1.child(publisher).child(id).child("users").setValue("an user likes your post");
                ref1.child(publisher).child(id).child("usersid").setValue(currentuser);
                ref1.child(publisher).child(id).child("post_key").setValue(post_key);
                ++Tlike;
                String like = Tlike + " people interested";
                TotalLikes.setText(like);
                likeStatusRef.child(currentuser).setValue(true);
                reference.child("users").child(currentuser).child("posts").child(post_key).child("totalLikes").setValue(Tlike);
                reference.child("users_post").child(post_key).child("totalLikes").setValue(Tlike).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Liked.setEnabled(true);
                    }
                });
            }
        });
        Liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Liked.setVisibility(View.GONE);
                Like.setVisibility(View.VISIBLE);
                Like.setEnabled(false);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                --Tlike;
                String like = Tlike + " people interested";
                TotalLikes.setText(like);
                likeStatusRef.child(currentuser).removeValue();
                reference.child("users").child(currentuser).child("posts").child(post_key).child("totalLikes").setValue(Tlike);
                reference.child("users_post").child(post_key).child("totalLikes").setValue(Tlike).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Like.setEnabled(true);
                    }
                });
            }

        });


        PostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String com = UserComment.getText().toString();
                PostComment.setEnabled(false);
                if (!TextUtils.isEmpty(com)) {
                    UserComment.setHint("comment");
                    UserComment.clearFocus();
                    UserComment.setText("");
                    ++Tcomment;
                    final Comments uComments = new Comments();
                   String comment = Tcomment+" comments";
                    TotalComments.setText(comment);

                    uComments.setFullName(profilename);
                    uComments.setProfileImage(profileimage);
                    uComments.setComment(com);
                    String id = commentRef1.push().getKey();
                    commentRef1.child(id).setValue(uComments);

                    commentRef.child("users").child(publisher).child("posts").child(post_key).child("totalComment").setValue(Tcomment);
                    commentRef.child("users_post").child(post_key).child("totalComment").setValue(Tcomment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            PostComment.setEnabled(true);
                        }
                    });
                }
            }
        });









    /*ValueEventListener listenerLike = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
        {
            int like;
            Posts post = dataSnapshot.getValue(Posts.class);
            like = post.TotalLikes;
            ++like;


            userRef.child(publisher).child(post_key).child("totalLikes").setValue(like);
            postRef.child("totalLikes").setValue(like).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    LikeStatusCheck();
                    Liked.setEnabled(true);
                }
            });

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };*/


   /* ValueEventListener listenerLikeremove = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
        {
            int like;
            Posts post = dataSnapshot.getValue(Posts.class);
            like = post.TotalLikes;
            --like;


            userRef.child(publisher).child(post_key).child("totalLikes").setValue(like);
            postRef.child("totalLikes").setValue(like).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    LikeStatusCheck();
                    Liked.setEnabled(true);
                }
            });

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };*/

    }

    ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
        {
            /*Like Liked Comment Message MessageOwn MessageRequest
            PostComment UserName
            PostTime TravelingDate
            TextBeforeTravelPlan TravelPlan
            TotalLikes
            UserComment AllCommentView
            UserProfileImage PostImage PostImageHolder;*/
            String like,comment=" comments";
            Posts post = dataSnapshot.getValue(Posts.class);

            Tlike = post.TotalLikes;
            Tcomment = post.TotalComment;

            like = post.TotalLikes +" people interested";
            comment = post.TotalComment+comment;
            String tm = post.TravelDateAndLocation+post.TravelLocation+" at "+post.TravelDate;

            String postimage = post.PostImage;
            String tplan = post.TravelPlan;
            String fullname = post.FullName;
            GetTimeDifference.CalculateTime(post.CurrentDateAndTime,post.TimeZone, PostTime,1);

            Picasso.get().load(post.ProfileImage).into(UserProfileImage);
            if (!TextUtils.isEmpty(postimage))
            {
                Picasso.get().load(postimage).into(PostImage);
                PostImageHolder.setVisibility(View.VISIBLE);
            }

            TravelingDate.setText(tm);
            TotalLikes.setText(like);
            TotalComments.setText(comment);
            TravelPlan.setText(tplan);
            UserName.setText(fullname);
            if (currentuser.equals(publisher))
            {
                MessageOwn.setVisibility(View.VISIBLE);
                MessageRequest.setVisibility(View.GONE);
                Message.setVisibility(View.GONE);
            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };











    private void LikeStatusCheck()
    {
        likeStatusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild(currentuser))
                    {
                        Like.setVisibility(View.GONE);
                        Liked.setVisibility(View.VISIBLE);
                    }else
                    {
                        Like.setVisibility(View.VISIBLE);
                        Liked.setVisibility(View.GONE);
                    }
                }else
                {
                    Like.setVisibility(View.VISIBLE);
                    Liked.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SinglePostActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
