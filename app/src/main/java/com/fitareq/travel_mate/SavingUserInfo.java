package com.fitareq.travel_mate;

public class SavingUserInfo
{
    String FullName, Address, DateOfBirth, UserIdentity, Gender, Country, ProfileImage, UserPhone, UserEmail;

    public SavingUserInfo() {
    }

    public SavingUserInfo(String fullName, String address, String dateOfBirth, String userIdentity, String gender, String country, String profileImage, String userPhone, String userEmail) {
        FullName = fullName;
        Address = address;
        DateOfBirth = dateOfBirth;
        UserIdentity = userIdentity;
        Gender = gender;
        Country = country;
        ProfileImage = profileImage;
        UserPhone = userPhone;
        UserEmail = userEmail;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public String getFullName() {
        return FullName;
    }

    public String getAddress() {
        return Address;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public String getUserIdentity() {
        return UserIdentity;
    }

    public String getGender() {
        return Gender;
    }

    public String getCountry() {
        return Country;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public String getUserPhone() {
        return UserPhone;
    }
}
