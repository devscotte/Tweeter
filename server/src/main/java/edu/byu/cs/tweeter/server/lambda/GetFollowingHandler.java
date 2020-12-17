package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.server.service.FollowingServiceImpl;

public class GetFollowingHandler implements RequestHandler<FollowingRequest, FollowingResponse>{

    @Override
    public FollowingResponse handleRequest(FollowingRequest input, Context context) {
        if(input.getFollower() == null || input.getLimit() < 0){
            throw new RuntimeException("[BadRequest] invalid request info");
        }

        FollowingServiceImpl followingService;

        try{
            followingService = new FollowingServiceImpl();
        }
        catch (Exception e){
            throw new RuntimeException("[ServerError] something went wrong");
        }
        return followingService.getFollowees(input);
    }
}
