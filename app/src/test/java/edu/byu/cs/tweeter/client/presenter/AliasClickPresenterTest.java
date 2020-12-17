package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.AliasClickServiceProxy;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;

public class AliasClickPresenterTest {

    private AliasClickRequest request;
    private AliasClickResponse response;
    private AliasClickServiceProxy mockClickService;
    private AliasClickPresenter presenter;
    User currentUser;
    User someUser;


    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        currentUser = new User("FirstName", "LastName", null);
        someUser = new User("FirstName1", "LastName1", "@FirstName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        request = new AliasClickRequest("@FirstName");
        response = new AliasClickResponse(someUser, false);

        mockClickService = Mockito.mock(AliasClickServiceProxy.class);
        Mockito.when(mockClickService.viewUserProfile(request)).thenReturn(response);

        presenter = Mockito.spy(new AliasClickPresenter(new AliasClickPresenter.View() {}));
        Mockito.when(presenter.getAliasClickService()).thenReturn(mockClickService);
    }

    @Test
    public void testClick() throws IOException, TweeterRemoteException{
        Mockito.when(mockClickService.viewUserProfile(request)).thenReturn(response);

        Assertions.assertEquals(response, presenter.viewUserProfile(request));
    }

    @Test
    public void testBadClick() throws IOException, TweeterRemoteException{
        Mockito.when(mockClickService.viewUserProfile(request)).thenThrow(new IOException());

        Assertions.assertThrows(IOException.class, () -> {
            presenter.viewUserProfile(request);
        });
    }
}
