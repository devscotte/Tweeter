package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class TweetDAOTest {
    private TweetRequest request;
    private TweetResponse response;
    private StoryDAO tweetDAO;
    private User currentUser;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        request = new TweetRequest(currentUser, "hi there");
        response = new TweetResponse();
        tweetDAO = Mockito.mock(StoryDAO.class);
    }

    @Test
    public void first_test(){
        Mockito.when(tweetDAO.tweet(request)).thenReturn(response);
        TweetResponse tweetResponse = tweetDAO.tweet(request);

        Assertions.assertEquals(response, tweetResponse);
    }

}
