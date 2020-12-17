package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FollowerRequest;
import edu.byu.cs.tweeter.model.service.response.FollowerResponse;
import edu.byu.cs.tweeter.server.service.FollowerServiceImpl;

public class GetFollowerHandler implements RequestHandler<FollowerRequest, FollowerResponse> {
    @Override
    public FollowerResponse handleRequest(FollowerRequest input, Context context) {
        if(input.getFollowee() == null || input.getLimit() < 0){
            throw new RuntimeException("[BadRequest] invalid request info");
        }

        FollowerServiceImpl followerService;
        System.out.println(input.getFollowee());
        System.out.println(input.getLimit());

        try{
            followerService = new FollowerServiceImpl();
        }
        catch (Exception e){
            throw new RuntimeException("[ServerError] something went wrong");
        }
        return followerService.getFollowers(input);
    }
}
