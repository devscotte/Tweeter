package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

public class FeedServiceProxyTest {
    private FeedRequest request;
    private FeedResponse response;
    private FeedServiceProxy mockFeedServiceProxy;

    private User currentUser;
    private User someUser;
    private Status someStatus;
    private String url = "/getfeed";

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", null);
        someUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        someStatus = new Status("my test status", someUser);
        request = new FeedRequest(currentUser, 10, someStatus);
        response = new FeedResponse(new ArrayList<Status>(), false);

        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getFeed(request, url)).thenReturn(response);

        mockFeedServiceProxy = Mockito.spy(new FeedServiceProxy());
        Mockito.when(mockFeedServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void good_test()throws IOException, TweeterRemoteException{
        FeedResponse feedResponse = mockFeedServiceProxy.getFeed(request);
        Assertions.assertEquals(response, feedResponse);
    }
}
