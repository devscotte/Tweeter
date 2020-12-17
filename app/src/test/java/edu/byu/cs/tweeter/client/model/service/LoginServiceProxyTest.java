package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginServiceProxyTest {
    private LoginRequest request;
    private LoginResponse response;
    private LoginServiceProxy mockLoginServiceProxy;
    private LoginPresenter presenter;

    private User currentUser;
    private AuthToken authToken;
    private String url = "/login";

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        authToken = new AuthToken();
        request = new LoginRequest("@FirstUser", "password");
        response = new LoginResponse(currentUser, authToken);

        ServerFacade mockserverFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockserverFacade.login(request, url)).thenReturn(response);

        mockLoginServiceProxy = Mockito.spy(new LoginServiceProxy());
        Mockito.when(mockLoginServiceProxy.getServerFacade()).thenReturn(mockserverFacade);
    }

    @Test
    public void good_test()throws IOException, TweeterRemoteException{
        LoginResponse loginResponse = mockLoginServiceProxy.login(request);
        Assertions.assertEquals(response, loginResponse);
    }
}
