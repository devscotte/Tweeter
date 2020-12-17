package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.StoryPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryServiceProxyTest {
    private StoryRequest request;
    private StoryResponse response;
    private StoryServiceProxy mockStoryServiceProxy;
    private StoryPresenter presenter;

    private User currentUser;
    private Status someStatus;
    private String url = "/getstory";

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        someStatus = new Status("my test status", currentUser);
        request = new StoryRequest(currentUser, 10, someStatus);
        response = new StoryResponse(new ArrayList<Status>(), false);

        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.getStory(request, url)).thenReturn(response);

        mockStoryServiceProxy = Mockito.spy(new StoryServiceProxy());
        Mockito.when(mockStoryServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void good_test()throws IOException, TweeterRemoteException{
        StoryResponse storyResponse = mockStoryServiceProxy.getStory(request);
        Assertions.assertEquals(response, storyResponse);
    }
}
