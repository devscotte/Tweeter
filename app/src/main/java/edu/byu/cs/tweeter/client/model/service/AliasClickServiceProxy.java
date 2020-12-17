package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.AliasClickService;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

public class AliasClickServiceProxy implements AliasClickService {

    static final String URL_PATH = "/viewuserprofile";

    @Override
    public AliasClickResponse viewUserProfile(AliasClickRequest request) throws IOException, TweeterRemoteException {
        AliasClickResponse aliasClickResponse = getServerFacade().viewUserProfile(request, URL_PATH);

        if(aliasClickResponse.isSuccess()){
            loadImages(aliasClickResponse);
        }

        return aliasClickResponse;
    }

    private void loadImages(AliasClickResponse response) throws IOException {

        byte [] bytes = ByteArrayUtils.bytesFromUrl(response.getAssociatedUser().getImageUrl());
        response.getAssociatedUser().setImageBytes(bytes);

    }

    ServerFacade getServerFacade() {
        return new ServerFacade();
    }
}
