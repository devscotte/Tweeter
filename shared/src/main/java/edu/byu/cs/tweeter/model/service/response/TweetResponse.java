package edu.byu.cs.tweeter.model.service.response;


public class TweetResponse extends Response {

    public TweetResponse(){
        super(true, null);
    }

    public TweetResponse(String message){
        super(false, message);
    }


}
