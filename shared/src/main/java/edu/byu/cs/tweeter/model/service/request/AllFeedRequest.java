package edu.byu.cs.tweeter.model.service.request;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class AllFeedRequest {
    private List<User> followers;
    private Status status;
    private boolean check;

    public AllFeedRequest(){}

    public AllFeedRequest(List<User> followers, Status status, boolean check){
        this.followers = followers;
        this.status = status;
        this.check = check;
    }

    public List<User> getFollowers() {
        return followers;
    }

    public Status getStatus() {
        return status;
    }

    public void setFollowers(List<User> followers) {
        this.followers = followers;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
