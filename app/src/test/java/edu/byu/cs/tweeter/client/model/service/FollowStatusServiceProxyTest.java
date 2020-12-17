package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.FollowStatusPresenter;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;

public class FollowStatusServiceProxyTest {
    private FollowStatusRequest request;
    private FollowStatusResponse response;
    private FollowStatusServiceProxy mockFollowStatusServiceProxy;
    private FollowStatusPresenter presenter;
    private User currentUser;
    private User otherUser;
    private String url = "/changefollowing";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", null);
        otherUser = new User("FirstName1", "LastName1", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        request = new FollowStatusRequest(currentUser, otherUser, false);
        response = new FollowStatusResponse(true);

        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.changeFollowing(request, url)).thenReturn(response);

        mockFollowStatusServiceProxy = Mockito.spy(new FollowStatusServiceProxy());
        Mockito.when(mockFollowStatusServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void good_test()throws IOException, TweeterRemoteException{
        FollowStatusResponse followStatusResponse = mockFollowStatusServiceProxy.changeFollowing(request);
        Assertions.assertEquals(response, followStatusResponse);
    }
}
