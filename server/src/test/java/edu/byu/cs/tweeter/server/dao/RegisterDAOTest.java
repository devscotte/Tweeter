package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;

public class RegisterDAOTest {
    private RegisterRequest request;
    private RegisterResponse response;
    private UserDAO registerDAO;

    private User currentUser;
    private AuthToken authToken;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        authToken = new AuthToken();
        request = new RegisterRequest("FirstName", "LastName", "FirstUser", "password", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        response = new RegisterResponse(currentUser, authToken);

        registerDAO = Mockito.mock(UserDAO.class);
    }

    @Test
    public void first_test(){
        Mockito.when(registerDAO.register(request)).thenReturn(response);
        RegisterResponse registerResponse = registerDAO.register(request);

        Assertions.assertEquals(response, registerResponse);
    }

    @Test
    public void second_test(){
        UserDAO userDAO = new UserDAO();

        RegisterResponse registerResponse = userDAO.register(request);

        Assertions.assertEquals(registerResponse.isSuccess(), true);
    }
}
