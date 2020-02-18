package com.fitareq.travel_mate;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationViewHolder extends RecyclerView.ViewHolder
{
    public TextView notify;


    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        notify = itemView.findViewById(R.id.noti);
    }
}
