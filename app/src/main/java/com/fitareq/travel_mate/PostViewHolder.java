package com.fitareq.travel_mate;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder
{

    public TextView CommentCount, TravelDate, TravelPlan, FullName, CurrentDateAndTime, LikeCount, TextBeforeTravelPlan, TravelDayCount, PostSetting;
    public CircleImageView InpostProfileImage;
    public Button Like, Comment, RequestMessage, LikeClicked, OwnPost, Chat;
    public ImageView PostImage;
    public LinearLayout PostImageHolder;

    public PostViewHolder(@NonNull final View itemView) {
        super(itemView);



        CommentCount = itemView.findViewById(R.id.in_post_comment_count);
        PostImageHolder = itemView.findViewById(R.id.post_image_holder);
        PostImage = itemView.findViewById(R.id.in_post_image);
        TravelDate = itemView.findViewById(R.id.traveling_date);
        TravelPlan = itemView.findViewById(R.id.in_post_travel_plan);
        InpostProfileImage = itemView.findViewById(R.id.in_post_profile_image);
        FullName = itemView.findViewById(R.id.in_post_username);
        CurrentDateAndTime = itemView.findViewById(R.id.in_post_time);
        Like = itemView.findViewById(R.id.in_post_like);
        LikeClicked = itemView.findViewById(R.id.in_post_like_clicked);
        Comment = itemView.findViewById(R.id.in_post_comment);
        RequestMessage = itemView.findViewById(R.id.in_post_share);
        OwnPost = itemView.findViewById(R.id.in_post_own);
        Chat = itemView.findViewById(R.id.in_post_chat);
        LikeCount = itemView.findViewById(R.id.in_post_like_count);
        TextBeforeTravelPlan = itemView.findViewById(R.id.in_post_textview_before_travel_plan);
        TravelDayCount = itemView.findViewById(R.id.in_post_travel_day_count);
        PostSetting = itemView.findViewById(R.id.in_post_more);




    }
}
