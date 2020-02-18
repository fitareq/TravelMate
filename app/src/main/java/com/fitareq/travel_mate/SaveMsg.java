package com.fitareq.travel_mate;

public class SaveMsg
{
    String SenderName,SenderMessages;

    public SaveMsg() {
    }

    public SaveMsg(String senderName, String senderMessages) {
        SenderName = senderName;
        SenderMessages = senderMessages;
    }

    public String getSenderName() {
        return SenderName;
    }

    public String getSenderMessages() {
        return SenderMessages;
    }
}
