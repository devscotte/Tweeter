package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;
import edu.byu.cs.tweeter.server.service.LoginServiceImpl;

public class LoginHandler implements RequestHandler<LoginRequest, LoginResponse> {

    @Override
    public LoginResponse handleRequest(LoginRequest input, Context context) {
        if(input.getUsername() == null || input.getPassword() == null){
            throw new RuntimeException("[BadRequest] request is incomplete");
        }

        LoginServiceImpl loginService;
        try{
            loginService = new LoginServiceImpl();
        }
        catch (Exception e){
            throw new RuntimeException("[ServerError] something went wrong");
        }
        return loginService.login(input);
    }
}
