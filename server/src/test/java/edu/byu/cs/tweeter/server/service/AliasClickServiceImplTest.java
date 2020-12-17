package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class AliasClickServiceImplTest {
    private AliasClickRequest request;
    private AliasClickResponse response;
    private UserDAO aliasClickDAO;
    private AliasClickServiceImpl aliasClickService;

    private User currentUser;
    private User someUser;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", null);
        someUser = new User("FirstName1", "LastName1", "@FirstName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        request = new AliasClickRequest("@FirstName");
        response = new AliasClickResponse(someUser, false);

        aliasClickDAO = Mockito.mock(UserDAO.class);
        Mockito.when(aliasClickDAO.viewUserProfile(request)).thenReturn(response);

        aliasClickService = Mockito.spy(AliasClickServiceImpl.class);
        Mockito.when(aliasClickService.getUserDao()).thenReturn(aliasClickDAO);
    }

    @Test
    public void good_test(){
        AliasClickResponse aliasClickResponse = aliasClickService.viewUserProfile(request);
        Assertions.assertEquals(response, aliasClickResponse);
    }
}
