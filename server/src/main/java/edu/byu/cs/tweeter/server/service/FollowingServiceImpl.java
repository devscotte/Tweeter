package edu.byu.cs.tweeter.server.service;


import edu.byu.cs.tweeter.model.service.FollowingService;
import edu.byu.cs.tweeter.model.service.request.FollowingRequest;
import edu.byu.cs.tweeter.model.service.response.FollowingResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

public class FollowingServiceImpl implements FollowingService {
    @Override
    public FollowingResponse getFollowees(FollowingRequest request) {
        return getFollowDAO().getFollowees(request);
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowingDAO class
     * for testing purposes. All usages of FollowingDAO should get their FollowingDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
