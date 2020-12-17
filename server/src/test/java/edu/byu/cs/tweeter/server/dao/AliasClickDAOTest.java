package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;

public class AliasClickDAOTest {
    private AliasClickRequest request;
    private AliasClickResponse response;
    private UserDAO aliasClickDAO;
    private UserDAO realDao;

    private User currentUser;
    private User someUser;
    private User myUser;
    private User testUser;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", null);
        someUser = new User("FirstName1", "LastName1", "@FirstName",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        myUser = new User("Test", "User", "@TestUser", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        testUser = new User("Person0", "0Person", "@Person0", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        request = new AliasClickRequest("@FirstName");
        response = new AliasClickResponse(someUser, false);

        aliasClickDAO = Mockito.mock(UserDAO.class);
        realDao = new UserDAO();
    }

    @Test
    public void first_test(){
        Mockito.when(aliasClickDAO.viewUserProfile(request)).thenReturn(response);
        AliasClickResponse aliasClickResponse = aliasClickDAO.viewUserProfile(request);

        Assertions.assertEquals(response, aliasClickResponse);
    }

    @Test
    public void second_test(){
        request = new AliasClickRequest("@Person0");
        request.setFollowerAlias(myUser.getAlias());

        AliasClickResponse aliasClickResponse = realDao.viewUserProfile(request);


        Assertions.assertEquals(aliasClickResponse.isSuccess(), true);
    }


}
