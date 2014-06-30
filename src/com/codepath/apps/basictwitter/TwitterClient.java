package com.codepath.apps.basictwitter;

import java.util.HashMap;
import java.util.Map;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

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

    private Map<String, Long> screenNameUserTimelineMaxMap;

    private boolean mentionsFirstCall = true;
    private boolean homeFirstCall = true;
    private Long home_max_id = null;
    private Long mentions_max_id = null;
    
    
    /**
     * Constructor 
     * @param context
     */
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
        screenNameUserTimelineMaxMap = new HashMap<String, Long>();
    }
    
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

    public void setHome_max_id(long max_id) {
        this.home_max_id = max_id;
    }
    
    /**
     * @return the mentions_max_id
     */
    public Long getMentions_max_id() {
        return mentions_max_id;
    }


    /**
     * @param mentions_max_id the mentions_max_id to set
     */
    public void setMentions_max_id(Long mentions_max_id) {
        this.mentions_max_id = mentions_max_id;
    }


    /**
     * @return the user_timeline_max_id
     */
    public Long getUser_timeline_max_id( String screen_name ) {
        if (screenNameUserTimelineMaxMap.containsKey(screen_name) ) {
            return screenNameUserTimelineMaxMap.get(screen_name);
        }
        
        return null;
    }


    /**
     * First request to timeline should only specify a count.
     * Keep track of lowest ID received in a SINGLE response, then passed to next request.
     * Subtract 1 from lowest tweet ID.
     * 
     * inclusive, tweets returned will be lower
     * 
     * @param screen_name 
     * @param user_timeline_max_id the user_timeline_max_id to set
     */
    public void setUser_timeline_max_id(String screen_name, Long user_timeline_max_id) {
        screenNameUserTimelineMaxMap.put(screen_name, user_timeline_max_id);
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
    
    
    /**
     * API call to twitter to get home timeline
     * https://api.twitter.com/1.1/statuses/user_timeline.json
     * 
     * @param handler
     */
    public void getHomeTimeline( AsyncHttpResponseHandler handler ) {
       String apiUrl = getApiUrl("statuses/home_timeline.json");
       RequestParams params = new RequestParams();
              
       params.put("count", this.count.toString());
       if ( homeFirstCall == false && home_max_id != null ) {
           params.put("max_id", home_max_id.toString() );
       }
       else {
           homeFirstCall = false;
       }
       
       client.get(apiUrl, params, handler);  // if no params set, then just pass null
    }


    /**
     * API call to twitter to get mentions
     * 
     * @param handler
     */
    public void getMentionsTimeline( AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        RequestParams params = new RequestParams();
               
        params.put("count", this.count.toString());
        if ( mentionsFirstCall == false && mentions_max_id != null ) {
            params.put("max_id", mentions_max_id.toString() );
        }
        else {
            mentionsFirstCall = false;
        }
        
        client.get(apiUrl, params, handler);  // if no params set, then just pass null
    }
    

    /**
     * API call to get user info.
     * 
     * @param handler
     */
    public void getMyInfo( AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, null, handler);  // if no params set, then just pass null
    }
    
    /**
     * API call to get user info.
     * https://dev.twitter.com/docs/api/1.1/get/users/show.json
     * https://api.twitter.com/1.1/users/show.json
     * @param user_id 
     * @param handler
     */
    public void getUserInfo( Long user_id, String screen_name, 
            AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        
        params.put("user_id", user_id.toString());
        params.put("screen_name", screen_name);
        client.get(apiUrl, params, handler);  // if no params set, then just pass null
    }
    
    /**
     * API call to get user timeline
     * 
     * @param handler
     */
    public void getUserTimeline( Long user_id, String screen_name,
            AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
               
        params.put("count", this.count.toString());
        
        // getting info about the user
        if ( user_id != null ) {
            params.put("user_id", user_id.toString());
        }
        if ( screen_name != null && screen_name.isEmpty() == false ) {
            params.put("screen_name", screen_name);
        }
        
        if ( screenNameUserTimelineMaxMap.containsKey(screen_name) == true ) {
            params.put("max_id", screenNameUserTimelineMaxMap.get(screen_name).toString() );
        }
        
        client.get(apiUrl, params, handler);  // if no params set, then just pass null
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