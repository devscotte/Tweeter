package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.LoginService;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

/**
 * Contains the business logic to support the login operation.
 */
public class LoginServiceProxy implements LoginService {

    static final String URL_PATH = "/login";
    static final String LOGGED_URL = "/loginuser";
    static final String NUM_FOLLOWERS = "/loginfollowers";
    static final String NUM_FOLLOWEES = "/loginfollowees";

    @Override
    public LoginResponse login(LoginRequest request) throws IOException, TweeterRemoteException {
        ServerFacade serverFacade = getServerFacade();
        LoginResponse loginResponse = serverFacade.login(request, URL_PATH);

        if(loginResponse.isSuccess()) {
            loadImage(loginResponse.getUser());
        }

        return loginResponse;
    }

    /**
     * Loads the profile image data for the user.
     *
     * @param user the user whose profile image data is to be loaded.
     */
    private void loadImage(User user) throws IOException {
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their ServerFacade instance from this
     * method to allow for proper mocking.
     *
     * @return the instance.
     */
    ServerFacade getServerFacade() {
        return new ServerFacade();
    }

    public User getLoggedInUser(){return getServerFacade().getLoggedInUser();}

    public int getFollowersUser(){return getServerFacade().getFollowersUser();}

    public int getFolloweesUser(){return getServerFacade().getFolloweesUser();}

    public int getFollowersClick(){return getServerFacade().getFollowersClick();}

    public int getFolloweesClick(){return getServerFacade().getFolloweesClick();}
}
