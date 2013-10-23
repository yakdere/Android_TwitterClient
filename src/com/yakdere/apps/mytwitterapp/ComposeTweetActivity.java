package com.yakdere.apps.mytwitterapp;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.yakdere.apps.mytwitterapp.models.Tweet;

public class ComposeTweetActivity extends Activity {
	EditText etTweet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_composetweet);
		etTweet = (EditText) findViewById(R.id.etComposeTweet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}
	//onPostTweet method is attached to Tweet Button with onClick attribute
	public void onPostTweet(View v) {
		String tweet_text = etTweet.getText().toString();
		
		// TODO add here check method of 140 character limits
		// TIP: https://dev.twitter.com/docs/counting-characters
		
		if (tweet_text.length() > 0) {
			TwitterClient client = MyTwitterApp.getRestClient();
			client.postTweet(tweet_text, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject jsonresponse) {
				Toast.makeText(getApplicationContext(), "tweeted!", Toast.LENGTH_SHORT).show();
				Log.d("DEBUG", "Tweet post success"+jsonresponse);
				//update timeline with new tweet
				Tweet new_tweet = Tweet.fromJson(jsonresponse);
				Intent i = new Intent();
				i.putExtra("new_composed_tweet", new_tweet);
				setResult(RESULT_OK, i);
				finish();	
			}
			public void onFailure(Throwable e, JSONObject errorResponse) {
				e.printStackTrace();
				Log.e("Error", e.toString());
				Toast.makeText(getApplicationContext(), "Tweet failed!", Toast.LENGTH_SHORT).show();
				finish();
			}
		});
		} else {
			Toast.makeText(getApplicationContext(), "No Entry!", Toast.LENGTH_SHORT).show();
		}
	}
	//cancel button clicked
	public void onCancelComposeTweet(View v) {
		Toast.makeText(getApplicationContext(), "Tweet Discarded", Toast.LENGTH_SHORT).show();
		finish();
	}

}
