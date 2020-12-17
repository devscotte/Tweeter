package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;

public class StoryDAO {

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withRegion("us-west-2")
            .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);

    private static final String TABLE_NAME = "TweeterStory";

    private static final String ALIAS = "alias";
    private static final String TIME_STAMP = "time_stamp";
    private static final String CONTENT = "content";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String IMAGE_URL = "image_url";

    public StoryResponse getStory(StoryRequest request) {
        Map<String, String> attrNames = new HashMap<>();
        attrNames.put("#alias", ALIAS);

        Map<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(":alias", new AttributeValue().withS(request.getAuthor().getAlias()));

        QueryRequest queryRequest = new QueryRequest()
                .withTableName(TABLE_NAME)
                .withKeyConditionExpression("#alias = :alias")
                .withExpressionAttributeNames(attrNames)
                .withExpressionAttributeValues(attrValues)
                .withLimit(request.getLimit());

        if(request.getLastStatus() != null){
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(ALIAS, new AttributeValue().withS(request.getAuthor().getAlias()));
            startKey.put(TIME_STAMP, new AttributeValue().withN(String.valueOf(request.getLastStatus().getTimeStamp())));

            queryRequest = queryRequest.withExclusiveStartKey(startKey);
        }

        QueryResult queryResult = amazonDynamoDB.query(queryRequest);
        List<Map<String, AttributeValue>> items = queryResult.getItems();
        List<Status> statuses = new ArrayList<>();

        if(items != null){
            for(Map<String, AttributeValue> item : items){
                String alias = item.get(ALIAS).getS();
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

        Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
        boolean hasMorePages = false;

        if(lastKey != null){
            hasMorePages = true;
        }

        if(statuses != null){
            Collections.sort(statuses);
            Collections.reverse(statuses);
        }

        return new StoryResponse(statuses, hasMorePages);
    }

    public TweetResponse tweet(TweetRequest request){
        Table table = dynamoDB.getTable(TABLE_NAME);

        //System.out.println(request.getText());
        //System.out.println(request.getUser());
        User user = request.getUser();
        String content = request.getText();

        Status status = new Status(content, user);

        Item item = new Item()
                .withPrimaryKey(ALIAS, user.getAlias())
                .withString(FIRST_NAME, user.getFirstName())
                .withString(LAST_NAME, user.getLastName())
                .withString(IMAGE_URL, user.getImageUrl())
                .withString(CONTENT, content)
                .withNumber(TIME_STAMP, status.getTimeStamp());

        table.putItem(item);

        return new TweetResponse();
    }
}
