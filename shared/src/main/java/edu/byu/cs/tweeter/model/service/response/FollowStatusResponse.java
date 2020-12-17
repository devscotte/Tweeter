package edu.byu.cs.tweeter.model.service.response;


public class FollowStatusResponse extends Response{

    private boolean newFollowStatus;

    public FollowStatusResponse(boolean newFollowStatus){
        super(true, null);
        this.newFollowStatus = newFollowStatus;
    }

    public FollowStatusResponse(String message){
        super(false, message);
    }

    public boolean isNewFollowStatus() {
        return newFollowStatus;
    }
}
