package edu.byu.cs.tweeter.model.service.request;

public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String imageUrl;

    public RegisterRequest(){}

    public RegisterRequest(String firstName, String lastName, String username, String password, String imageUrl){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
