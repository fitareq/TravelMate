package com.fitareq.travel_mate;

public class Messages
{
    String SenderName,SenderMessages;

    public Messages()
    {
    }

    public Messages(String senderName, String senderMessages) {
        SenderName = senderName;
        SenderMessages = senderMessages;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }



    public String getSenderMessages() {
        return SenderMessages;
    }

    public void setSenderMessages(String senderMessages) {
        SenderMessages = senderMessages;
    }

}
