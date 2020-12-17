package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.client.presenter.StoryPresenter;

public class GetStoryTask extends AsyncTask<StoryRequest, Void, StoryResponse> {

    private final StoryPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer{
        void storyRetrieved(StoryResponse storyResponse);
        void handleException(Exception exception);
    }

    public GetStoryTask(StoryPresenter presenter, Observer observer){
        if(observer == null){
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }
    @Override
    protected StoryResponse doInBackground(StoryRequest... storyRequests) {
        StoryResponse response = null;

        try {
            response = presenter.getStory(storyRequests[0]);
        } catch (Exception e) {
            exception = e;
        }
        return response;
    }

    @Override
    protected void onPostExecute(StoryResponse response) {
        if(exception != null){
            observer.handleException(exception);
        }
        else{
            observer.storyRetrieved(response);
        }
    }
}
