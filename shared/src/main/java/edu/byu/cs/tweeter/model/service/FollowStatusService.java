package edu.byu.cs.tweeter.model.service;

import java.io.IOError;
import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;

public interface FollowStatusService {
    FollowStatusResponse changeFollowing(FollowStatusRequest request) throws IOException, TweeterRemoteException;
}
