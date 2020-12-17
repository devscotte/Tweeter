package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.TweetServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class TweetPresenter {

    private final View view;

    public interface View{

    }

    public TweetPresenter(View view){this.view = view;}

    public TweetResponse tweet(TweetRequest request)throws IOException, TweeterRemoteException {
        TweetServiceProxy tweetServiceProxy = getTweetService();
        return tweetServiceProxy.tweet(request);
    }

    TweetServiceProxy getTweetService(){return new TweetServiceProxy();}
}
