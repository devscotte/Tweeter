package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;

public class LogoutDAOTest {
    private LogoutRequest request;
    private LogoutResponse response;
    private UserDAO logoutDAO;
    private User currentUser;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        request = new LogoutRequest(currentUser);
        response = new LogoutResponse();
        logoutDAO = Mockito.mock(UserDAO.class);
    }

    @Test
    public void first_test(){
        Mockito.when(logoutDAO.logout(request)).thenReturn(response);
        LogoutResponse logoutResponse = logoutDAO.logout(request);

        Assertions.assertEquals(response, logoutResponse);
    }
}
