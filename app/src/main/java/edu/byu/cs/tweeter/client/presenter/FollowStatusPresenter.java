package edu.byu.cs.tweeter.client.presenter;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowStatusServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;

public class FollowStatusPresenter {

    private final View view;

    public interface View{

    }

    public FollowStatusPresenter(View view){this.view = view;}

    public FollowStatusResponse changeFollow(FollowStatusRequest request) throws IOException, TweeterRemoteException {
        FollowStatusServiceProxy followStatusServiceProxy = getFollowStatusService();
        return followStatusServiceProxy.changeFollowing(request);
    }

    public FollowStatusServiceProxy getFollowStatusService(){return new FollowStatusServiceProxy();}
}
