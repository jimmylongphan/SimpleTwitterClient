/**
 * 
 */
package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.EndlessScrollListener;
import com.codepath.apps.basictwitter.ProfileActivity;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetArrayAdapter;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;

/**
 *
 */
public class TweetsListFragment extends Fragment {
    protected ArrayList<Tweet> tweets;
    protected TweetArrayAdapter aTweets;
    protected ListView lvTweets;
    protected TweetEndlessScrollListener tweetEndlessScrollListener;
    protected TwitterClient client;
    protected UserProfileCall userProfileCall;
    
    /**
     * All fragments must implement this populate timeline method
     *
     */
    public interface PopulateTimeline {
        public void populateTimeline( Long user_id, String screen_name ); 
    }

    
    /**
     * Activity must define this interface.
     * In this Fragment, we're callin the method from this interface.
     * 
     * The TimelineActivity will implement this interface and allows the fragment
     * to communicate events to the Activity.
     *
     */
    public interface TweetEndlessScrollListener {
        public void onLoadMore( int totalItemsCount );
    }
    
    /**
     * Activity must define this interface.
     * In this fragment, we want to get information on a user.
     *
     */
    public interface UserProfileCall {
        public void getUserProfile(Long user_id, String screen_name);
    }
    
    /**
     * View logic
     * View related code goes here
     */
    public TweetsListFragment() {
    }
    

    /**
     * @return the client
     */
    public TwitterClient getClient() {
        return client;
    }
    
    
    /**
     * Fires before
     * Non view logic
     * Data structures go here
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweets = new ArrayList<Tweet>();
        aTweets = new TweetArrayAdapter(getActivity(), tweets);  // only minimally call getActivity()
        
        // notify the fragment that it's menu items should be loaded
        setHasOptionsMenu(true);
        
        client = TwitterApplication.getRestClient();
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        
        // Inflate the layout
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        
        // Assign our view referencces
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        
        
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                
                tweetEndlessScrollListener.onLoadMore(totalItemsCount);
                //customLoadMoreDataFromApi(); 
            }
        });
        
        // Only handle if the view clicked is an image view
        /*
        lvTweets.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                
                FragmentActivity fa = getActivity();
                if ( fa instanceof ProfileActivity ) {
                    // dont do anything if we are already inside profile
                    return;
                }
                
                Tweet tweet = aTweets.getItem(position);
                User user = tweet.getUser();
                final Long user_id = user.getUid();
                final String screen_name = user.getScreenName();
                ImageView ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
                
                String debugStr = String.format("lvTweets: user_id: %s,  screen_name: %s", user_id.toString(), screen_name);
                Log.d("DEBUG", debugStr);
                
                ivProfileImage.setOnClickListener( new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userProfileCall.getUserProfile(user_id, screen_name);
                    }
                });
            }
        });
        */
        
        // Return the layout view
        return v;
    }


    /**
     * Add actions to the menu bar in time line
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    
    /**
     * Store the UserProfile interface from the activities
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if ( activity instanceof UserProfileCall ) {
            userProfileCall = (UserProfileCall) activity;
        }
    }
    
    
    /**
     * Delegate adding to the internal adapter
     * 
     * @param tweets
     */
    public void addAll( ArrayList<Tweet> tweets) {
        aTweets.addAll(tweets);
    }
    
    /**
     * Method to get the lowest value from the current list of tweets
     * 
     * @return
     */
    public long getMax_id() {
        // initialize first max_id value
        Tweet tweet = aTweets.getItem(0);
        
        Long lowestMax = tweet.getUid();
        Long currentMax;
        for ( int i=1 ; i < aTweets.getCount(); i++ ) {
           tweet = aTweets.getItem(i);
           currentMax = tweet.getUid();
           if ( currentMax < lowestMax ) {
               lowestMax = currentMax;
           }
        }
        
        // decrease max_id by 1
        lowestMax--;
        return lowestMax;
    }
    
    /**
     * Method to insert tweet at position
     * 
     * @param tweet
     * @param position
     */
    public void insert( Tweet tweet, int position ) {
        aTweets.insert(tweet, position);
    }

}
