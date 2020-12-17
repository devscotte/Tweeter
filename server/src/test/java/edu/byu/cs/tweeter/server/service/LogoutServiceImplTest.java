package edu.byu.cs.tweeter.server.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LogoutServiceImplTest {
    private LogoutRequest request;
    private LogoutResponse response;
    private UserDAO logoutDAO;
    private LogoutServiceImpl logoutService;

    private User currentUser;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        request = new LogoutRequest(currentUser);
        response = new LogoutResponse();

        logoutDAO = Mockito.mock(UserDAO.class);
        Mockito.when(logoutDAO.logout(request)).thenReturn(response);

        logoutService = Mockito.spy(LogoutServiceImpl.class);
        Mockito.when(logoutService.getUserDao()).thenReturn(logoutDAO);
    }

    @Test
    public void good_test()throws IOException, TweeterRemoteException {
        LogoutResponse logoutResponse = logoutService.logout(request);
        Assertions.assertEquals(response, logoutResponse);
    }
}
