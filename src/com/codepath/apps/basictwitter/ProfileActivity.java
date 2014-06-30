package com.codepath.apps.basictwitter;


import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.fragments.UserTimelineFragment;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ProfileActivity extends FragmentActivity {
    private static final int REQUEST_CODE = 10;
    protected TwitterClient client;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        client = TwitterApplication.getRestClient();
        loadProfileInfo();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    
    /**
     * Method to handle compose action
     * @param mi
     */
    public void onComposeAction(MenuItem mi) {
    }
    
    
    /**
     * 
     * @param mi
     */
    public void onProfileView(MenuItem mi) {
    }

    
    /**
     * Populate the profile header given information from twitter
     * @param u
     */
    protected void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        
        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFriendsCount() + " Following");

        // load the image
        ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
    }
    
    /**
     * Populate the timelines given the user information.
     * 
     * @param user
     */
    protected void populateProfileTimeline(User user) {
        Long user_id = user.getUid();
        String screen_name = user.getScreenName();
        
        String debugStr = String.format( 
                "loadProfileInfo: user_id: %s, screen_name: %s ", user_id.toString(), screen_name );
        Log.d("DEBUG", debugStr );
        
        UserTimelineFragment utf = 
                (UserTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentUserTimeline);
        utf.populateTimeline(user_id, screen_name);
    }
    
    
    /**
     * 
     */
    protected void loadProfileInfo() {
        String screen_name = getIntent().getStringExtra("screen_name");
        Long user_id = getIntent().getLongExtra("user_id", 0);
       
        
        if ( screen_name == null ) {
            TwitterApplication.getRestClient().getMyInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject json) {
                    User u = User.fromJson(json);
                    getActionBar().setTitle("@" + u.getScreenName());
                    
                    populateProfileHeader(u);
                    populateProfileTimeline(u);
                }
                
                @Override
                public void onFailure(Throwable e, String s) {
                    Log.d("ERROR", e.toString() );
                    Log.d("ERROR", s);
                }
                
                @Override
                protected void handleFailureMessage(Throwable e, String s) {
                    Log.d("ERROR", e.toString() );
                    Log.d("ERROR", s);
                }
                
            });
        }
        else {
            TwitterApplication.getRestClient().getUserInfo(user_id, screen_name, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(JSONObject json) {
                    User u = User.fromJson(json);
                    getActionBar().setTitle("@" + u.getScreenName());
                    populateProfileHeader(u);
                    populateProfileTimeline(u);
                }
                
                @Override
                public void onFailure(Throwable e, String s) {
                    Log.d("ERROR", e.toString() );
                    Log.d("ERROR", s);
                }
                
                @Override
                protected void handleFailureMessage(Throwable e, String s) {
                    Log.d("ERROR", e.toString() );
                    Log.d("ERROR", s);
                }
            });
        }

    }
}
