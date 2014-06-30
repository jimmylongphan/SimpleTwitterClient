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
public class MentionsTimelineFragment extends TweetsListFragment implements PopulateTimeline, TweetEndlessScrollListener{
    /**
     * 
     */
    public MentionsTimelineFragment() {
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
     * Makes the restful api call to twitter and processes the results
     * 
     */
    @Override
    public void populateTimeline(Long user_id, String screen_name) {
        client.getMentionsTimeline(new JsonHttpResponseHandler() { 
            @Override
            public void onSuccess(JSONArray json) {
                if ( json.length() > 0 ) {
                    //Log.d("debug", json.toString() );
                    addAll(Tweet.fromJSONArray(json));
                    client.setMentions_max_id(getMax_id());
                }
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
    
    
    /**
     * Implementing the interface defined in TweetsListFragment::TweetEndlessScrollListener
     */
    @Override
    public void onLoadMore(int totalItemsCount) {
        populateTimeline( null, null );
    }
    
}
