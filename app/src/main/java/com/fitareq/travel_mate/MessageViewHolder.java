package com.fitareq.travel_mate;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageViewHolder extends RecyclerView.ViewHolder
{
    public TextView SenderMessages, ReceiverMessages;
    public CircleImageView SenderImage;

    public MessageViewHolder(@NonNull View itemView)
    {
        super(itemView);


        SenderImage = itemView.findViewById(R.id.sender_image);
        SenderMessages = itemView.findViewById(R.id.sender_text);
        ReceiverMessages = itemView.findViewById(R.id.receiver_text);
    }
}
