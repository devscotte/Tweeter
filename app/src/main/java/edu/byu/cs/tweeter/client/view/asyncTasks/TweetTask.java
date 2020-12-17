package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.client.presenter.TweetPresenter;

public class TweetTask extends AsyncTask<TweetRequest, Void, TweetResponse> {

    private final TweetPresenter presenter;
    private final Observer observer;
    private Exception exception;

    @Override
    protected TweetResponse doInBackground(TweetRequest... tweetRequests) {
        TweetResponse tweetResponse = null;

        try{
            tweetResponse = presenter.tweet(tweetRequests[0]);
        }catch (Exception e){
            exception = e;
        }

        return tweetResponse;
    }

    public interface Observer{
        void tweetRetrieved(TweetResponse response);
        void handleException(Exception exception);
    }

    public TweetTask(TweetPresenter presenter, Observer observer){
        if(observer == null){
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected void onPostExecute(TweetResponse response) {
        if(exception != null){
            observer.handleException(exception);
        }
        else{
            observer.tweetRetrieved(response);
        }
    }
}
