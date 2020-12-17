package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class LoginDAOTest {
    private LoginRequest request;
    private LoginResponse response;
    private UserDAO loginDAO;
    private User currentUser;
    private AuthToken authToken;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        authToken = new AuthToken();
        request = new LoginRequest("@FirstUser", "password");
        response = new LoginResponse(currentUser, authToken);

        loginDAO = Mockito.mock(UserDAO.class);
    }

    @Test
    public void first_test(){
        Mockito.when(loginDAO.login(request)).thenReturn(response);
        LoginResponse loginResponse = loginDAO.login(request);

        Assertions.assertEquals(response, loginResponse);
    }

    @Test
    public void second_test(){
        User user = new User("Test", "User", "@TestUser", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        user.setPassword("password");
        request = new LoginRequest("@TestUser", "password");

        UserDAO realDao = new UserDAO();

        LoginResponse loginResponse = realDao.login(request);

        Assertions.assertEquals(loginResponse.isSuccess(), true);
    }

}
