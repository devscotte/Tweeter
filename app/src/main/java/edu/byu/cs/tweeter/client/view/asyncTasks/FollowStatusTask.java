package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;
import edu.byu.cs.tweeter.client.presenter.FollowStatusPresenter;

public class FollowStatusTask extends AsyncTask<FollowStatusRequest, Void, FollowStatusResponse> {

    private final FollowStatusPresenter presenter;
    private final Observer observer;
    private Exception exception;

    @Override
    protected FollowStatusResponse doInBackground(FollowStatusRequest... followStatusRequests) {
        FollowStatusResponse response = null;

        try{
            response = presenter.changeFollow(followStatusRequests[0]);
        }catch (Exception e){
            exception = e;
        }

        return response;
    }

    public interface Observer{
        void followStatusRetrieved(FollowStatusResponse response);
        void handleException(Exception exception);
    }

    public FollowStatusTask(FollowStatusPresenter presenter, Observer observer){
        if(observer == null){
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected void onPostExecute(FollowStatusResponse response) {
        if(exception != null){
            observer.handleException(exception);
        }
        else{
            observer.followStatusRetrieved(response);
        }
    }
}
