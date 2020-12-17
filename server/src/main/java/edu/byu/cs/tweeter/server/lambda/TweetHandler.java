package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.server.service.TweetServiceImpl;

public class TweetHandler implements RequestHandler<TweetRequest, TweetResponse> {
    @Override
    public TweetResponse handleRequest(TweetRequest input, Context context) {
        if(input == null){
            throw new RuntimeException("[BadRequest] invalid request info");
        }

        String queue_rl = "https://sqs.us-west-2.amazonaws.com/082779601441/PostsQ";
        TweetServiceImpl tweetService;

        try{
            tweetService = new TweetServiceImpl();
        }
        catch (Exception e){
            throw new RuntimeException("[ServerError] something went wrong");
        }

        TweetResponse response = tweetService.tweet(input);
        System.out.println("Tweet response: " + response.isSuccess());

        User user = new User(input.getUser().getFirstName(), input.getUser().getLastName(), input.getUser().getAlias(), input.getUser().getImageUrl());
        Status status = new Status(input.getText(), user);

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queue_rl)
                .withMessageBody(new Gson().toJson(status));
        AmazonSQS amazonSQS = AmazonSQSClientBuilder.defaultClient();
        SendMessageResult sendMessageResult = amazonSQS.sendMessage(sendMessageRequest);

        String messageID = sendMessageResult.getMessageId();
        System.out.println("Message ID: " + messageID);

        return response;

    }
}
