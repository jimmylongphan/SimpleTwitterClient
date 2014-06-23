package com.codepath.apps.basictwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "Q6C5Ky3DQDbmgoCglDmgJd7GO";       // Change this
    public static final String REST_CONSUMER_SECRET = "5lSvjQYA7AIoWTXJKCtlnCsC215M3bcCJzEFtDItbwemEXKo2g"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)
    
    private Long count = (long) 5;
    private Long since_id = (long) 1;
    private Long max_id = Long.MIN_VALUE;
    
    private boolean firstCall = true;
    
    /**
     * @return the since_id
     */
    public Long getSince_id() {
        return since_id;
    }


    /**
     * Set to the greatest ID of ALL the tweets the application has already processed.
     * 
     * not inclusive, tweets returned will be higher
     * 
     * @param since_id the since_id to set
     */
    public void setSince_id(Long since_id) {
        if( since_id > this.since_id )
        {
            this.since_id = since_id;
        }
    }


    /**
     * @return the max_id
     */
    public Long getMax_id() {
        return max_id;
    }


    /**
     * First request to timeline should only specify a count.
     * Keep track of lowest ID received in a SINGLE response, then passed to next request.
     * Subtract 1 from lowest tweet ID.
     * 
     * inclusive, tweets returned will be lower
     * 
     * @param max_id the max_id to set
     */
    public void setMax_id(Long max_id) {
        this.max_id = max_id;
    }


    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    
    /**
     * 
     * @param handler
     */
    public void getHomeTimeline( AsyncHttpResponseHandler handler ) {
       String apiUrl = getApiUrl("statuses/home_timeline.json");
       RequestParams params = new RequestParams();
       
       //params.put("since_id", this.since_id.toString());  
       //params.put("max_id", this.max_id.toString());
       
       params.put("count", this.count.toString());
       if ( firstCall == false ) {
           params.put("max_id", max_id.toString() );
       }
       else {
           firstCall = false;
       }
       
       client.get(apiUrl, params, handler);  // if no params set, then just pass null
    }

    /**
     * 
     * @param aTweets
     */
    public void setMax_id(ArrayAdapter<Tweet> aTweets) {
        
        if ( aTweets.isEmpty() ) {
            return;
        }
        
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
        this.max_id = lowestMax;
    }

    
    /**
     * Method to send a tweet
     * 
     * @param tweetMsg message to tweet
     */
    public void tweet(String tweetMsg, AsyncHttpResponseHandler handler ) {
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        
        params.put("status", tweetMsg);
        client.post(apiUrl, params, handler ); // if no params set, then pass null
    }
    
    
    // CHANGE THIS
    // DEFINE METHODS for different API endpoints here
    /*
    public void getInterestingnessList(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }
    */
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}