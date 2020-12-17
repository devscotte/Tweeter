package edu.byu.cs.tweeter.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class StoryServiceImplTest {
    private StoryRequest request;
    private StoryResponse response;
    private StoryDAO storyDAO;
    private StoryServiceImpl storyService;

    private User currentUser;
    private Status someStatus;

    @BeforeEach
    public void setup(){
        currentUser = new User("FirstName", "LastName", "@FirstUser", null);
        someStatus = new Status("my test status", currentUser);
        request = new StoryRequest(currentUser, 10, someStatus);
        response = new StoryResponse(new ArrayList<Status>(), false);

        storyDAO = Mockito.mock(StoryDAO.class);
        Mockito.when(storyDAO.getStory(request)).thenReturn(response);

        storyService = Mockito.spy(StoryServiceImpl.class);
        Mockito.when(storyService.getStoryDao()).thenReturn(storyDAO);
    }

    @Test
    public void good_test(){
        StoryResponse storyResponse = storyService.getStory(request);
        Assertions.assertEquals(response, storyResponse);
    }
}
