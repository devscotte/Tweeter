package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;

public class FeedServiceImplTest {
    private FeedRequest request;
    private FeedResponse response;
    private FeedDAO feedDAO;
    private FeedServiceImpl feedService;

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
        Mockito.when(feedDAO.getFeed(request)).thenReturn(response);

        feedService = Mockito.spy(FeedServiceImpl.class);
        Mockito.when(feedService.getFeedDao()).thenReturn(feedDAO);
    }

    @Test
    public void good_test(){
        FeedResponse feedResponse = feedService.getFeed(request);
        Assertions.assertEquals(response, feedResponse);
    }
}
