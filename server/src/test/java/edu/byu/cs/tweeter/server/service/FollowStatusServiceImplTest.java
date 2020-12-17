package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowStatusServiceImplTest {
    private FollowStatusRequest request;
    private FollowStatusResponse response;
    private FollowDAO followStatusDAO;
    private FollowStatusServiceImpl followStatusService;

    private User currentUser;
    private User otherUser;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", null);
        otherUser = new User("FirstName1", "LastName1", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        request = new FollowStatusRequest(currentUser, otherUser, false);
        response = new FollowStatusResponse(true);

        followStatusDAO = Mockito.mock(FollowDAO.class);
        Mockito.when(followStatusDAO.changeFollowing(request)).thenReturn(response);

        followStatusService = Mockito.spy(FollowStatusServiceImpl.class);
        Mockito.when(followStatusService.getFollowDao()).thenReturn(followStatusDAO);
    }

    @Test
    public void good_test(){
        FollowStatusResponse followStatusResponse = followStatusService.changeFollowing(request);
        Assertions.assertEquals(response, followStatusResponse);
    }
}
