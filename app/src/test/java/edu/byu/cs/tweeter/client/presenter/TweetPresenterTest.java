package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.TweetServiceProxy;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.TweetServiceProxyTest;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class TweetPresenterTest {

    private TweetResponse response;
    private TweetRequest request;
    private TweetServiceProxy mockTweetServiceProxy;
    private TweetPresenter presenter;

    private User currentUser;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        request = new TweetRequest(currentUser, "hi there");
        response = new TweetResponse();

        mockTweetServiceProxy = Mockito.mock(TweetServiceProxy.class);
        Mockito.when(mockTweetServiceProxy.tweet(request)).thenReturn(response);

        presenter = Mockito.spy(new TweetPresenter(new TweetPresenter.View() {}));
        Mockito.when(presenter.getTweetService()).thenReturn(mockTweetServiceProxy);
    }

    @Test
    public void goodTweeter() throws IOException, TweeterRemoteException{
        Mockito.when(mockTweetServiceProxy.tweet(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.tweet(request));
    }


}
