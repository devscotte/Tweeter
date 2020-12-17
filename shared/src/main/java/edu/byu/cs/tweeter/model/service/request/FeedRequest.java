package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedRequest {
    private User user;
    private int limit;
    private Status lastStatus;

    public FeedRequest(){}

    public FeedRequest(User user, int limit, Status lastStatus){
        this.user = user;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public User getUser() {
        return user;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public int getLimit() {
        return limit;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
