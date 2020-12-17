package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class RegisterServiceImplTest {
    private RegisterRequest request;
    private RegisterResponse response;
    private UserDAO registerDAO;
    private RegisterServiceImpl registerService;

    private User currentUser;
    private AuthToken authToken;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        authToken = new AuthToken();
        request = new RegisterRequest("FirstName", "LastName", "FirstUser", "password", null);
        response = new RegisterResponse(currentUser, authToken);

        registerDAO = Mockito.mock(UserDAO.class);
        Mockito.when(registerDAO.register(request)).thenReturn(response);

        registerService = Mockito.spy(RegisterServiceImpl.class);
        Mockito.when(registerService.getUserDao()).thenReturn(registerDAO);
    }

    @Test
    public void good_test(){
        RegisterResponse registerResponse = registerService.register(request);
        Assertions.assertEquals(response, registerResponse);
    }
}
