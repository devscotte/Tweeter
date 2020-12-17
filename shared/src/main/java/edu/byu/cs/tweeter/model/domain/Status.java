package edu.byu.cs.tweeter.model.domain;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class Status implements Comparable<Status>{

    private String text;
    private long timeStamp;
    private User author;

    public Status(){

    }

    public Status(@NotNull String text, @NotNull User author){
        this.text = text;
        this.author = author;
        this.timeStamp = System.currentTimeMillis();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public Date getDate(){
        return new Date(timeStamp);
    }

    public String getText() {
        return text;
    }

    public User getAuthor() {
        return author;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setUser(User author) {
        this.author = author;
    }


    @Override
    public int compareTo(Status status) {
        int compare = Long.compare(timeStamp, status.getTimeStamp());
        return compare;
    }


}
