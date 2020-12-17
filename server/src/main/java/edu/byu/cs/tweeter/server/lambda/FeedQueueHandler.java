package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.service.request.AllFeedRequest;
import edu.byu.cs.tweeter.server.service.TweetServiceImpl;

public class FeedQueueHandler implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent input, Context context) {

        TweetServiceImpl tweetService;

        try{
            tweetService = new TweetServiceImpl();
        }
        catch (Exception e){
            throw new RuntimeException("[ServerError] something went wrong");
        }

        System.out.println(input);
        for(SQSEvent.SQSMessage message : input.getRecords()){
            System.out.println(message.getBody());

            AllFeedRequest request = new Gson().fromJson(message.getBody(), AllFeedRequest.class);

            tweetService.allTweet(request);
        }

        return null;
    }
}
