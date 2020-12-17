package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutPresenter {

    private final View view;

    public interface View{

    }

    public LogoutPresenter(View view){
        this.view = view;
    }

    public LogoutResponse logout(LogoutRequest request)throws IOException, TweeterRemoteException {
        LogoutServiceProxy logoutServiceProxy = getLogoutService();
        return logoutServiceProxy.logout(request);
    }

    public LogoutServiceProxy getLogoutService(){return new LogoutServiceProxy();}
}
