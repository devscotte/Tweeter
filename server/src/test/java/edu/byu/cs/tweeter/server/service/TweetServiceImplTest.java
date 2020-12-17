package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class TweetServiceImplTest {
    private TweetRequest request;
    private TweetResponse response;
    private StoryDAO tweetDAO;
    private TweetServiceImpl tweetService;

    private User currentUser;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        request = new TweetRequest(currentUser, "hi there");
        response = new TweetResponse();

        tweetDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(tweetDAO.tweet(request)).thenReturn(response);

        tweetService = Mockito.spy(TweetServiceImpl.class);
        Mockito.when(tweetService.getStoryDao()).thenReturn(tweetDAO);
    }

    @Test
    public void good_test(){
        TweetResponse tweetResponse = tweetService.tweet(request);
        Assertions.assertEquals(response, tweetResponse);
    }
}
