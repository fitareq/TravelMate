package com.fitareq.travel_mate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class MatesActivity extends AppCompatActivity {

    RecyclerView mates;
    private FirebaseRecyclerOptions<Mates> options;
    private FirebaseRecyclerAdapter<Mates, MatesViewHolder> adapter;
    DatabaseReference mateRef, userRef, activeRef;
    String current, publisherimage, publishername;
    ActiveStatus updateActiveStatus;
    Toolbar toolbar;
    DatabaseReference UserRef,mateRefrence;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mates);

        updateActiveStatus = new ActiveStatus();

        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        mateRefrence = FirebaseDatabase.getInstance().getReference().child("mates_list");

        toolbar = findViewById(R.id.notification_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TravelMates");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        current =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        mateRef = FirebaseDatabase.getInstance().getReference().child("mates_list").child(current);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        activeRef = FirebaseDatabase.getInstance().getReference().child("users_active_status");

        mates = findViewById(R.id.mate_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MatesActivity.this);
        linearLayoutManager.setReverseLayout(true);
        mates.setLayoutManager(linearLayoutManager);
        options = new FirebaseRecyclerOptions.Builder<Mates>().setQuery(mateRef,Mates.class).build();

        adapter = new FirebaseRecyclerAdapter<Mates, MatesViewHolder>(options)
        {


            @NonNull
            @Override
            public MatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MatesViewHolder(LayoutInflater.from(MatesActivity.this).inflate(R.layout.mate_style, parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull final MatesViewHolder holder, int position, @NonNull final Mates model) {
                final String mateid = model.getMate();
                final AlertDialog.Builder builder = new AlertDialog.Builder(MatesActivity.this);
                userRef.child(mateid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            if (dataSnapshot.hasChild("fullName")&&dataSnapshot.hasChild("profileImage"))
                            {
                                publisherimage = dataSnapshot.child("profileImage").getValue().toString();
                                publishername = dataSnapshot.child("fullName").getValue().toString();
                                Picasso.get().load(publisherimage).into(holder.circleImageView);
                                holder.textView.setText(publishername);
                            }
                        }

                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                activeRef.child(mateid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            if (dataSnapshot.hasChild("status"))
                            {
                                String s = dataSnapshot.child("status").getValue().toString();
                                if (s.equals("active"))
                                {
                                    holder.activeState.setVisibility(View.VISIBLE);
                                    holder.inactiveState.setVisibility(View.GONE);
                                }
                                else
                                {
                                    holder.inactiveState.setVisibility(View.VISIBLE);
                                    holder.activeState.setVisibility(View.GONE);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                switch (model.getStatus()) {
                    case "request sent":
                        holder.textView.setEnabled(false);
                        holder.circleImageView.setEnabled(false);
                        holder.acceptOrRejectButtonHolder.setVisibility(View.VISIBLE);
                        holder.acceptRequest.setVisibility(View.GONE);
                        break;
                    case "got request":
                        holder.textView.setEnabled(false);
                        holder.circleImageView.setEnabled(false);
                        holder.acceptOrRejectButtonHolder.setVisibility(View.VISIBLE);
                        holder.acceptRequest.setVisibility(View.VISIBLE);
                        holder.rejectRequest.setVisibility(View.VISIBLE);
                        break;
                    case "TravelMate":
                        holder.textView.setEnabled(true);
                        holder.circleImageView.setEnabled(true);
                        holder.acceptOrRejectButtonHolder.setVisibility(View.GONE);
                        break;
                }

                holder.rejectRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserRef.child(mateid).child("mates").child(current).removeValue();
                        UserRef.child(current).child("mates").child(mateid).removeValue();
                        mateRefrence.child(mateid)  .child(current) .child("status").removeValue();
                        mateRefrence.child(current).child(mateid)   .child("status").removeValue();
                        mateRefrence.child(mateid)  .child(current) .child("mate").removeValue();
                        mateRefrence.child(current).child(mateid)   .child("mate").removeValue();
                    }
                });
                holder.acceptRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserRef.child(mateid).child("mates").child(current).setValue("TravelMate");
                        UserRef.child(current).child("mates").child(mateid).setValue("TravelMate");
                        mateRefrence.child(mateid)  .child(current) .child("status").setValue("TravelMate");
                        mateRefrence.child(current).child(mateid)   .child("status").setValue("TravelMate");
                        mateRefrence.child(mateid)  .child(current) .child("mate").setValue(current);
                        mateRefrence.child(current).child(mateid)   .child("mate").setValue(mateid);
                    }
                });

                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            goToChat(mateid);

                    }
                });
                holder.circleImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goToChat(mateid);
                    }
                });
            }
        };



        mates.setAdapter(adapter);
        adapter.startListening();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (!updateActiveStatus.statusCheck())
            updateActiveStatus.setActive();

    }

    @Override
    protected void onStop() {
        super.onStop();



    }

    private void goToChat(String mateid)
    {
        Intent intent = new Intent(MatesActivity.this, ChatActivity.class);
        intent.putExtra("publisher",mateid);
        intent.putExtra("publisher_image",publisherimage);
        intent.putExtra("publisher_name",publishername);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MatesActivity.this,HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
