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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.attachment.AttachmentMarshaller;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.AllFeedRequest;
import edu.byu.cs.tweeter.model.service.request.FeedRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.FeedResponse;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class FeedDAO {

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static final String TABLE_NAME = "TweeterFeed";

    private static final String ALIAS = "alias";
    private static final String TIME_STAMP = "time_stamp";
    private static final String CONTENT = "content";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String TWEETER = "tweeter";
    private static final String IMAGE_URL = "image_url";

    public FeedResponse getFeed(FeedRequest request) {
        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#alias", ALIAS);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":alias", new AttributeValue().withS(request.getUser().getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditionExpression("#alias = :alias")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if(request.getLastStatus() != null){
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(ALIAS, new AttributeValue().withS(request.getUser().getAlias()));
            startKey.put(TIME_STAMP, new AttributeValue().withN(String.valueOf(request.getLastStatus().getTimeStamp())));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        List<Status> statuses = new ArrayList<>();

        if(items != null){
            for(Map<String, AttributeValue> item : items){
                String alias = item.get(TWEETER).getS();
                long timeStamp = Long.parseLong(item.get(TIME_STAMP).getN());
                String content = item.get(CONTENT).getS();
                String firstName = item.get(FIRST_NAME).getS();
                String lastName = item.get(LAST_NAME).getS();
                String imageUrl = item.get(IMAGE_URL).getS();

                User user = new User(firstName, lastName, alias, imageUrl);
                Status status = new Status(content, user);
                status.setTimeStamp(timeStamp);

                statuses.add(status);
            }
        }

        if(statuses.size() > 0){
            Collections.sort(statuses);
            Collections.reverse(statuses);
        }

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        boolean hasMorePages = false;

        if(lastKey != null){
            hasMorePages = true;
        }

        return new FeedResponse(statuses, hasMorePages);
    }


    public void allTweet(AllFeedRequest request){
        List<Item> items = new ArrayList<>();
        List<User> users = request.getFollowers();
        Status status = request.getStatus();
        User tempUser = status.getAuthor();

        for(User user : users){
            Item item = new Item()
                    .withPrimaryKey(ALIAS, user.getAlias(), TIME_STAMP, status.getTimeStamp())
                    .withString(CONTENT, status.getText())
                    .withString(FIRST_NAME, tempUser.getFirstName())
                    .withString(LAST_NAME, tempUser.getLastName())
                    .withString(TWEETER, tempUser.getAlias())
                    .withString(IMAGE_URL, tempUser.getImageUrl());

            items.add(item);
        }

        //Update feed for the user themself
        if(!request.isCheck()){
            Table table = dynamoDB.getTable(TABLE_NAME);

            Item item = new Item()
                    .withPrimaryKey(ALIAS, tempUser.getAlias())
                    .withString(FIRST_NAME, tempUser.getFirstName())
                    .withString(LAST_NAME, tempUser.getLastName())
                    .withString(IMAGE_URL, tempUser.getImageUrl())
                    .withString(CONTENT, status.getText())
                    .withString(TWEETER, tempUser.getAlias())
                    .withNumber(TIME_STAMP, status.getTimeStamp());

            table.putItem(item);
        }

        TableWriteItems tableWriteItems = new TableWriteItems(TABLE_NAME)
                .withItemsToPut(items);

        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);
    }


}
