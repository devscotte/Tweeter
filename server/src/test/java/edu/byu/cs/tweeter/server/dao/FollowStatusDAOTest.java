package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;

public class FollowStatusDAOTest {
    private FollowStatusRequest request;
    private FollowStatusResponse response;
    private FollowDAO followStatusDAO;
    private User currentUser;
    private User otherUser;
    private User myUser;
    private User anotherUser;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", null);
        otherUser = new User("FirstName1", "LastName1", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        request = new FollowStatusRequest(currentUser, otherUser, false);
        response = new FollowStatusResponse(true);
        followStatusDAO = Mockito.mock(FollowDAO.class);

        myUser = new User("Test", "User", "@TestUser", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        anotherUser = new User("Person0", "0Person", "@Person0", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");


    }

    @Test
    public void first_test(){
        Mockito.when(followStatusDAO.changeFollowing(request)).thenReturn(response);
        FollowStatusResponse followStatusResponse = followStatusDAO.changeFollowing(request);

        Assertions.assertEquals(response, followStatusResponse);
    }

    @Test
    public void second_test(){
        FollowDAO followDAO = new FollowDAO();
        FollowStatusRequest followStatusRequest = new FollowStatusRequest(myUser, anotherUser, true);

        FollowStatusResponse followStatusResponse = followDAO.changeFollowing(followStatusRequest);

        Assertions.assertEquals(followStatusResponse.isSuccess(), true);

        followDAO.changeFollowing(new FollowStatusRequest(myUser, anotherUser, false));
    }


}
