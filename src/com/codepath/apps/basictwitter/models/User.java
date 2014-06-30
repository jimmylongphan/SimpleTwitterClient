/**
 * 
 */
package com.codepath.apps.basictwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class User {

    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String profileBackgroundImageUrl;
    private int statusesCount;
    private int followersCount;
    private int friendsCount;
    private String description;


    // User.fromJSON(...)
    /**
     * Factory method to fill in the User object
     * 
     * @param jsonObject
     * @return
     */
    public static User fromJson(JSONObject jsonObject) {
        User u = new User();
        try { 
            u.name = jsonObject.getString("name");
            u.uid = jsonObject.getLong("id");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.profileBackgroundImageUrl = jsonObject.getString("profile_background_image_url");
            u.statusesCount = jsonObject.getInt("statuses_count");
            u.followersCount = jsonObject.getInt("followers_count");
            u.friendsCount = jsonObject.getInt("friends_count");
            u.description = jsonObject.getString("description");
        } catch ( JSONException e ) {
            e.printStackTrace();
            return null;
        }
        
        return u;
    }

    
    /**
     * 
     */
    public User() {
    }

    
    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
    /**
     * @return the profileBackgroundImageUrl
     */
    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }


    /**
     * @return the statusesCount
     */
    public int getStatusesCount() {
        return statusesCount;
    }


    /**
     * @return the followersCount
     */
    public int getFollowersCount() {
        return followersCount;
    }


    /**
     * @return the friendsCount
     */
    public int getFriendsCount() {
        return friendsCount;
    }


    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    public String getTagline() {
        return description;
    }
}
