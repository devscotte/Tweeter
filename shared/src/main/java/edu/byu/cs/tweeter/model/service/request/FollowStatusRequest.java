package edu.byu.cs.tweeter.model.service.request;

import edu.byu.cs.tweeter.model.domain.User;

public class FollowStatusRequest {


    private User myUser;
    private User otherUser;
    private boolean followingStatus;

    public FollowStatusRequest(){}

    public FollowStatusRequest(User myUser, User otherUser, boolean followingStatus){
        this.myUser = myUser;
        this.otherUser = otherUser;
        this.followingStatus = followingStatus;
    }

    public User getMyUser() {
        return myUser;
    }

    public User getOtherUser() {
        return otherUser;
    }

    public boolean isFollowingStatus() {
        return followingStatus;
    }

    public void setFollowingStatus(boolean followingStatus) {
        this.followingStatus = followingStatus;
    }

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }

    public void setOtherUser(User otherUser) {
        this.otherUser = otherUser;
    }
}
