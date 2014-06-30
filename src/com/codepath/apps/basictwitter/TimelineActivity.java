package com.codepath.apps.basictwitter;


import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.basictwitter.fragments.HometimelineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.fragments.TweetsListFragment;
import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity 
        implements TweetsListFragment.UserProfileCall  {
    private static final int REQUEST_CODE = 10;
    private final String HOME_TAB_TAG = "home";
    private final String MENTIONS_TAB_TAG = "mentions";
    
    TweetsListFragment tweetsListFragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //tweetsListFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
        setupTabs();
    }

    /**
     * 
     */
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // inflate the menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    
    /**
     * ActionBar Menu Item
     * Method to handle compose action
     * @param mi
     */
    public void onComposeAction(MenuItem mi) {
        Intent i = new Intent(getApplicationContext(), ComposeActivity.class);
        
        // invoke settings activity to get user settings
        startActivityForResult(i, REQUEST_CODE);
    }

    
    /**
     * Getting results from compose
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == RESULT_OK && requestCode == REQUEST_CODE ) {
            // Returns tab index currently selected
            int tabSelected = getActionBar().getSelectedNavigationIndex();
            if ( tabSelected == 0 ) {
                tweetsListFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentByTag(HOME_TAB_TAG);
            }
            else {
                tweetsListFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentByTag(HOME_TAB_TAG);
            }
            
            String tweetMsg = data.getStringExtra("tweet");
            if ( tweetMsg != null && tweetMsg.isEmpty() == false ) {
                tweetsListFragment.getClient().tweet( tweetMsg, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Tweet userTweet = Tweet.fromJson(jsonObject);
                        
                        // get the response and add it to the front of the listings
                        tweetsListFragment.insert(userTweet, 0);
                    }
                    
                    @Override
                    public void onFailure(Throwable e, String s) {
                        Log.d("ERROR", e.toString() );
                        Log.d("ERROR", s);
                    }
                    
                });
            }
        }
        
    }
    
    /**
     * ActionBar menu item
     * 
     * @param mi
     */
    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    
    /**
     * 
     */
    private void setupTabs() {
        // get action bar
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // tell action bar we want tabs
        actionBar.setDisplayShowTitleEnabled(true);

        Tab tab1 = actionBar
            .newTab()
            .setText("Home")
            .setIcon(R.drawable.ic_home)
            .setTag("HomeTimelineFragment")
            .setTabListener(
                new FragmentTabListener<HometimelineFragment>(R.id.flContainer, this, HOME_TAB_TAG,
                        HometimelineFragment.class));

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        Tab tab2 = actionBar
            .newTab()
            .setText("Mentions")
            .setIcon(R.drawable.ic_mentions)
            .setTag("MentionsTimelineFragment")
            .setTabListener(
                new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, MENTIONS_TAB_TAG,
                        MentionsTimelineFragment.class));

        actionBar.addTab(tab2);
    }

    /**
     * We can start get the user profile activity here
     */
    @Override
    public void getUserProfile(Long user_id, String screen_name) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user_id", user_id);
        i.putExtra("screen_name", screen_name);
        startActivity(i);
    }
}
