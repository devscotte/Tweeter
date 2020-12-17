package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;

import edu.byu.cs.tweeter.client.model.service.StoryServiceProxy;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.StoryServiceProxyTest;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryPresenterTest {

    private StoryRequest request;
    private StoryResponse response;
    private StoryServiceProxy mockStoryServiceProxy;
    private StoryPresenter presenter;

    private User currentUser;
    private Status someStatus;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        someStatus = new Status("my test status", currentUser);
        request = new StoryRequest(currentUser, 10, someStatus);
        response = new StoryResponse(new ArrayList<Status>(), false);

        mockStoryServiceProxy = Mockito.mock(StoryServiceProxy.class);
        Mockito.when(mockStoryServiceProxy.getStory(request)).thenReturn(response);

        presenter = Mockito.spy(new StoryPresenter(new StoryPresenter.View() {}));
        Mockito.when(presenter.getStoryService()).thenReturn(mockStoryServiceProxy);
    }

    @Test
    public void goodStoryReq()throws IOException, TweeterRemoteException{
        Mockito.when(mockStoryServiceProxy.getStory(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.getStory(request));
    }

    @Test
    public void badStoryReq()throws IOException, TweeterRemoteException{
        Mockito.when(mockStoryServiceProxy.getStory(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.getStory(request);
        });
    }
}
