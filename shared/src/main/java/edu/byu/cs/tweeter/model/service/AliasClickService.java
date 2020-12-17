package edu.byu.cs.tweeter.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;

public interface AliasClickService {
    AliasClickResponse viewUserProfile(AliasClickRequest request) throws IOException, TweeterRemoteException;
}
