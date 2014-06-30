/**
 * 
 */
package com.codepath.apps.basictwitter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 *
 */
public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetArrayAdapter( Context context, List<Tweet> tweets ) {
        super( context, 0, tweets);
        
    }


    /**
     * 
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for position
        Tweet tweet = getItem(position);
        
        // Find or inflate the template
        View v;
        if ( convertView == null ) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.tweet_item, parent, false);
        }
        else {
            v = convertView;
        }
        // Find the views within template
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
        TextView tvRelativeTimeAgo = (TextView) v.findViewById(R.id.tvRelativeTimeAgo);
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        TextView tvTimestamp = (TextView) v.findViewById(R.id.tvTimestamp);
        
        ivProfileImage.setImageResource(android.R.color.transparent);
        ImageLoader imageLoader = ImageLoader.getInstance();
        // Populate views with tweet data
        imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        tvUserName.setText( " @" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvRelativeTimeAgo.setText(tweet.getRelativeTimestamp());
        tvName.setText(tweet.getUser().getName());
        tvTimestamp.setText(tweet.getCreatedAt());
        
        final User user = tweet.getUser();
        
        ivProfileImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //client.setUser_timeline_max_id( screen_name, getMax_id() );
                
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user_id", user.getUid() );
                i.putExtra("screen_name", user.getScreenName() );
                v.getContext().startActivity(i);
            }

        });
        return v;
    }

    
}
