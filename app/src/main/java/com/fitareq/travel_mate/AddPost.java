package com.fitareq.travel_mate;

public class AddPost
{
     String TravelLocation, TravelDate, TravelPlan, UserId, FullName, ProfileImage, CurrentDateAndTime, TimeZone, PostImage;
     String TravelDateAndLocation = "Hi Mates, I've planned for a tour. The date and location is: ";
     int TotalLikes, TotalComments;

    public AddPost()
    {

    }

    public AddPost(String postImage) {
        PostImage = postImage;
    }

    public AddPost(String travelLocation, String travelDate, String travelPlan, String userId, String fullName, String profileImage, String currentDateAndTime, int totalLikes,int totalComments, String timeZone) {
        TravelLocation = travelLocation;
        TravelDate = travelDate;
        TravelPlan = travelPlan;
        UserId = userId;
        FullName = fullName;
        ProfileImage = profileImage;
        CurrentDateAndTime = currentDateAndTime;
        TotalLikes = totalLikes;
        TimeZone = timeZone;
        TotalComments = totalComments;
    }

    public void setTotalComments(int totalComments) {
        TotalComments = totalComments;
    }

    public void setTravelLocation(String travelLocation) {
        TravelLocation = travelLocation;
    }

    public void setTravelDate(String travelDate) {
        TravelDate = travelDate;
    }

    public void setTravelPlan(String travelPlan) {
        TravelPlan = travelPlan;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public void setCurrentDateAndTime(String currentDateAndTime) {
        CurrentDateAndTime = currentDateAndTime;
    }

    public void setTimeZone(String timeZone) {
        TimeZone = timeZone;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public void setTravelDateAndLocation(String travelDateAndLocation) {
        TravelDateAndLocation = travelDateAndLocation;
    }

    public void setTotalLikes(int totalLikes) {
        TotalLikes = totalLikes;
    }

    public String getPostImage() {
        return PostImage;
    }

    public String getTimeZone() {
        return TimeZone;
    }

    public String getTravelDateAndLocation() {
        return TravelDateAndLocation;
    }

    public int getTotalLikes() {
        return TotalLikes;
    }

    public String getCurrentDateAndTime() {
        return CurrentDateAndTime;
    }

    public String getTravelLocation() {
        return TravelLocation;
    }

    public String getTravelDate() {
        return TravelDate;
    }

    public String getTravelPlan() {
        return TravelPlan;
    }

    public String getUserId() {
        return UserId;
    }

    public String getFullName() { return FullName; }

    public String getProfileImage() { return ProfileImage; }

    public int getTotalComments() {
        return TotalComments;
    }
}
