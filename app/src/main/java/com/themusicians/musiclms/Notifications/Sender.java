package com.themusicians.musiclms.Notifications;

public class Sender {
    public Data notification;
    public String to;

    public Sender(Data data, String to){
        this.notification =data;
        this.to = to;
    }
}
