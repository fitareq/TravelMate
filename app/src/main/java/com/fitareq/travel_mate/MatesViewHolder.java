package com.fitareq.travel_mate;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatesViewHolder extends RecyclerView.ViewHolder{


    public CircleImageView circleImageView;
    public TextView textView, lastMessage;
    public ImageView activeState, inactiveState, acceptRequest, rejectRequest;
    public RelativeLayout acceptOrRejectButtonHolder;
    public MatesViewHolder(@NonNull View itemView) {
        super(itemView);

        circleImageView = itemView.findViewById(R.id.mateimage);
        textView = itemView.findViewById(R.id.matename);
        activeState = itemView.findViewById(R.id.active_state);
        inactiveState = itemView.findViewById(R.id.inactive_state);
        lastMessage = itemView.findViewById(R.id.last_text);
        acceptRequest = itemView.findViewById(R.id.accept_mate_request);
        rejectRequest = itemView.findViewById(R.id.delete_mate_request);
        acceptOrRejectButtonHolder = itemView.findViewById(R.id.accept_reject_button_holder);

    }
}
