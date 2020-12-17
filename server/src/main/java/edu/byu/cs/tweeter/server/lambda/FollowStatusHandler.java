package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;
import edu.byu.cs.tweeter.server.service.FollowStatusServiceImpl;

public class FollowStatusHandler implements RequestHandler<FollowStatusRequest, FollowStatusResponse> {
    @Override
    public FollowStatusResponse handleRequest(FollowStatusRequest input, Context context) {
        if(input.getMyUser() == null || input.getOtherUser() == null){
            throw new RuntimeException("[BadRequest] request incomplete");
        }

        FollowStatusServiceImpl followStatusService;

        try {
            followStatusService = new FollowStatusServiceImpl();
        }
        catch (Exception e){
            throw new RuntimeException("[ServerError] something went wrong");
        }
        return followStatusService.changeFollowing(input);
    }
}
