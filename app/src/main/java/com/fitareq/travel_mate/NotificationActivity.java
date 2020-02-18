package com.fitareq.travel_mate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationActivity extends AppCompatActivity {




    private FirebaseRecyclerOptions<Notification> options;
    private FirebaseRecyclerAdapter<Notification, NotificationViewHolder> adapter;
    private RecyclerView recycler;
    DatabaseReference notRef;
    Toolbar toolbar;
    String current;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        current =  FirebaseAuth.getInstance().getCurrentUser().getUid();
        notRef = FirebaseDatabase.getInstance().getReference().child("notification_style").child(current);



        recycler = findViewById(R.id.notification_view);
        toolbar = findViewById(R.id.notification_toolbar);

        notRef.keepSynced(true);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        linearLayoutManager.setReverseLayout(true);
        recycler.setLayoutManager(linearLayoutManager);
        options = new FirebaseRecyclerOptions.Builder<Notification>().setQuery(notRef,Notification.class).build();




        adapter = new FirebaseRecyclerAdapter<Notification, NotificationViewHolder>(options)
        {


            @NonNull
            @Override
            public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new NotificationViewHolder(LayoutInflater.from(NotificationActivity.this).inflate(R.layout.notification_style, parent,false));
            }

            @Override
            protected void onBindViewHolder(@NonNull NotificationViewHolder holder, int position, @NonNull Notification model) {

                holder.notify.setText(model.getMessage());
            }
        };


        recycler.setAdapter(adapter);
        adapter.startListening();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NotificationActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
