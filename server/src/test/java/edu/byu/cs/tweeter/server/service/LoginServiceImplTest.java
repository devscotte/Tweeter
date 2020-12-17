package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class LoginServiceImplTest {
    private LoginRequest request;
    private LoginResponse response;
    private UserDAO loginDAO;
    private LoginServiceImpl loginService;

    private User currentUser;
    private AuthToken authToken;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        authToken = new AuthToken();
        request = new LoginRequest("@FirstUser", "password");
        response = new LoginResponse(currentUser, authToken);

        loginDAO = Mockito.mock(UserDAO.class);
        Mockito.when(loginDAO.login(request)).thenReturn(response);

        loginService = Mockito.spy(LoginServiceImpl.class);
        Mockito.when(loginService.getUserDao()).thenReturn(loginDAO);
    }

    @Test
    public void good_test(){
        LoginResponse loginResponse = loginService.login(request);
        Assertions.assertEquals(response, loginResponse);
    }
}
