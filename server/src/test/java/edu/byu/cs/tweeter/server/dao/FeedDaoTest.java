package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;

public class FeedDaoTest {
    private FeedRequest request;
    private FeedResponse response;
    private FeedDAO feedDAO;
    private FeedDAO realDao;

    private User currentUser;
    private User someUser;
    private Status someStatus;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", null);
        someUser = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        someStatus = new Status("my test status", someUser);
        request = new FeedRequest(currentUser, 10, someStatus);
        response = new FeedResponse(new ArrayList<Status>(), false);
        feedDAO = Mockito.mock(FeedDAO.class);

        realDao = new FeedDAO();
    }

    @Test
    public void first_test(){
        Mockito.when(feedDAO.getFeed(request)).thenReturn(response);
        FeedResponse feedResponse = feedDAO.getFeed(request);

        Assertions.assertEquals(response, feedResponse);
    }

    @Test void second_test(){
        User user = new User("Person0", "0Person", "@Person0", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        FeedRequest feedRequest = new FeedRequest(user, 10, null);

        FeedResponse feedResponse = realDao.getFeed(feedRequest);

        Assertions.assertEquals(feedResponse.isSuccess(), true);
    }
}
