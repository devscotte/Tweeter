package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.AllFeedRequest;
import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.service.FollowerServiceImpl;

public class TweetQueueHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent input, Context context) {
        int LIMIT = 25;
        String queue_rl = "https://sqs.us-west-2.amazonaws.com/082779601441/TweetFeedQ";
        FollowerServiceImpl followerService;

        try{
            followerService = new FollowerServiceImpl();
        }
        catch (Exception e){
            throw new RuntimeException("[ServerError] something went wrong");
        }

        System.out.println(input);
        for(SQSEvent.SQSMessage message : input.getRecords()){
            System.out.println(message.getBody());

            Status status = new Gson().fromJson(message.getBody(), Status.class);

            User followee = status.getAuthor();
            System.out.println(followee);
            User lastFollower = null;
            boolean loop = true;
            System.out.println(status);

            while(loop){
                FollowerRequest followerRequest = new FollowerRequest(followee, LIMIT, lastFollower);
                FollowerResponse followerResponse = followerService.getFollowers(followerRequest);
                System.out.println(followerResponse);

                if(followerResponse == null || !followerResponse.isSuccess()){
                    throw new RuntimeException("[ServerError] something went wrong");
                }

                boolean check = followerResponse.getHasMorePages();

                AllFeedRequest allFeedRequest = new AllFeedRequest(followerResponse.getFollowers(), status, check);

                SendMessageRequest sendMessageRequest = new SendMessageRequest()
                        .withQueueUrl(queue_rl)
                        .withMessageBody(new Gson().toJson(allFeedRequest));
                AmazonSQS amazonSQS = AmazonSQSClientBuilder.defaultClient();
                SendMessageResult sendMessageResult = amazonSQS.sendMessage(sendMessageRequest);

                System.out.println("Message ID: " + sendMessageResult.getMessageId());

                if(!followerResponse.getHasMorePages()){
                    loop = false;
                    break;
                }
                try{
                    lastFollower = followerResponse.getFollowers().get(LIMIT - 1);
                }
                catch (Exception e){
                    e.printStackTrace();
                    loop = false;
                    break;
                }
            }
        }

        return null;
    }
}
