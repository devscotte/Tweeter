package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.RegisterRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.RegisterResponse;


public class UserDAO {

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static final String TABLE_NAME = "TweeterUsers";
    private static final String FOLLOW_TABLE = "TweeterFollow";
    private static final String ALIAS = "alias";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String IMAGE_URL = "image_url";
    private static final String PASSWORD = "password";
    private static final String SALT = "salt";

    private static final String FOLLOWER_ALIAS = "follower_handle";
    private static final String FOLLOWEE_ALIAS = "followee_handle";

    private static User loggedInUser;
    private static User aliasClickedUser;

    public LoginResponse login(LoginRequest request) {
        Table table = dynamoDB.getTable(TABLE_NAME);

        Item item = table.getItem(ALIAS, request.getUsername());
        if(item == null){
            return new LoginResponse("Username or password is incorrect");
        }

        String aliasRequest = request.getUsername();
        String passwordRequest = request.getPassword();

        String alias = item.getString(ALIAS);
        String firstName = item.getString(FIRST_NAME);
        String lastName = item.getString(LAST_NAME);
        String imageUrl = item.getString(IMAGE_URL);
        String password = item.getString(PASSWORD);
        String salt = item.getString(SALT);

        String correctPass = getSecurePassword(passwordRequest, salt);

        if(!correctPass.equals(password)){
            return new LoginResponse("Username or password is incorrect");
        }

        User user = new User(firstName, lastName, alias, imageUrl);
        user.setPassword(password);

        loggedInUser = user;
        return new LoginResponse(user, new AuthToken());
    }

    public RegisterResponse register(RegisterRequest request) {
        Table table = dynamoDB.getTable(TABLE_NAME);

        Item item = table.getItem(ALIAS, request.getUsername());

        if(item != null){
            return new RegisterResponse("Alias already taken");
        }

        String password = request.getPassword();

        String salt = getSalt();
        String newPassword = getSecurePassword(password, salt);

        request.setImageUrl("https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");

        Item itemNew = new Item().withPrimaryKey(ALIAS, request.getUsername())
                .withString(FIRST_NAME, request.getFirstName())
                .withString(LAST_NAME, request.getLastName())
                .withString(IMAGE_URL, request.getImageUrl())
                .withString(PASSWORD, newPassword)
                .withString(SALT, salt);

        table.putItem(itemNew);

        User user = new User(request.getFirstName(), request.getLastName(), request.getUsername(), request.getImageUrl());

        return new RegisterResponse(user, new AuthToken());

    }

    public void addUser(User user){
        Table table = dynamoDB.getTable(TABLE_NAME);

        String password = user.getPassword();

        String salt = getSalt();
        String newPassword = getSecurePassword(password, salt);

        Item itemNew = new Item().withPrimaryKey(ALIAS, user.getAlias())
                .withString(FIRST_NAME, user.getFirstName())
                .withString(LAST_NAME, user.getLastName())
                .withString(IMAGE_URL, user.getImageUrl())
                .withString(PASSWORD, newPassword)
                .withString(SALT, salt);

        table.putItem(itemNew);
    }

    public AliasClickResponse viewUserProfile(AliasClickRequest request) {
        Table table = dynamoDB.getTable(TABLE_NAME);
        System.out.println(table);

        System.out.println(getLoggedInUser());
        Item item = table.getItem(ALIAS, request.getAlias());
        System.out.println(item);

        if(item == null){
            return new AliasClickResponse("No user found at alias " + request.getAlias());
        }

        String alias = item.getString(ALIAS);
        String firstName = item.getString(FIRST_NAME);
        String lastName = item.getString(LAST_NAME);
        String imageUrl = item.getString(IMAGE_URL);

        User user = new User(firstName, lastName, alias, imageUrl);

        boolean isFollowing = checkTheFollow(request.getFollowerAlias(), alias);

        return new AliasClickResponse(user, isFollowing);

    }

    public boolean checkTheFollow(String follower, String following){
        FollowDAO followDAO = new FollowDAO();

        return followDAO.checkTheFollow(follower, following);

    }

    public LogoutResponse logout(LogoutRequest request){
        return new LogoutResponse();
    }

    public void addUserBatch(List<User> users){
        TableWriteItems items = new TableWriteItems(TABLE_NAME);

        for(User user : users){

            String password = user.getPassword();

            String salt = getSalt();
            String newPassword = getSecurePassword(password, salt);

            Item item = new Item()
                    .withPrimaryKey(ALIAS, user.getAlias())
                    .withString(FIRST_NAME, user.getFirstName())
                    .withString(LAST_NAME, user.getLastName())
                    .withString(IMAGE_URL, user.getImageUrl())
                    .withString(PASSWORD, newPassword)
                    .withString(SALT, salt);

            items.addItemToPut(item);

            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(TABLE_NAME);
            }
        }

        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);
        //logger.log("Wrote User Batch");

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
            //logger.log("Wrote more Users");
        }
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    private static String getSecurePassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH PASSWORD";
    }

    private static String getSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return "FAILED TO GET SALT";
    }

}
