package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.LoginServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.LoginServiceProxyTest;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginPresenterTest {

    private LoginRequest request;
    private LoginResponse response;
    private LoginServiceProxy mockLoginServiceProxy;
    private LoginPresenter presenter;

    private User currentUser;
    private AuthToken authToken;

    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        authToken = new AuthToken();
        request = new LoginRequest("@FirstUser", "password");
        response = new LoginResponse(currentUser, authToken);

        mockLoginServiceProxy = Mockito.mock(LoginServiceProxy.class);
        Mockito.when(mockLoginServiceProxy.login(request)).thenReturn(response);

        presenter = Mockito.spy(new LoginPresenter(new LoginPresenter.View() {}));
        Mockito.when(presenter.getLoginService()).thenReturn(mockLoginServiceProxy);

    }

    @Test
    public void testLogin()throws IOException, TweeterRemoteException{
        Mockito.when(mockLoginServiceProxy.login(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.login(request));
    }

    @Test
    public void testBadLogin()throws IOException, TweeterRemoteException{
        Mockito.when(mockLoginServiceProxy.login(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.login(request);
        });
    }
}
