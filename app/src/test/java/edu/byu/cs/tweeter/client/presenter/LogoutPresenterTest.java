package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.LogoutServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutPresenterTest {

    private LogoutRequest request;
    private LogoutResponse response;
    private LogoutServiceProxy mockLogoutServiceProxy;
    private LogoutPresenter presenter;

    private User currentUser;

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        request = new LogoutRequest(currentUser);
        response = new LogoutResponse();

        mockLogoutServiceProxy = Mockito.mock(LogoutServiceProxy.class);
        Mockito.when(mockLogoutServiceProxy.logout(request)).thenReturn(response);

        presenter = Mockito.spy(new LogoutPresenter(new LogoutPresenter.View() {}));
        Mockito.when(presenter.getLogoutService()).thenReturn(mockLogoutServiceProxy);
    }

    @Test
    public void goodLogout()throws IOException, TweeterRemoteException{
        Mockito.when(mockLogoutServiceProxy.logout(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.logout(request));
    }

    @Test
    public void badLogout()throws IOException, TweeterRemoteException{
        Mockito.when(mockLogoutServiceProxy.logout(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.logout(request);
        });
    }
}
