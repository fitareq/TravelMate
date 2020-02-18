package com.fitareq.travel_mate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChatActivity extends AppCompatActivity {

    //private ScrollView scrollView;
    private Toolbar toolbar;
    private DatabaseReference userRef,msgRef,msgRef1;
    private RecyclerView users;
    private String publisher,currentuser, publisherimage,publishename;
    String username= "name";
    private EditText UserText;
    private ImageView send;
    FirebaseRecyclerAdapter<Messages,MessageViewHolder> adapter;
    FirebaseRecyclerOptions<Messages> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        publisher = getIntent().getStringExtra("publisher");
        publisherimage = getIntent().getStringExtra("publisher_image");
        publishename = getIntent().getStringExtra("publisher_name");
        currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        msgRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentuser).child("chats").child(publisher);
        msgRef.keepSynced(true);
        msgRef1 = FirebaseDatabase.getInstance().getReference().child("users").child(publisher).child("chats").child(currentuser);

        UserText = findViewById(R.id.chat_msg);
        //scrollView = findViewById(R.id.scroll);
        send = findViewById(R.id.send_btn);
        users = findViewById(R.id.chat_list);
        toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(publishename);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        userRef.child(currentuser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("fullName").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        users.setLayoutManager(linearLayoutManager);
        options = new FirebaseRecyclerOptions.Builder<Messages>().setQuery(msgRef,Messages.class).build();

        adapter = new FirebaseRecyclerAdapter<Messages, MessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Messages model)
            {



                //scrollView.fullScroll(View.FOCUS_DOWN);
                //users.scrollToPosition(adapter.getItemCount());
                if (!model.getSenderName().isEmpty())
                {
                    if (model.getSenderName().equals(username))
                    {
                        holder.ReceiverMessages.setVisibility(View.VISIBLE);
                        holder.ReceiverMessages.setText(model.getSenderMessages());

                    }
                    else
                    {
                        holder.SenderImage.setVisibility(View.VISIBLE);
                        holder.SenderMessages.setVisibility(View.VISIBLE);
                        Picasso.get().load(publisherimage).into(holder.SenderImage);
                        holder.SenderMessages.setText(model.getSenderMessages());
                    }
                }
            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MessageViewHolder(LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_style,parent,false));
            }
        };

        users.setAdapter(adapter);
        adapter.startListening();
        users.smoothScrollToPosition(adapter.getItemCount());





        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String msg = UserText.getText().toString();
                //UserText.setText(msg+" @ "+username+" @ "+publisher);
                if (!msg.isEmpty())
                {
                    msgRef.push().setValue(new SaveMsg(username, msg));
                    msgRef1.push().setValue(new SaveMsg(username, msg));
                    UserText.setText("");
                    UserText.setHint("message");
                    users.smoothScrollToPosition(adapter.getItemCount()-1);
                    //UserText.requestFocus();
                    //scrollView.pageScroll(View.FOCUS_DOWN);
                }

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ChatActivity.this, MatesActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
