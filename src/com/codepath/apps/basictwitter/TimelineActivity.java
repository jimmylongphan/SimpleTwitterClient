package com.codepath.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends Activity {
    private static final int REQUEST_CODE = 10;
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private ArrayAdapter<Tweet> aTweets;
    private ListView lvTweets;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        populateTimeline();
        
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            
            @Override
            public void onLoadMore(int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                
                customLoadMoreDataFromApi(); 
            }
        });
        
    }
    
    /**
     * Add actions to the menu bar in timeline
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        
        return true;
    }
    
    // Append more data into the adapter
    public void customLoadMoreDataFromApi() {
        // This method probably sends out a network request and appends new data items to your adapter. 
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
        
        populateTimeline();
    }


    public void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() { 
            @Override
            public void onSuccess(JSONArray json) {
                //Log.d("debug", json.toString() );
                aTweets.addAll(Tweet.fromJSONArray(json));
                
                client.setMax_id(aTweets);
            }
            
            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("ERROR", e.toString() );
                Log.d("ERROR", s);
            }
        });
    }
    
    /**
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( resultCode == RESULT_OK && requestCode == REQUEST_CODE ) {
            String tweetMsg = data.getStringExtra("tweet");
            if ( tweetMsg != null && tweetMsg.isEmpty() == false ) {
                client.tweet( tweetMsg, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Tweet userTweet = Tweet.fromJson(jsonObject);
                        
                        // get the response and add it to the front of the listings
                        aTweets.insert(userTweet, 0);
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
    
}
