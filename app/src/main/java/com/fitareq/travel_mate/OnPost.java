package com.fitareq.travel_mate;

public class OnPost
{
    String TravelDate, TravelLocation, TravelPlan, FullName, ProfileImage, CurrentDateAndTime, UserId, TravelDateAndLocation, TimeZone, PostImage;

    public OnPost(String travelDate, String travelLocation, String travelPlan, String fullName, String profileImage, String currentDateAndTime, String userId, String travelDateAndLocation, String timeZone, String postImage) {
        TravelDate = travelDate;
        TravelLocation = travelLocation;
        TravelPlan = travelPlan;
        FullName = fullName;
        ProfileImage = profileImage;
        CurrentDateAndTime = currentDateAndTime;
        UserId = userId;
        TravelDateAndLocation = travelDateAndLocation;
        TimeZone = timeZone;
        PostImage = postImage;
    }

    public OnPost() {
    }
}
