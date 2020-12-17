package edu.byu.cs.tweeter.client.model.service;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.StoryService;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.client.util.ByteArrayUtils;

public class StoryServiceProxy implements StoryService {

    static final String URL_PATH = "/getstory";
    @Override
    public StoryResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException {
        StoryResponse response = getServerFacade().getStory(request, URL_PATH);

        if(response.isSuccess()){
            loadImages(response);
        }

        return response;
    }

    private void loadImages(StoryResponse response)throws IOException{
        for(Status status : response.getStatuses()){
            User user = status.getAuthor();
            byte [] bytes = ByteArrayUtils.bytesFromUrl(user.getImageUrl());
            user.setImageBytes(bytes);
        }
    }

    ServerFacade getServerFacade(){
        return new ServerFacade();
    }
}
