package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterServiceProxyTest {
    private RegisterRequest request;
    private RegisterResponse response;
    private RegisterServiceProxy mockRegisterServiceProxy;
    private RegisterPresenter presenter;

    private User currentUser;
    private AuthToken authToken;
    private String url = "/register";

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        authToken = new AuthToken();
        request = new RegisterRequest("FirstName", "LastName", "FirstUser", "password", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        response = new RegisterResponse(currentUser, authToken);

        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.register(request, url)).thenReturn(response);

        mockRegisterServiceProxy = Mockito.spy(new RegisterServiceProxy());
        Mockito.when(mockRegisterServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void good_test()throws IOException, TweeterRemoteException{
        RegisterResponse registerResponse = mockRegisterServiceProxy.register(request);
        Assertions.assertEquals(response, registerResponse);
    }
}
