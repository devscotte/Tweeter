package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowStatusServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.FollowStatusServiceProxyTest;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;


public class FollowStatusPresenterTest {

    private FollowStatusRequest request;
    private FollowStatusResponse response;
    private FollowStatusServiceProxy mockFollowStatusServiceProxy;
    private FollowStatusPresenter presenter;
    private User currentUser;
    private User otherUser;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", null);
        otherUser = new User("FirstName1", "LastName1", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        request = new FollowStatusRequest(currentUser, otherUser, false);
        response = new FollowStatusResponse(true);

        mockFollowStatusServiceProxy = Mockito.mock(FollowStatusServiceProxy.class);
        Mockito.when(mockFollowStatusServiceProxy.changeFollowing(request)).thenReturn(response);

        presenter = Mockito.spy(new FollowStatusPresenter(new FollowStatusPresenter.View() {}));
        Mockito.when(presenter.getFollowStatusService()).thenReturn(mockFollowStatusServiceProxy);
    }

    @Test
    public void changeFollowing() throws IOException, TweeterRemoteException{
        Mockito.when(mockFollowStatusServiceProxy.changeFollowing(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.changeFollow(request));
    }

    @Test
    public void changeBadFollowing() throws IOException, TweeterRemoteException{
        Mockito.when(mockFollowStatusServiceProxy.changeFollowing(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.changeFollow(request);
        });
    }
}
