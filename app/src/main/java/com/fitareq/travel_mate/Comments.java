package com.fitareq.travel_mate;

public class Comments
{
    String ProfileImage, FullName, Comment;

    public Comments() {
    }

    public Comments(String profileImage, String fullName, String comment) {
        ProfileImage = profileImage;
        FullName = fullName;
        Comment = comment;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }
}
