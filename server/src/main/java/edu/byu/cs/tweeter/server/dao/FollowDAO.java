package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;

public class FollowDAO {

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static final String TABLE_NAME = "TweeterFollow";
    private static final String INDEX = "follows_index";

    private static final String FOLLOWER_ALIAS = "follower_handle";
    private static final String FOLLOWER_FNAME = "follower_first_name";
    private static final String FOLLOWER_LNAME = "follower_last_name";
    private static final String FOLLOWER_IMAGE_URL = "follower_image_url";

    private static final String FOLLOWEE_ALIAS = "followee_handle";
    private static final String FOLLOWEE_FNAME = "followee_first_name";
    private static final String FOLLOWEE_LNAME = "followee_last_name";
    private static final String FOLLOWEE_IMAGE_URL = "followee_image_url";

    public FollowingResponse getFollowees(FollowingRequest request) {
        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#follower", FOLLOWER_ALIAS);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":follower_handle", new AttributeValue().withS(request.getFollower().getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditionExpression("#follower = :follower_handle")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if(request.getLastFollowee() != null){
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FOLLOWER_ALIAS, new AttributeValue().withS(request.getFollower().getAlias()));
            startKey.put(FOLLOWEE_ALIAS, new AttributeValue().withS(request.getLastFollowee().getAlias()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        List<User> followees = new ArrayList<>();

        if(items != null){
            for(Map<String, AttributeValue> item : items){
                String alias = item.get(FOLLOWEE_ALIAS).getS();
                String firstName = item.get(FOLLOWEE_FNAME).getS();
                String lastName = item.get(FOLLOWEE_LNAME).getS();
                String imageUrl = item.get(FOLLOWEE_IMAGE_URL).getS();

                User user = new User(firstName, lastName, alias, imageUrl);

                followees.add(user);
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        boolean hasMorePages = false;

        if(lastKey != null){
            hasMorePages = true;
        }

        return new FollowingResponse(followees, hasMorePages);
    }

    public FollowerResponse getFollowers(FollowerRequest request) {
        System.out.println(request);
        System.out.println(request.getLimit());
        System.out.println(request.getFollowee());
        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#followee", FOLLOWEE_ALIAS);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":followee_handle", new AttributeValue().withS(request.getFollowee().getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withIndexName(INDEX)
                .withKeyConditionExpression("#followee = :followee_handle")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if(request.getLastFollower() != null){
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(FOLLOWEE_ALIAS, new AttributeValue().withS(request.getFollowee().getAlias()));
            startKey.put(FOLLOWER_ALIAS, new AttributeValue().withS(request.getLastFollower().getAlias()));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        List<User> followers = new ArrayList<>();

        if(items != null){
            for(Map<String, AttributeValue> item : items){
                String alias = item.get(FOLLOWER_ALIAS).getS();
                String firstName = item.get(FOLLOWER_FNAME).getS();
                String lastName = item.get(FOLLOWER_LNAME).getS();
                String imageUrl = item.get(FOLLOWER_IMAGE_URL).getS();

                User user = new User(firstName, lastName, alias, imageUrl);

                followers.add(user);
            }
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        boolean hasMorePages = false;

        if(lastKey != null){
            hasMorePages = true;
        }

        return new FollowerResponse(followers, hasMorePages);
    }

    public FollowStatusResponse changeFollowing(FollowStatusRequest request) {
        Table table = dynamoDB.getTable(TABLE_NAME);

        boolean isFollowing;

        if(request.isFollowingStatus()){
            isFollowing = false;

            table.deleteItem(FOLLOWER_ALIAS, request.getMyUser().getAlias(), FOLLOWEE_ALIAS, request.getOtherUser().getAlias());

            return new FollowStatusResponse(isFollowing);
        }
        isFollowing = true;

        Item item = new Item()
                .withPrimaryKey(FOLLOWER_ALIAS, request.getMyUser().getAlias(), FOLLOWEE_ALIAS, request.getOtherUser().getAlias())
                .withString(FOLLOWER_FNAME, request.getOtherUser().getFirstName())
                .withString(FOLLOWER_LNAME, request.getOtherUser().getLastName())
                .withString(FOLLOWER_IMAGE_URL, request.getOtherUser().getImageUrl())
                .withString(FOLLOWEE_FNAME, request.getMyUser().getFirstName())
                .withString(FOLLOWEE_LNAME, request.getMyUser().getFirstName())
                .withString(FOLLOWEE_IMAGE_URL, request.getMyUser().getImageUrl());

        table.putItem(item);

        return new FollowStatusResponse(isFollowing);
    }

    public boolean checkTheFollow(String follower, String following){
        Table table = dynamoDB.getTable(TABLE_NAME);

        Item item = table.getItem(FOLLOWER_ALIAS, follower, FOLLOWEE_ALIAS, following);

        if(item != null){
            return true;
        }

        return false;
    }

    public void addUserBatch(List<Follow> follows){
        TableWriteItems items = new TableWriteItems(TABLE_NAME);

        for(Follow follow : follows){
            Item item = new Item()
                    .withPrimaryKey(FOLLOWER_ALIAS, follow.getFollower().getAlias(), FOLLOWEE_ALIAS, follow.getFollowee().getAlias())
                    .withString(FOLLOWER_FNAME, follow.getFollower().getFirstName())
                    .withString(FOLLOWER_LNAME, follow.getFollower().getLastName())
                    .withString(FOLLOWER_IMAGE_URL, follow.getFollower().getImageUrl())
                    .withString(FOLLOWEE_FNAME, follow.getFollowee().getFirstName())
                    .withString(FOLLOWEE_LNAME, follow.getFollowee().getLastName())
                    .withString(FOLLOWEE_IMAGE_URL, follow.getFollowee().getImageUrl());

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


}
