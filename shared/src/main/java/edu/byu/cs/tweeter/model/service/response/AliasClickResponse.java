package edu.byu.cs.tweeter.model.service.response;

import edu.byu.cs.tweeter.model.domain.User;

public class AliasClickResponse extends Response{

    private User associatedUser;
    private boolean following;

    public AliasClickResponse(User associatedUser, boolean following){
        super(true, null);
        this.associatedUser = associatedUser;
        this.following = following;
    }

    public AliasClickResponse(String message){
        super(false, message);
    }

    public User getAssociatedUser() {
        return associatedUser;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following){
        this.following = following;
    }
}
