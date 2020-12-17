package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.AliasClickService;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class AliasClickServiceImpl implements AliasClickService {
    @Override
    public AliasClickResponse viewUserProfile(AliasClickRequest request) {
        System.out.println(UserDAO.getLoggedInUser());
        return getUserDao().viewUserProfile(request);
    }

    UserDAO getUserDao(){
        return new UserDAO();
    }



}
