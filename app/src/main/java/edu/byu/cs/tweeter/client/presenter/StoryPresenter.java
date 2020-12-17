package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryPresenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        // If needed, specify methods here that will be called on the view in response to model updates
    }

    public StoryPresenter(View view){this.view = view;}

    public StoryResponse getStory(StoryRequest request) throws IOException, TweeterRemoteException {
        StoryServiceProxy storyServiceProxy = getStoryService();
        return storyServiceProxy.getStory(request);
    }

    StoryServiceProxy getStoryService(){return new StoryServiceProxy();}
}
