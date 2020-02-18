package com.fitareq.travel_mate;

public class Notification
{
    String Message, Post_key, Usersid;

    public Notification(String message, String post_key, String usersid) {
        Message = message;
        Post_key = post_key;
        Usersid = usersid;
    }

    public Notification() {
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setPost_key(String post_key) {
        Post_key = post_key;
    }

    public void setUsersid(String usersid) {
        Usersid = usersid;
    }

    public String getPost_key() {
        return Post_key;
    }



    public String getUsersid() {
        return Usersid;
    }


}
