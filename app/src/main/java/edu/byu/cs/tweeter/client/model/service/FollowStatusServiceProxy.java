package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.FollowStatusService;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;

public class FollowStatusServiceProxy implements FollowStatusService {

    static final String URL_PATH = "/changefollowing";
    @Override
    public FollowStatusResponse changeFollowing(FollowStatusRequest request) throws IOException, TweeterRemoteException {
        FollowStatusResponse response = getServerFacade().changeFollowing(request, URL_PATH);

        return response;
    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
