package edu.byu.cs.tweeter.model.service.request;

public class AliasClickRequest {

    private String alias;

    private String followerAlias;

    public AliasClickRequest(){}

    public AliasClickRequest(String alias){
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }
}
