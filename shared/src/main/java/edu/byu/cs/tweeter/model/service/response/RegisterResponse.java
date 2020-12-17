package edu.byu.cs.tweeter.model.service.response;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterResponse extends Response {

    private User user;
    private AuthToken authToken;

    public RegisterResponse(String message){
        super(false, message);
    }

    public RegisterResponse(User user, AuthToken authToken){
        super(true, null);
        this.user = user;
        this.authToken = authToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }
}
