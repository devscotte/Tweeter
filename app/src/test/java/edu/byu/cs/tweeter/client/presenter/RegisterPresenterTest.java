package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxy;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.RegisterServiceProxyTest;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterPresenterTest {

    private RegisterRequest request;
    private RegisterResponse response;
    private RegisterServiceProxy mockRegisterServiceProxy;
    private RegisterPresenter presenter;

    private User currentUser;
    private AuthToken authToken;

    @BeforeEach
    public void setup()throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        authToken = new AuthToken();
        request = new RegisterRequest("FirstName", "LastName", "FirstUser", "password", null);
        response = new RegisterResponse(currentUser, authToken);

        mockRegisterServiceProxy = Mockito.mock(RegisterServiceProxy.class);
        Mockito.when(mockRegisterServiceProxy.register(request)).thenReturn(response);

        presenter = Mockito.spy(new RegisterPresenter(new RegisterPresenter.View() {}));
        Mockito.when(presenter.getRegisterService()).thenReturn(mockRegisterServiceProxy);
    }

    @Test
    public void goodRegister()throws IOException, TweeterRemoteException{
        Mockito.when(mockRegisterServiceProxy.register(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.register(request));
    }

    @Test
    public void badRegister()throws IOException, TweeterRemoteException{
        Mockito.when(mockRegisterServiceProxy.register(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, ()-> {
            presenter.register(request);
        });
    }
}
