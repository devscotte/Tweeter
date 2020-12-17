package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryRequest {

    private User author;
    private int limit;
    private Status lastStatus;

    public StoryRequest(){}

    public StoryRequest(User author, int limit, Status lastStatus){
        this.author = author;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public int getLimit() {
        return limit;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public User getAuthor() {
        return author;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
