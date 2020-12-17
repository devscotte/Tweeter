package edu.byu.cs.tweeter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusGenerator {

    private static StatusGenerator instance;

    public static StatusGenerator getInstance() {
        if(instance == null){
            instance = new StatusGenerator();
        }
        return instance;
    }

    public List<Status> generateStatuses(int min, int max, Set<User> users){
        List<Status> statuses = new ArrayList<>();
        Random r = new Random();

        for(User thisUser : users){
            int statusCount = min + r.nextInt(max - min + 1);

            for(int i = 0; i < statusCount; ++i){
                statuses.add(new Status(generateText(), thisUser));
            }
        }

        return statuses;
    }

    public String generateText(){
        final int MAX_CHAR = 180;
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        int textLength = 0;

        while(textLength == 0){
            textLength = r.nextInt(MAX_CHAR);
        }

        for(int i = 0; i < textLength; ++i){
            sb.append((char)(r.nextInt(26) + 'a'));
        }

        return sb.toString();
    }

}
