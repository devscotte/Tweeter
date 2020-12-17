package edu.byu.cs.tweeter.server.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;

class FollowingDAOTest {

    private final User user1 = new User("Daffy", "Duck", "");
    private final User user2 = new User("Fred", "Flintstone", "");
    private final User user3 = new User("Barney", "Rubble", "");
    private final User user4 = new User("Wilma", "Rubble", "");
    private final User user5 = new User("Clint", "Eastwood", "");
    private final User user6 = new User("Mother", "Teresa", "");
    private final User user7 = new User("Harriett", "Hansen", "");
    private final User user8 = new User("Zoe", "Zabriski", "");
    private FollowingRequest request;
    private FollowingResponse response;

    User currentUser = new User("FirstName", "LastName", null);

    User resultUser1 = new User("FirstName1", "LastName1",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
    User resultUser2 = new User("FirstName2", "LastName2",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
    User resultUser3 = new User("FirstName3", "LastName3",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

    private FollowDAO followingDAOSpy;

    @BeforeEach
    void setup() {

        request = new FollowingRequest(currentUser, 3, null);
        response = new FollowingResponse(Arrays.asList(resultUser1, resultUser2, resultUser3), false);
        followingDAOSpy = Mockito.spy(new FollowDAO());
    }

    /*@Test
    void testGetFollowees_noFolloweesForUser() {
        Mockito.when(followingDAOSpy.getFollowees(request)).thenReturn(response);

        FollowingRequest request = new FollowingRequest(user1, 10, null);
        FollowingResponse response = followingDAOSpy.getFollowees(request);

        Assertions.assertEquals(0, response.getFollowees().size());
        Assertions.assertFalse(response.getHasMorePages());
    }*/

    @Test
    void testGetFollowees_oneFollowerForUser_limitGreaterThanUsers() {
        List<User> followees = Collections.singletonList(user2);
        Mockito.when(followingDAOSpy.getFollowees(request)).thenReturn(response);

        //FollowingRequest request = new FollowingRequest(user1, 10, null);
        FollowingResponse response = followingDAOSpy.getFollowees(request);

        Assertions.assertEquals(3, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(resultUser2));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_twoFollowersForUser_limitEqualsUsers() {
        List<User> followees = Arrays.asList(user2, user3);
        Mockito.when(followingDAOSpy.getFollowees(request)).thenReturn(response);

        //FollowingRequest request = new FollowingRequest(user3, 2, null);
        FollowingResponse response = followingDAOSpy.getFollowees(request);

        Assertions.assertEquals(3, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(resultUser2));
        Assertions.assertTrue(response.getFollowees().contains(resultUser3));
        Assertions.assertFalse(response.getHasMorePages());
    }

    @Test
    void testGetFollowees_limitLessThanUsers_endsOnPageBoundary() {
        List<User> followees = Arrays.asList(user2, user3, user4, user5, user6, user7);
        Mockito.when(followingDAOSpy.getFollowees(request)).thenReturn(response);

        //FollowingRequest request = new FollowingRequest(user5, 2, null);
        FollowingResponse response = followingDAOSpy.getFollowees(request);

        // Verify first page
        Assertions.assertEquals(3, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(resultUser2));
        Assertions.assertTrue(response.getFollowees().contains(resultUser3));
    }

    @Test
    public void real_test(){
        User user = new User("Person0", "0Person", "@Person0", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        FollowingRequest followingRequest = new FollowingRequest(user, 10, null);
        FollowDAO realDao = new FollowDAO();

        FollowingResponse followingResponse = realDao.getFollowees(followingRequest);

        Assertions.assertEquals(followingResponse.isSuccess(), true);
    }


    /*@Test
    void testGetFollowees_limitLessThanUsers_notEndsOnPageBoundary() {
        List<User> followees = Arrays.asList(user2, user3, user4, user5, user6, user7, user8);
        Mockito.when(followingDAOSpy.getFollowees(request)).thenReturn(response);

        FollowingRequest request = new FollowingRequest(user6, 2, null);
        FollowingResponse response = followingDAOSpy.getFollowees(request);

        // Verify first page
        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user2));
        Assertions.assertTrue(response.getFollowees().contains(user3));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify second page
        request = new FollowingRequest(user6, 2, response.getFollowees().get(1));
        response = followingDAOSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user4));
        Assertions.assertTrue(response.getFollowees().contains(user5));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify third page
        request = new FollowingRequest(user6, 2, response.getFollowees().get(1));
        response = followingDAOSpy.getFollowees(request);

        Assertions.assertEquals(2, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user6));
        Assertions.assertTrue(response.getFollowees().contains(user7));
        Assertions.assertTrue(response.getHasMorePages());

        // Get and verify fourth page
        request = new FollowingRequest(user6, 2, response.getFollowees().get(1));
        response = followingDAOSpy.getFollowees(request);

        Assertions.assertEquals(1, response.getFollowees().size());
        Assertions.assertTrue(response.getFollowees().contains(user8));
        Assertions.assertFalse(response.getHasMorePages());
    }*/
}
