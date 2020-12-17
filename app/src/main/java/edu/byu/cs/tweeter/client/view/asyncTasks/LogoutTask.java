package edu.byu.cs.tweeter.client.view.asyncTasks;

import android.os.AsyncTask;

import java.io.IOException;

import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.client.presenter.LogoutPresenter;

public class LogoutTask extends AsyncTask<LogoutRequest, Void, LogoutResponse> {

    private final LogoutPresenter presenter;
    private final Observer observer;
    private Exception exception;

    public interface Observer{
        void logoutRetrieved(LogoutResponse response);
        void handleException(Exception exception);
    }

    public LogoutTask(LogoutPresenter presenter, Observer observer){
        this.presenter = presenter;
        this.observer = observer;
    }

    @Override
    protected LogoutResponse doInBackground(LogoutRequest... logoutRequests) {
        LogoutResponse logoutResponse = null;

        try{
            logoutResponse = presenter.logout(logoutRequests[0]);
        }catch (Exception e){
            exception = e;
        }
        return logoutResponse;
    }

    @Override
    protected void onPostExecute(LogoutResponse response) {
        if(exception != null){
            observer.handleException(exception);
        }
        else{
            observer.logoutRetrieved(response);
        }
    }
}
