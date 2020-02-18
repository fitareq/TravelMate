package com.fitareq.travel_mate;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class ActiveStatus
{
    private String CURRENT_USER;

    private Timer timer;
    private TimerTask hourlyTask;
    private DatabaseReference reference;
    private Boolean active_status = false;


    ActiveStatus()
    {
        CURRENT_USER = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("users_active_status");
        timer = new Timer();


        hourlyTask = new TimerTask() {
            @Override
            public void run ()
            {
                reference.child(CURRENT_USER).child("status").setValue("active");

            }
        };
    }
    Boolean statusCheck()
    {
        reference.child(CURRENT_USER).child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    String s = dataSnapshot.getValue().toString();
                    active_status = s.equals("active");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return active_status;
    }

    void setActive()
    {
        timer.schedule(hourlyTask,01, 1000*60);
    }

    void setInactive()
    {
            timer.cancel();
            reference.child(CURRENT_USER).child("status").setValue("inactive");

    }
}
