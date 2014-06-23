/**
 * 
 */
package com.codepath.apps.basictwitter.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;


/**
 *
 */
public class Tweet {
    private String body;
    private long uid;
    private String createdAt;
    private User user;
    private String relativeTimestamp;
    
    public static final String TWITTER_FORMAT = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    /**
     * Factory method to fill in Tweet from json
     * 
     * @param jsonObject
     * @return
     */
    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        
        // Extract values from the json to populate the member variables
        
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.relativeTimestamp = tweet.getRelativeTimeAgo(tweet.createdAt);
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e ) {
            e.printStackTrace();
            return null;
        }
        
        
        return tweet;
    }
    
    /**
     * 
     * @param jsonArray
     * @return
     */
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
        
        for ( int i=0; i < jsonArray.length(); i++ ) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch( Exception e ) {
                e.printStackTrace();
                continue;
            }
            
            Tweet tweet = Tweet.fromJson(tweetJson);
            if ( tweet != null ) {
                tweets.add(tweet);
            }
        }
        
        return tweets;
    }
    
    /**
     * 
     */
    public Tweet() {
    }

    
    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    public String getRelativeTimestamp() {
        return relativeTimestamp;
    }
    
    
    @Override
    public String toString() {
        return getBody() + " - " + getUser().getScreenName();
    }

 
    /**
     * Method to parse the created_at field from twitter.
     * 
     * getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
     * 
     * @param rawJsonDate
     * @return relativeDate of the tweet
     */
    public String getRelativeTimeAgo(String rawJsonDate) {
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER_FORMAT, Locale.ENGLISH);
        sf.setLenient(true);
     
        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
     
        return relativeDate;
    } 
}
