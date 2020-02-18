package com.fitareq.travel_mate;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

public class ArchLifecycleApp extends Application implements LifecycleObserver
{
    ActiveStatus updateActiveStatus;
    @Override
    public void onCreate() {
        super.onCreate();
        updateActiveStatus = new ActiveStatus();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onAppBackgrounded()
    {
        if (updateActiveStatus.statusCheck())
        {
            Toast.makeText(ArchLifecycleApp.this, "Changing to inactive", Toast.LENGTH_SHORT).show();
            updateActiveStatus.setInactive();
        }
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onAppForegrounded() {
        if (!updateActiveStatus.statusCheck())
        {
            Toast.makeText(ArchLifecycleApp.this, "Changing to active", Toast.LENGTH_SHORT).show();
            updateActiveStatus.setActive();
        }
    }
}
