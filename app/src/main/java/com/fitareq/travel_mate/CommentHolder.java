package com.fitareq.travel_mate;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentHolder extends RecyclerView.ViewHolder
{
    public CircleImageView ProfileImage;
    public TextView FullName, Comment;

    public CommentHolder(@NonNull View itemView) {
        super(itemView);
        ProfileImage = itemView.findViewById(R.id.commenter_profile_image);
        FullName = itemView.findViewById(R.id.commenter_name);
        Comment = itemView.findViewById(R.id.user_comment);
    }
}
