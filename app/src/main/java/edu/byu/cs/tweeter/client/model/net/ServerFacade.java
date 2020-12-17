package edu.byu.cs.tweeter.client.model.net;

import com.google.gson.internal.$Gson$Preconditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.byu.cs.tweeter.client.model.net.FollowGenerator;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Follow;
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

/**
 * Acts as a Facade to the Tweeter server. All network requests to the server should go through
 * this class.
 */
public class ServerFacade {

    private static Map<User, List<User>> followeesByFollower;
    private static Map<User, List<User>> followersByFollowee;
    private static Map<User, List<Status>> userStatuses;
    private static Map<User, List<Status>> allFeedStatuses;
    private static Map<String, String> defaultLoginUser;
    private static User defaultUser;
    private static User loggedInUser;
    private static User clickedUser = null;
    private List<Follow> follows;
    private static final String SERVER_URL = "https://uudfhyclx6.execute-api.us-west-2.amazonaws.com/dev";
    private final ClientCommunicator clientCommunicator = new ClientCommunicator(SERVER_URL);
    private static int followerSize = -1;
    private static int followingSize = -1;

    public ServerFacade(){
        if (defaultLoginUser == null){
            defaultLoginUser = new HashMap<>();
            defaultLoginUser.put("@TestUser", "password");
            defaultUser = new User("Test", "User", "@TestUser",
                    "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
            loggedInUser = defaultUser;
        }
    }

    /**
     * Performs a login and if successful, returns the logged in user and an auth token. The current
     * implementation is hard-coded to return a dummy user and doesn't actually make a network
     * request.
     *
     * @param request contains all information needed to perform a login.
     * @return the login response.
     */
    public LoginResponse login(LoginRequest request, String urlPath) throws IOException, TweeterRemoteException{

        LoginResponse loginResponse = clientCommunicator.doPost(urlPath, request, null, LoginResponse.class);

        if(loginResponse.isSuccess()){
            loggedInUser = loginResponse.getUser();

            return loginResponse;
        }
        else{
            throw new RuntimeException(loginResponse.getMessage());
        }
        /*if(allFeedStatuses == null){
            allFeedStatuses = initializeFeed();
        }

        ensureFollow();

        String alias = request.getUsername();
        String password = defaultLoginUser.get(alias);

        if(password == null){
            return new LoginResponse("Username or password is incorrect");
        }

        else if(password.equals(request.getPassword())){
            loggedInUser = defaultUser;
            return new LoginResponse(loggedInUser, new AuthToken());
        }
        else{
            return new LoginResponse("Username or password is incorrect");
        }*/
    }

    public RegisterResponse register(RegisterRequest request, String urlpath) throws IOException, TweeterRemoteException{
        RegisterResponse registerResponse = clientCommunicator.doPost(urlpath, request, null, RegisterResponse.class);

        if(registerResponse.isSuccess()) {
            loggedInUser = registerResponse.getUser();
            return registerResponse;
        }
        else{
            throw new RuntimeException(registerResponse.getMessage());
        }

        /*if(allFeedStatuses == null){
            allFeedStatuses = initializeFeed();
        }

        String alias = request.getUsername();
        List<String> allAliases = new ArrayList<>();
        boolean duplicateAlias = false;

        for(User user : allFeedStatuses.keySet()){
            allAliases.add(user.getAlias());
        }

        for(String s : allAliases){
            if(s.equals(alias)){
                duplicateAlias = true;
            }
        }

        if(!duplicateAlias){
            User registerUser = new User(request.getFirstName(), request.getLastName(), alias, request.getImageUrl());
            followeesByFollower.put(registerUser, new ArrayList<>());
            followersByFollowee.put(registerUser, new ArrayList<>());
            userStatuses.put(registerUser, new ArrayList<>());
            allFeedStatuses.put(registerUser, new ArrayList<>());
            loggedInUser = registerUser;

            return new RegisterResponse(loggedInUser, new AuthToken());
        }
        else{
            return new RegisterResponse("Alias taken. Please enter a new one");
        }*/

    }

    public LogoutResponse logout(LogoutRequest request, String urlpath) throws IOException, TweeterRemoteException{
        LogoutResponse logoutResponse = clientCommunicator.doPost(urlpath, request, null, LogoutResponse.class);

        if(logoutResponse.isSuccess()){
            loggedInUser = null;
            return logoutResponse;
        }
        else{
            throw new RuntimeException(logoutResponse.getMessage());
        }

        /*defaultLoginUser = null;
        defaultUser = null;
        loggedInUser = null;
        LogoutResponse response = new LogoutResponse();
        return response;*/
    }

    public TweetResponse tweet(TweetRequest request, String urlpath) throws IOException, TweeterRemoteException{

        TweetResponse tweetResponse = clientCommunicator.doPost(urlpath, request, null, TweetResponse.class);

        if(tweetResponse.isSuccess()){
            return tweetResponse;
        }
        else{
            throw new RuntimeException(tweetResponse.getMessage());
        }

        /*if(allFeedStatuses == null){
            allFeedStatuses = initializeFeed();
        }
        List<Status> statuses = userStatuses.get(request.getUser());

        Status status = new Status(request.getText(), request.getUser());
        statuses.add(status);

        List<User> followers = followersByFollowee.get(request.getUser());

        for(User follower : followers){
            List<Status> tmep = allFeedStatuses.get(follower);
            if(tmep == null){

            }
            else{
                allFeedStatuses.get(follower).add(status);
            }
        }

        return new TweetResponse();*/
    }

    public AliasClickResponse viewUserProfile(AliasClickRequest request, String urlpath) throws IOException, TweeterRemoteException{
        AliasClickResponse aliasClickResponse = clientCommunicator.doPost(urlpath, request, null, AliasClickResponse.class);

        if(aliasClickResponse.isSuccess()){
            clickedUser = aliasClickResponse.getAssociatedUser();
            return aliasClickResponse;
        }
        else{
            throw new RuntimeException(aliasClickResponse.getMessage());
        }

        /*boolean foundUser = false;

        for(User user : allFeedStatuses.keySet()){
            if(user.getAlias().equals(request.getAlias())){
                clickedUser = user;
                foundUser = true;
                break;
            }
        }

        if(!foundUser){
            clickedUser = null;
            return new AliasClickResponse("Can't find clicked user");
        }

        List<User> followees = followeesByFollower.get(loggedInUser);
        boolean isFollowing = followees.contains(clickedUser);

        return new AliasClickResponse(clickedUser, isFollowing);*/

    }

    public FollowStatusResponse changeFollowing(FollowStatusRequest request, String urlpath) throws IOException, TweeterRemoteException{

        FollowStatusResponse followStatusResponse = clientCommunicator.doPost(urlpath, request, null, FollowStatusResponse.class);

        if(followStatusResponse.isSuccess()){
            //defaultNum = defaultNum - 1;
            return followStatusResponse;
        }
        else{
            throw new RuntimeException(followStatusResponse.getMessage());
        }
        /*User myUser = request.getMyUser();
        User otherUser = request.getOtherUser();

        List<User> myFollowees = followeesByFollower.get(myUser);
        List<User> otherFollowers = followersByFollowee.get(otherUser);

        if(request.isFollowingStatus()){
            myFollowees.remove(otherUser);
            otherFollowers.remove(myUser);

            return new FollowStatusResponse(false);
        }

        myFollowees.add(otherUser);
        otherFollowers.add(myUser);

        return new FollowStatusResponse(true);*/
    }

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. The current implementation
     * returns generated data and doesn't actually make a network request.
     *
     * @param request contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the following response.
     */
    public FollowingResponse getFollowees(FollowingRequest request, String urlPath) throws IOException, TweeterRemoteException {

        FollowingResponse followingResponse = clientCommunicator.doPost(urlPath, request, null, FollowingResponse.class);

        if(followingResponse.isSuccess()){

            followingSize = followingResponse.getFollowees().size();
            return followingResponse;
        }
        else{
            throw new RuntimeException(followingResponse.getMessage());
        }

        // Used in place of assert statements because Android does not support them
        /*if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFollower() == null) {
                throw new AssertionError();
            }
        }

        if(followeesByFollower == null) {
            followeesByFollower = initializeFollowees();
        }

        List<User> allFollowees = followeesByFollower.get(request.getFollower());
        List<User> responseFollowees = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowees != null) {
                int followeesIndex = getFolloweesStartingIndex(request.getLastFollowee(), allFollowees);

                for(int limitCounter = 0; followeesIndex < allFollowees.size() && limitCounter < request.getLimit(); followeesIndex++, limitCounter++) {
                    responseFollowees.add(allFollowees.get(followeesIndex));
                }

                hasMorePages = followeesIndex < allFollowees.size();
            }
        }

        return new FollowingResponse(responseFollowees, hasMorePages);*/
    }

    public FollowerResponse getFollowers(FollowerRequest request, String urlpath) throws IOException, TweeterRemoteException{

        FollowerResponse followerResponse = clientCommunicator.doPost(urlpath, request, null, FollowerResponse.class);

        if(followerResponse.isSuccess()){

            followerSize = followerResponse.getFollowers().size();
            return followerResponse;
        }
        else{
            throw new RuntimeException(followerResponse.getMessage());
        }

        /*if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getFollowee() == null) {
                throw new AssertionError();
            }
        }

        if(followersByFollowee == null) {
            followersByFollowee = initializeFollowers();
        }

        List<User> allFollowers = followersByFollowee.get(request.getFollowee());
        List<User> responseFollowers = new ArrayList<>(request.getLimit());

        boolean hasMorePages = false;

        if(request.getLimit() > 0) {
            if (allFollowers != null) {
                int followersIndex = getFollowersStartingIndex(request.getLastFollower(), allFollowers);

                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
                    responseFollowers.add(allFollowers.get(followersIndex));
                }

                hasMorePages = followersIndex < allFollowers.size();
            }
        }

        return new FollowerResponse(responseFollowers, hasMorePages);*/
    }

    public StoryResponse getStory(StoryRequest request, String urlpath) throws IOException, TweeterRemoteException{

        StoryResponse storyResponse = clientCommunicator.doPost(urlpath, request, null, StoryResponse.class);

        if(storyResponse.isSuccess()){
            return storyResponse;
        }
        else{
            throw new RuntimeException(storyResponse.getMessage());
        }

        /*if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getAuthor() == null) {
                throw new AssertionError();
            }
        }

        if(userStatuses == null){
            userStatuses = initializeStories();
        }

        List<Status> allStatuses = userStatuses.get(request.getAuthor());
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        if(allStatuses != null){
            Collections.sort(allStatuses);
            Collections.reverse(allStatuses);
        }

        boolean hasMorePages = false;

        if(request.getLimit() > 0){
            int storyIndex = getStoryStartingIndex(request.getLastStatus(), allStatuses);

            for(int limitCounter = 0; storyIndex < allStatuses.size() && limitCounter < request.getLimit(); ++storyIndex, ++limitCounter){
                responseStatuses.add(allStatuses.get(storyIndex));
            }

            hasMorePages = storyIndex < allStatuses.size();
        }

        return new StoryResponse(responseStatuses, hasMorePages);*/
    }

    public FeedResponse getFeed(FeedRequest request, String urlpath) throws IOException, TweeterRemoteException{

        FeedResponse feedResponse = clientCommunicator.doPost(urlpath, request, null, FeedResponse.class);

        if(feedResponse.isSuccess()){
            return feedResponse;
        }
        else{
            throw new RuntimeException(feedResponse.getMessage());
        }

        /*if(BuildConfig.DEBUG) {
            if(request.getLimit() < 0) {
                throw new AssertionError();
            }

            if(request.getUser() == null) {
                throw new AssertionError();
            }
        }

        if(allFeedStatuses == null){
            allFeedStatuses = initializeFeed();
        }

        List<Status> allStatuses = allFeedStatuses.get(request.getUser());
        List<Status> responseStatuses = new ArrayList<>(request.getLimit());

        if(allStatuses != null){
            Collections.sort(allStatuses);
            Collections.reverse(allStatuses);
        }

        boolean hasMorePages = false;

        if(request.getLimit() > 0){
            int feedIndex = getFeedStartingIndex(request.getLastStatus(), allStatuses);

            for(int limitCounter = 0; feedIndex < allStatuses.size() && limitCounter < request.getLimit(); ++feedIndex, ++limitCounter){
                responseStatuses.add(allStatuses.get(feedIndex));
            }

            hasMorePages = feedIndex < allStatuses.size();
        }

        return new FeedResponse(responseStatuses, hasMorePages);*/

    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFollowee the last followee that was returned in the previous request or null if
     *                     there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(User lastFollowee, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFollowee != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFollowee.equals(allFollowees.get(i))) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                }
            }
        }

        return followeesIndex;
    }

    private int getFollowersStartingIndex(User lastFollower, List<User> allFollowers){
        int followersIndex = 0;

        if(lastFollower != null){
            for(int i = 0; i < allFollowers.size(); ++i){
                if(lastFollower.equals(allFollowers.get(i))){
                    followersIndex = i + 1;
                }
            }
        }

        return followersIndex;
    }

    private int getStoryStartingIndex(Status last, List<Status> allStatuses){
        int storyIndex = 0;

        if(last != null){
            return allStatuses.indexOf(last) + 1;
        }

        return storyIndex;
    }

    private int getFeedStartingIndex(Status last, List<Status> allStatuses){
        int feedIndex = 0;

        if(last != null){
            return allStatuses.indexOf(last) + 1;
        }

        return feedIndex;
    }

    /**
     * Generates the followee data.
     */
    private Map<User, List<User>> initializeFollowees() {

        Map<User, List<User>> followeesByFollower = new HashMap<>();

        follows = getFollowGenerator().generateUsersAndFollows(100,
                    0, 50, FollowGenerator.Sort.FOLLOWER_FOLLOWEE);

        // Populate a map of followees, keyed by follower so we can easily handle followee requests
        for(Follow follow : follows) {
            List<User> followees = followeesByFollower.get(follow.getFollower());

            if(followees == null) {
                followees = new ArrayList<>();
                followeesByFollower.put(follow.getFollower(), followees);
            }

            followees.add(follow.getFollowee());
        }

        return followeesByFollower;
    }

    // FIX FOLLOWERS CODE
    private Map<User, List<User>> initializeFollowers(){
        Map<User, List<User>> followersByFollowee = new HashMap<>();

        //follows = getFollowGenerator().generateUsersAndFollows(100,
                //0, 50, FollowGenerator.Sort.FOLLOWEE_FOLLOWER);

        // Populate a map of followees, keyed by follower so we can easily handle followee requests
        for(Follow follow : follows) {
            List<User> followers = followersByFollowee.get(follow.getFollower());

            if(followers == null) {
                followers = new ArrayList<>();
                followersByFollowee.put(follow.getFollower(), followers);
            }

            followers.add(follow.getFollowee());
        }

        return followersByFollowee;
    }

    private Map<User, List<Status>> initializeStories(){
        Map<User, List<Status>> userStatuses = new HashMap<>();
        if(followeesByFollower == null){
            followeesByFollower = initializeFollowees();
        }

        if(followersByFollowee == null){
            followersByFollowee = initializeFollowers();
        }

        List<Status> statuses = getStatusGenerator().generateStatuses(15, 30, followeesByFollower.keySet());

        for(Status status : statuses){
            List<Status> authorStatuses = userStatuses.get(status.getAuthor());

            if(authorStatuses == null){
                authorStatuses = new ArrayList<>();
                userStatuses.put(status.getAuthor(), authorStatuses);
            }

            authorStatuses.add(status);
        }

        return userStatuses;
    }

    private Map<User, List<Status>> initializeFeed(){
        Map<User, List<Status>> allFeedStatuses = new HashMap<>();
        if(userStatuses == null){
            userStatuses = initializeStories();
        }

        addLinks();

        for(User follower : followeesByFollower.keySet()){
            List<Status> feedStatuses = allFeedStatuses.get(follower);

            if(feedStatuses == null){
                feedStatuses = new ArrayList<>();
                allFeedStatuses.put(follower, feedStatuses);
            }

            List<User> followees = followeesByFollower.get(follower);

            if(followees != null){
                for(User followee : followees){
                    List<Status> followeeStatus = userStatuses.get(followee);

                    if(followeeStatus != null){
                        feedStatuses.addAll(followeeStatus);
                    }
                }
            }
        }

        return allFeedStatuses;
    }

    public void addLinks(){
        Random rand = new Random();
        int index;
        User tempUser;

        for(User user : userStatuses.keySet()){
            index = rand.nextInt(userStatuses.keySet().size());
            tempUser = (User) userStatuses.keySet().toArray()[index];
            List<Status> statuses = userStatuses.get(user);
            Status status = new Status(tempUser.getAlias() + " " + "https://www.google.com/", user);
            statuses.add(status);
        }
    }

    public void ensureFollow(){
        for(User user: followersByFollowee.keySet()){
            List<User> followers = followersByFollowee.get(user);
            boolean foundTestUser = false;
            if(followers != null){
                for(User u : followers){
                    if(u.getAlias().equals("@TestUser")){
                        foundTestUser = true;
                    }
                }
                if(!foundTestUser && (!user.getAlias().equals("@TestUser"))){
                    followers.add(defaultUser);
                }
            }
        }

        for(User user : followeesByFollower.keySet()){
            List<User> followees = followeesByFollower.get(user);
            boolean foundTestUser = false;
            if(followees != null){
                for(User u : followees){
                    if(u.getAlias().equals("@TestUser")){
                        foundTestUser = true;
                    }
                }
                if(!foundTestUser && (!user.getAlias().equals("@TestUser"))){
                    followees.add(defaultUser);
                }
            }
        }
    }
    /**
     * Returns an instance of FollowGenerator that can be used to generate Follow data. This is
     * written as a separate method to allow mocking of the generator.
     *
     * @return the generator.
     */
    FollowGenerator getFollowGenerator() {
        return FollowGenerator.getInstance();
    }

    StatusGenerator getStatusGenerator(){return StatusGenerator.getInstance();}

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public int getFollowersUser(){
        if(loggedInUser != null){
            return followerSize;
        }
        return 0;
    }

    public int getFolloweesUser(){
        if(loggedInUser != null){
            return followingSize;
        }
        return 0;
    }

    public int getFollowersClick(){
        if(clickedUser != null){
            return followerSize;
        }
        return 0;
    }

    public int getFolloweesClick(){
        if(clickedUser != null){
            return followingSize;
        }
        return 0;
    }
}
