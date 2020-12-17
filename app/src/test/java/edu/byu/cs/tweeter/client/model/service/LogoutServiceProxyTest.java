package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutServiceProxyTest {
    private LogoutRequest request;
    private LogoutResponse response;
    private LogoutServiceProxy mockLogoutServiceProxy;
    private LogoutPresenter presenter;

    private User currentUser;
    private String url = "/logout";

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        request = new LogoutRequest(currentUser);
        response = new LogoutResponse();

        ServerFacade mockServerFacade = Mockito.mock(ServerFacade.class);
        Mockito.when(mockServerFacade.logout(request, url)).thenReturn(response);

        mockLogoutServiceProxy = Mockito.spy(new LogoutServiceProxy());
        Mockito.when(mockLogoutServiceProxy.getServerFacade()).thenReturn(mockServerFacade);
    }

    @Test
    public void good_test()throws IOException, TweeterRemoteException{
        LogoutResponse logoutResponse = mockLogoutServiceProxy.logout(request);
        Assertions.assertEquals(response, logoutResponse);
    }
}
