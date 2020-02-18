package com.fitareq.travel_mate;

public class Posts
{
    String TravelDate, TravelLocation, TravelPlan, FullName, ProfileImage, CurrentDateAndTime, UserId, TravelDateAndLocation, TimeZone, PostImage;
    int TotalLikes, TotalComment;
    Boolean Liked;

    public Posts() {
    }

    public Posts(String travelDate, String travelLocation, String travelPlan, String fullName, String profileImage, String currentDateAndTime, int totalLikes, int totalComment, Boolean liked, String userId, String travelDateAndLocation, String timeZone, String postImage) {
        TravelDate = travelDate;
        TravelLocation = travelLocation;
        TravelPlan = travelPlan;
        FullName = fullName;
        ProfileImage = profileImage;
        CurrentDateAndTime = currentDateAndTime;
        TotalLikes = totalLikes;
        Liked = liked;
        UserId = userId;
        TravelDateAndLocation = travelDateAndLocation;
        TimeZone = timeZone;
        PostImage = postImage;
        TotalComment = totalComment;
    }

    public int getTotalComment() {
        return TotalComment;
    }

    public void setTotalComment(int totalComment) {
        TotalComment = totalComment;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public String getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(String timeZone) {
        TimeZone = timeZone;
    }

    public String getTravelDateAndLocation() {
        return TravelDateAndLocation;
    }

    public void setTravelDateAndLocation(String travelDateAndLocation) {
        TravelDateAndLocation = travelDateAndLocation;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Boolean getLiked() {
        return Liked;
    }

    public void setLiked(Boolean liked) {
        Liked = liked;
    }

    public String getCurrentDateAndTime() {
        return CurrentDateAndTime;
    }

    public void setCurrentDateAndTime(String currentDateAndTime) {
        CurrentDateAndTime = currentDateAndTime;
    }

    public int getTotalLikes() {
        return TotalLikes;
    }

    public void setTotalLikes(int totalLikes) {
        TotalLikes = totalLikes;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getTravelDate() {
        return TravelDate;
    }

    public void setTravelDate(String travelDate) {
        TravelDate = travelDate;
    }

    public String getTravelLocation() {
        return TravelLocation;
    }

    public void setTravelLocation(String travelLocation) {
        TravelLocation = travelLocation;
    }

    public String getTravelPlan() {
        return TravelPlan;
    }

    public void setTravelPlan(String travelPlan) {
        TravelPlan = travelPlan;
    }
}
