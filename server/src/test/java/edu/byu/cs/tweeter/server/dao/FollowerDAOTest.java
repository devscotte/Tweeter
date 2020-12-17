package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;

public class FollowerDAOTest {
    private FollowerRequest request;
    private FollowerResponse response;
    private FollowDAO followerDAO;
    private FollowDAO realDao;
    private User myUser;

    @BeforeEach
    public void setup(){
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        request = new FollowerRequest(currentUser, 3, null);
        response = new FollowerResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        followerDAO = Mockito.mock(FollowDAO.class);

        myUser = new User("Person0", "0Person", "@Person0", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        realDao = new FollowDAO();
    }

    @Test
    public void first_test(){
        Mockito.when(followerDAO.getFollowers(request)).thenReturn(response);
        FollowerResponse followerResponse = followerDAO.getFollowers(request);

        Assertions.assertEquals(response, followerResponse);
    }

    @Test
    public void second_test(){
        FollowerRequest followerRequest = new FollowerRequest(myUser, 10, null);
        FollowerResponse followerResponse = realDao.getFollowers(followerRequest);

        Assertions.assertEquals(followerResponse.isSuccess(), true);
    }

}
