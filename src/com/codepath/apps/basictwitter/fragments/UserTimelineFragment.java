/**
 * 
 */
package com.codepath.apps.basictwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.basictwitter.fragments.TweetsListFragment.PopulateTimeline;
import com.codepath.apps.basictwitter.fragments.TweetsListFragment.TweetEndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

/**
 *
 */
public class UserTimelineFragment extends TweetsListFragment implements PopulateTimeline, TweetEndlessScrollListener {

    /**
     * 
     */
    public UserTimelineFragment() {
        // EXPLANATION
        // This hometimeline fragment defines the populate() method which is called from
        // listener
        // However, the since this fragment is a child class and an implementation
        // we set the endless scroll listener in this child class because it has the 
        // definition for the actions needed by endless scrolling.
        this.tweetEndlessScrollListener = this;
    }

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        populateTimeline( null, null );
    }

    /**
     * 
     */
    @Override
    public void populateTimeline( Long user_id, String screen_name ) {
        client.getUserTimeline( user_id, screen_name, new JsonHttpResponseHandler() { 
            @Override
            public void onSuccess(JSONArray json) {
                if ( json.length() > 0 ) {
                    //Log.d("debug", json.toString() );
                    addAll(Tweet.fromJSONArray(json));
                }
            }
            
            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("ERROR", e.toString() );
                Log.d("ERROR", s);
            }
        });
        
        if ( aTweets.getCount() > 0 ) {
            client.setUser_timeline_max_id( screen_name, getMax_id() );
        }
    }


    /**
     * 
     */
    @Override
    public void onLoadMore(int totalItemsCount) {
        populateTimeline( null, null );
    }
    
}
