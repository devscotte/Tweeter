package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.server.service.LogoutServiceImpl;

public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {

    @Override
    public LogoutResponse handleRequest(LogoutRequest input, Context context) {
        if(input.getUser() == null){
            throw new RuntimeException("[BadRequest] invalid request info");
        }

        LogoutServiceImpl logoutService;

        try{
            logoutService = new LogoutServiceImpl();
        }
        catch (Exception e){
            throw new RuntimeException("[ServerError] something went wrong");
        }
        return logoutService.logout(input);
    }
}
