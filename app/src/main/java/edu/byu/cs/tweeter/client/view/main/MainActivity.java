package edu.byu.cs.tweeter.client.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.AliasClickRequest;
import edu.byu.cs.tweeter.model.service.request.FollowStatusRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.AliasClickResponse;
import edu.byu.cs.tweeter.model.service.response.FollowStatusResponse;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.client.presenter.AliasClickPresenter;
import edu.byu.cs.tweeter.client.presenter.FollowStatusPresenter;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.presenter.TweetPresenter;
import edu.byu.cs.tweeter.client.view.LoginActivity;
import edu.byu.cs.tweeter.client.view.asyncTasks.AliasClickTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.FollowStatusTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.TweetTask;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements TweetPresenter.View, TweetTask.Observer, FeedFragment.FeedFragmentListener, AliasClickPresenter.View, AliasClickTask.Observer, LoginPresenter.View, FollowStatusPresenter.View, FollowStatusTask.Observer {

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    private PopupWindow popupWindow;
    private TweetPresenter tweetPresenter;
    private AliasClickPresenter aliasClickPresenter;
    private LoginPresenter loginPresenter;
    private FollowStatusPresenter followStatusPresenter;
    private User user;
    private User temp;
    private AuthToken authToken;
    private boolean following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tweetPresenter = new TweetPresenter(this);
        aliasClickPresenter = new AliasClickPresenter(this);
        loginPresenter = new LoginPresenter(this);
        followStatusPresenter = new FollowStatusPresenter(this);
        temp = loginPresenter.getLoginService().getLoggedInUser();

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);

        FloatingActionButton fab = findViewById(R.id.fab);

        // We should use a Java 8 lambda function for the listener (and all other listeners), but
        // they would be unfamiliar to many students who use this code.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View tweetView = layoutInflater.inflate(R.layout.fragment_tweet, (ViewGroup) findViewById(R.id.main_activity), false);

                popupWindow = new PopupWindow(tweetView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                popupWindow.showAsDropDown(findViewById(R.id.main_activity));
                Button closeTweetView = tweetView.findViewById(R.id.no_tweet);
                Button sendTweet = tweetView.findViewById(R.id.post_tweet);

                closeTweetView.setOnClickListener(cancelTheTweet);
                sendTweet.setOnClickListener(fullSendTweet);

            }
        });

        if(!user.equals(temp)){
            following = getIntent().getBooleanExtra("isFollowing", false);
            SmallerPagerAdapter smallerPagerAdapter = new SmallerPagerAdapter(this, getSupportFragmentManager(), user, authToken);
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(smallerPagerAdapter);

            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);

            TextView followFollowing = findViewById(R.id.logout_follow);
            if(!following){
                followFollowing.setText("Follow");
            }
            else{
                followFollowing.setText("Following");
            }
            followFollowing.setOnClickListener(toFollowOrNot);

            TextView followeeCount = findViewById(R.id.followeeCount);
            int followees = loginPresenter.getLoginService().getFolloweesClick();
            followeeCount.setText("Following: " + followees);

            TextView followerCount = findViewById(R.id.followerCount);
            int followers = loginPresenter.getLoginService().getFollowersClick();
            followerCount.setText("Followers: " + followers);

        }
        else{
            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken);
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);

            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);

            TextView logoutText = findViewById(R.id.logout_follow);
            logoutText.setText("Logout");
            logoutText.setOnClickListener(backToLogin);

            TextView followeeCount = findViewById(R.id.followeeCount);
            int followees = loginPresenter.getLoginService().getFolloweesUser();
            followeeCount.setText("Following: " + followees);

            TextView followerCount = findViewById(R.id.followerCount);
            int followers = loginPresenter.getLoginService().getFollowersUser();
            followerCount.setText("Followers: " + followers);
        }

        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

    }

    private View.OnClickListener cancelTheTweet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            popupWindow.dismiss();
        }
    };

    private View.OnClickListener fullSendTweet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EditText tweet = popupWindow.getContentView().findViewById(R.id.newStatus);
            TweetTask tweetTask = new TweetTask(tweetPresenter, MainActivity.this);
            TweetRequest tweetRequest = new TweetRequest(user, tweet.getText().toString());
            tweetTask.execute(tweetRequest);
        }
    };

    private View.OnClickListener backToLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener toFollowOrNot = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FollowStatusTask followStatusTask = new FollowStatusTask(followStatusPresenter, MainActivity.this);
            FollowStatusRequest followStatusRequest = new FollowStatusRequest(temp, user, following);
            followStatusTask.execute(followStatusRequest);
        }
    };

    @Override
    public void tweetRetrieved(TweetResponse response) {
        if(response.getMessage() == null || response.isSuccess()){
            popupWindow.dismiss();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.CURRENT_USER_KEY, user);
            intent.putExtra(MainActivity.AUTH_TOKEN_KEY, authToken);
            startActivity(intent);
        }
    }

    @Override
    public void aliasClickRetrieved(AliasClickResponse response) {
        if(response.getAssociatedUser() != null){
            following = response.isFollowing();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.CURRENT_USER_KEY, response.getAssociatedUser());
            intent.putExtra("isFollowing", following);
            startActivity(intent);
        }
    }

    @Override
    public void followStatusRetrieved(FollowStatusResponse response) {
        if(response.isSuccess()){
            TextView followFollowing = findViewById(R.id.logout_follow);
            if(response.isNewFollowStatus()){
                followFollowing.setText("Following");
            }
            else{
                followFollowing.setText("Follow");
            }

            TextView followerCount = findViewById(R.id.followerCount);
            int followers = loginPresenter.getLoginService().getFollowersClick();
            followerCount.setText("Followers: " + followers);
        }
    }

    @Override
    public void handleException(Exception exception) {
        if(exception != null){
            exception.printStackTrace();
        }
    }

    @Override
    public void aliasClick(AliasClickRequest request) {
        new AliasClickTask(aliasClickPresenter, MainActivity.this).execute(request);
    }


}