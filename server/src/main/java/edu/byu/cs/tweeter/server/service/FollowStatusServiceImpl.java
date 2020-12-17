package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.FollowStatusService;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowStatusServiceImpl implements FollowStatusService {
    @Override
    public FollowStatusResponse changeFollowing(FollowStatusRequest request) {
        return getFollowDao().changeFollowing(request);
    }

    FollowDAO getFollowDao(){
        return new FollowDAO();
    }
}
