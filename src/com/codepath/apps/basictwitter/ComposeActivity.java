package com.codepath.apps.basictwitter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ComposeActivity extends Activity {

    Button btnTweet;
    Button btnCancel;
    EditText etTweet;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        
        btnTweet = (Button) findViewById(R.id.btnTweet);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        etTweet = (EditText) findViewById(R.id.etTweet);
    }
    
    
    /**
     * Handler to send tweet message back to the twitter client activity
     * 
     * @param v Button that sends tweet
     */
    public void onTweet( View v ) {
        String tweetMsg = etTweet.getText().toString();
        
        Intent data = new Intent();
        data.putExtra("tweet", tweetMsg);
        
        // Activity finish, return data
        setResult(RESULT_OK, data);
        super.finish();
    }
    
    
    /**
     * Handler to stop tweet
     * 
     * @param v Button that cancels tweet
     */
    public void onCancel( View v ) {
        etTweet.clearComposingText();
        super.finish();
    }
    
}
