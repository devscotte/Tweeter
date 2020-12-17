package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;

public class StoryDAOTest {
    private StoryRequest request;
    private StoryResponse response;
    private StoryDAO storyDAO;
    private User currentUser = new User("FirstName", "LastName", "@FirstUser", null);
    private Status someStatus = new Status("my test status", currentUser);

    @BeforeEach
    public void setup(){
        request = new StoryRequest(currentUser, 10, null);
        response = new StoryResponse(new ArrayList<Status>(), false);
        storyDAO = Mockito.mock(StoryDAO.class);
    }

    @Test
    public void first_test(){
        Mockito.when(storyDAO.getStory(request)).thenReturn(response);
        StoryResponse storyResponse = storyDAO.getStory(request);

        Assertions.assertEquals(0, storyResponse.getStatuses().size());
    }

    @Test
    public void second_test(){
        User user = new User("Test", "User", "@TestUser", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        request = new StoryRequest(user, 10, null);

        StoryDAO storyDAO = new StoryDAO();

        StoryResponse storyResponse = storyDAO.getStory(request);

        Assertions.assertEquals(storyResponse.isSuccess(), true);
    }
}
