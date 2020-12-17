package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.TweetService;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

public class TweetServiceProxy implements TweetService {

    static final String URL_PATH = "/tweet";
    @Override
    public TweetResponse tweet(TweetRequest request) throws IOException, TweeterRemoteException {
        TweetResponse tweetResponse = getServerFacade().tweet(request, URL_PATH);

        return tweetResponse;
    }

    private void loadImage(User user) throws IOException{
        byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
        user.setImageBytes(bytes);
    }

    ServerFacade getServerFacade(){
        return new ServerFacade();
    }
}
