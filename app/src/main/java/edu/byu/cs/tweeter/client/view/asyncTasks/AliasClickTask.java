package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;
import edu.byu.cs.tweeter.client.presenter.AliasClickPresenter;

public class AliasClickTask extends AsyncTask<AliasClickRequest, Void, AliasClickResponse> {

    private final AliasClickPresenter presenter;
    private final Observer observer;
    private Exception exception;

    @Override
    protected AliasClickResponse doInBackground(AliasClickRequest... aliasClickRequests) {
        AliasClickResponse response = null;

        try{
            response = presenter.viewUserProfile(aliasClickRequests[0]);
        }catch (Exception e){
            exception = e;
        }

        return response;
    }

    public interface Observer{
        void aliasClickRetrieved(AliasClickResponse response);
        void handleException(Exception exception);
    }

    public AliasClickTask(AliasClickPresenter presenter, Observer observer){
        if(observer == null){
            throw new NullPointerException();
        }

        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected void onPostExecute(AliasClickResponse response) {
        if(exception != null){
            observer.handleException(exception);
        }
        else{
            observer.aliasClickRetrieved(response);
        }
    }
}
