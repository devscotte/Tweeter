package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.AliasClickServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;

public class AliasClickPresenter {

    private final View view;

    public interface View{

    }

    public AliasClickPresenter(View view){
        this.view = view;
    }

    public AliasClickResponse viewUserProfile(AliasClickRequest request)throws IOException, TweeterRemoteException {
        AliasClickServiceProxy aliasClickServiceProxy = getAliasClickService();

        return aliasClickServiceProxy.viewUserProfile(request);
    }

    AliasClickServiceProxy getAliasClickService(){return new AliasClickServiceProxy();}
}
