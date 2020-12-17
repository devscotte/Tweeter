package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.service.TweetService;
import edu.byu.cs.tweeter.model.service.request.AllFeedRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.server.dao.FeedDAO;
import edu.byu.cs.tweeter.server.dao.StoryDAO;

public class TweetServiceImpl implements TweetService {
    @Override
    public TweetResponse tweet(TweetRequest request) {
        return getStoryDao().tweet(request);
    }

    public void allTweet(AllFeedRequest request){
        getFeedDao().allTweet(request);
    }

    StoryDAO getStoryDao(){
        return new StoryDAO();
    }
    FeedDAO getFeedDao(){return new FeedDAO();}
}
