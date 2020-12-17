package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.FeedServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

public class FeedPresenterTest {

    private FeedRequest request;
    private FeedResponse response;
    private FeedServiceProxy mockFeedServiceProxy;
    private FeedPresenter presenter;

    private User currentUser;
    private User someUser;
    private Status someStatus;


    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", null);
        someUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        someStatus = new Status("my test status", someUser);
        request = new FeedRequest(currentUser, 10, someStatus);
        response = new FeedResponse(new ArrayList<Status>(), false);


        mockFeedServiceProxy = Mockito.mock(FeedServiceProxy.class);
        Mockito.when(mockFeedServiceProxy.getFeed(request)).thenReturn(response);

        presenter = Mockito.spy(new FeedPresenter(new FeedPresenter.View() {}));
        Mockito.when(presenter.getFeedService()).thenReturn(mockFeedServiceProxy);
    }

    @Test
    public void testGetFeed() throws IOException, TweeterRemoteException{
        Mockito.when(mockFeedServiceProxy.getFeed(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.getFeed(request));
    }

    @Test
    public void testFailFeed() throws IOException, TweeterRemoteException{
        Mockito.when(mockFeedServiceProxy.getFeed(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getFeed(request);
        });
    }
}
