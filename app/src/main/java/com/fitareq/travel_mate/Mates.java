package com.fitareq.travel_mate;

public class Mates
{
    String Status,Mate;

    public Mates(String status, String mate) {
        Status = status;
        Mate = mate;
    }

    public Mates() {
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMate() {
        return Mate;
    }

    public void setMate(String mate) {
        Mate = mate;
    }
}
