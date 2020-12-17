package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class IntegrationTest {
    private User testUser = new User("Test", "User", "@TestUser",
            "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
    private User anotherUser = new User("Some", "User", "@SomeUser", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
    private String aliasClickUrl = "/viewuserprofile";
    private String feedUrl = "/getfeed";
    private String followerUrl = "/getfollowers";
    private String followeesUrl = "/getfollowing";
    private String changeStatusUrl = "/changefollowing";
    private String loginUrl = "/login";
    private String logoutUrl = "/logout";
    private String registerUrl = "/register";
    private String storyUrl = "/getstory";
    private String tweetUrl = "/tweet";

    @Test
    public void alias_test() throws IOException, TweeterRemoteException {
        AliasClickRequest request = new AliasClickRequest("@TestUser");
        request.setFollowerAlias("@People0");
        AliasClickResponse response = new AliasClickResponse(testUser, false);

        ServerFacade serverFacade = new ServerFacade();
        AliasClickResponse aliasClickResponse = serverFacade.viewUserProfile(request, aliasClickUrl);

        Assertions.assertEquals(response.isSuccess(), aliasClickResponse.isSuccess());
    }

    @Test
    public void feed_test()throws IOException, TweeterRemoteException{
        FeedRequest request = new FeedRequest(testUser, 10, null);
        FeedResponse response = new FeedResponse(new ArrayList<Status>(), false);

        ServerFacade serverFacade = new ServerFacade();
        FeedResponse feedResponse = serverFacade.getFeed(request, feedUrl);

        Assertions.assertEquals(response.isSuccess(), feedResponse.isSuccess());
    }

    @Test
    public void follower_test()throws IOException, TweeterRemoteException{
        FollowerRequest request = new FollowerRequest(testUser, 10, null);
        FollowerResponse response = new FollowerResponse(new ArrayList<User>(), false);

        ServerFacade serverFacade = new ServerFacade();
        FollowerResponse followerResponse = serverFacade.getFollowers(request, followerUrl);

        Assertions.assertEquals(response.isSuccess(), followerResponse.isSuccess());
    }

    @Test
    public void following_test()throws IOException, TweeterRemoteException{
        FollowingRequest request = new FollowingRequest(testUser, 10, null);
        FollowingResponse response = new FollowingResponse(new ArrayList<User>(), false);

        ServerFacade serverFacade = new ServerFacade();
        FollowingResponse followingResponse = serverFacade.getFollowees(request, followeesUrl);

        Assertions.assertEquals(response.isSuccess(), followingResponse.isSuccess());
    }

    @Test
    public void followStatus_test()throws IOException, TweeterRemoteException{
        FollowStatusRequest request = new FollowStatusRequest(testUser, anotherUser, false);
        FollowStatusResponse response = new FollowStatusResponse(true);

        ServerFacade serverFacade = new ServerFacade();
        FollowStatusResponse followStatusResponse = serverFacade.changeFollowing(request, changeStatusUrl);

        Assertions.assertEquals(response.isSuccess(), followStatusResponse.isSuccess());
    }

    @Test
    public void login_test()throws IOException, TweeterRemoteException{
        LoginRequest request = new LoginRequest("@TestUser", "password");
        LoginResponse response = new LoginResponse(testUser, new AuthToken());

        ServerFacade serverFacade = new ServerFacade();
        LoginResponse loginResponse = serverFacade.login(request, loginUrl);

        Assertions.assertEquals(response.isSuccess(), loginResponse.isSuccess());
    }

    @Test
    public void register_test()throws IOException, TweeterRemoteException{
        User tempUser = new User ("Hi", "Paul", "@HiPaul", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        RegisterRequest request = new RegisterRequest("Hi", "Paul", "@HiPaul", "password", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        RegisterResponse response = new RegisterResponse(tempUser, new AuthToken());

        ServerFacade serverFacade = new ServerFacade();
        RegisterResponse registerResponse = serverFacade.register(request, registerUrl);

        Assertions.assertEquals(response.isSuccess(), registerResponse.isSuccess());
    }

    @Test
    public void story_test()throws IOException, TweeterRemoteException{
        StoryRequest request = new StoryRequest(testUser, 10, null);
        StoryResponse response = new StoryResponse(new ArrayList<Status>(), false);

        ServerFacade serverFacade = new ServerFacade();
        StoryResponse storyResponse = serverFacade.getStory(request, storyUrl);

        Assertions.assertEquals(response.isSuccess(), storyResponse.isSuccess());
    }

    /*@Test
    public void tweet_test()throws IOException, TweeterRemoteException{
        TweetRequest request = new TweetRequest(testUser, "server test");
        TweetResponse response = new TweetResponse();

        ServerFacade serverFacade = new ServerFacade();
        TweetResponse tweetResponse = serverFacade.tweet(request, tweetUrl);

        Assertions.assertEquals(response.isSuccess(), tweetResponse.isSuccess());
    }*/

   /* @Test
    public void logout_test()throws IOException, TweeterRemoteException{
        LogoutRequest request = new LogoutRequest(testUser);
        LogoutResponse response = new LogoutResponse();

        ServerFacade serverFacade = new ServerFacade();
        LogoutResponse logoutResponse = serverFacade.logout(request, logoutUrl);

        Assertions.assertEquals(response.isSuccess(), logoutResponse.isSuccess());
    }*/


}
