package edu.byu.cs.tweeter.server;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public class Filler {
    private final static int NUM_USERS = 10000;

    public static void fillDatabase(){
        UserDAO userDAO = new UserDAO();
        FollowDAO followDAO = new FollowDAO();

        List<Follow> follows = new ArrayList<>();
        List<User> users = new ArrayList<>();

        for(int i = 0; i < NUM_USERS; ++i){
            String alias = "@Person" + i;
            String firstName = "Person" + i;
            String lastName = i + "Person";
            String imageUrl = null;
            String password = "password";

            if(i % 2 == 0){
                imageUrl = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
            }
            else{
                imageUrl = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";
            }

            User user = new User(firstName, lastName, alias, imageUrl);
            user.setPassword(password);

            users.add(user);
        }

        User myUser = new User("Test", "User", "@TestUser", "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        myUser.setPassword("password");

        //Have everyone follow the test user
        for(User user : users){
            Follow follow = new Follow(user, myUser);
            follows.add(follow);
        }

        //Have test user follow a fifth of the users
        for(int i = 0; i < users.size() / 5; ++i){
            Follow follow = new Follow(myUser, users.get(i));
            follows.add(follow);
        }

        //Add user to the table
        userDAO.addUser(myUser);

        if(users.size() > 0){
            userDAO.addUserBatch(users);
        }

        if(follows.size() > 0){
            followDAO.addUserBatch(follows);
        }

    }

    public static void main(String[] args){
        Filler filler = new Filler();
        filler.fillDatabase();
    }
}
