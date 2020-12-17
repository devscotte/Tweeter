package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.TweetPresenter;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class TweetServiceProxyTest {
    private TweetResponse response;
    private TweetRequest request;
    private TweetServiceProxy mockTweetServiceProxy;
    private TweetPresenter presenter;

    private User currentUser;
    private String url = "/tweet";

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        request = new TweetRequest(currentUser, "hi there");
        response = new TweetResponse();

        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.tweet(request, url)).thenReturn(response);

        mockTweetServiceProxy = Mockito.spy(new TweetServiceProxy());
        Mockito.when(mockTweetServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void good_test()throws IOException, TweeterRemoteException{
        TweetResponse tweetResponse = mockTweetServiceProxy.tweet(request);
        Assertions.assertEquals(response, tweetResponse);
    }
}
