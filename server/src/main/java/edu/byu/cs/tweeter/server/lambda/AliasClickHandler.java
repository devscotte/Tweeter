package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;
import edu.byu.cs.tweeter.server.service.AliasClickServiceImpl;

public class AliasClickHandler implements RequestHandler<AliasClickRequest, AliasClickResponse>{

    @Override
    public AliasClickResponse handleRequest(AliasClickRequest input, Context context) {
        if(input.getAlias() == null){
            throw new RuntimeException("[BadRequest] no alias found");
        }

        AliasClickServiceImpl aliasClickService;

        try{
            aliasClickService = new AliasClickServiceImpl();
        }
        catch (Exception e){
            throw new RuntimeException("[ServerError] something went wrong");
        }

        return aliasClickService.viewUserProfile(input);
    }
}
