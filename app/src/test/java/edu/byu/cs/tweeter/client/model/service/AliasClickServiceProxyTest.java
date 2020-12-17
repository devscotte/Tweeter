package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.AliasClickPresenter;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;

public class AliasClickServiceProxyTest {
    private AliasClickRequest request;
    private AliasClickResponse response;
    private AliasClickServiceProxy mockClickService;
    User currentUser;
    User someUser;
    private String url = "/viewuserprofile";

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", null);
        someUser = new User("FirstName1", "LastName1", "@FirstName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        request = new AliasClickRequest("@FirstName");
        response = new AliasClickResponse(someUser, false);

        ServerFacade mockserverFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockserverFacade.viewUserProfile(request, url)).thenReturn(response);

        mockClickService = Mockito.spy(new AliasClickServiceProxy());
        Mockito.when(mockClickService.getServerFacade()).thenReturn(mockserverFacade);
    }

    @Test
    public void good_test()throws IOException, TweeterRemoteException{
        AliasClickResponse aliasClickResponse = mockClickService.viewUserProfile(request);
        Assertions.assertEquals(response, aliasClickResponse);
    }




}
