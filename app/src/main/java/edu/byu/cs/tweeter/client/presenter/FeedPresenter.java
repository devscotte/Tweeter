package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FeedServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

public class FeedPresenter {

    private final View view;

    public interface View{

    }

    public FeedPresenter(View view){this.view = view;}

    public FeedResponse getFeed(FeedRequest request) throws IOException, TweeterRemoteException {
        FeedServiceProxy feedServiceProxy = getFeedService();

        return feedServiceProxy.getFeed(request);
    }

    FeedServiceProxy getFeedService(){return new FeedServiceProxy();}
}
