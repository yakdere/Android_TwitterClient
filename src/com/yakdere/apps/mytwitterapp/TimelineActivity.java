package com.yakdere.apps.mytwitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yakdere.apps.mytwitterapp.models.Tweet;

public class TimelineActivity extends Activity {
	ListView lvTweets;
	TweetsAdapter tAdapter;
	ArrayList<Tweet> tweets;
	RequestParams params;
	Boolean updateadapter, backtotop;
	//TODO getRelativeDateTimeString method for displayin relative times
	//TODO PullToRefresh library for automatic timeline refresh
	//TODO Timeline tweets are clickable, click, open and show options for retweet and favorite
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new Select().from(Tweet.class).execute();
		tAdapter = new TweetsAdapter(getBaseContext(), tweets);
		getTimeline();
		//attach endless scroll
		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				getMore();
			}
		});
		lvTweets.setAdapter(tAdapter);
	}
	private void getTimeline() {
		params = null;
		backtotop = false;
		updateadapter = false;
		clientRequest(params);
	}
	private void getMore() {
		if (tweets != null && !tweets.isEmpty()) {
			params = new RequestParams();
			params.put("max_id", String.valueOf(tweets.get(tweets.size() - 1).getId()));
		} else {
			params = null;
		}
		backtotop = false;
		updateadapter = true;
		clientRequest(params);
	}
	private void getRefresh() {
		if (tweets != null && !tweets.isEmpty()) {
			params = new RequestParams();
			params.put("since_id", String.valueOf(tweets.get(0).getId()));
		} else {
			params = null;
		}
		backtotop = true;
		updateadapter = true;
		clientRequest(params);
	}
		
	public void clientRequest(RequestParams params) {
		TwitterClient client = MyTwitterApp.getRestClient();
		client.getHomeTimeline(params, new JsonHttpResponseHandler(){
			
			public void onSuccess(JSONArray jsonTweets) {
				tweets.addAll(Tweet.fromJson(jsonTweets));
				tAdapter.notifyDataSetChanged();
				lvTweets.setAdapter(tAdapter);
				if(backtotop) {
					lvTweets.smoothScrollToPosition(0);
				} 
				if(updateadapter) {
					Tweet.overwriteTweetsLimited_25(tweets);
				} else {
					Tweet.save(tweets);
				}
			}
			public void onFailure(Throwable t, JSONArray arg1) {
				Toast.makeText(getBaseContext(), "Load Error", Toast.LENGTH_SHORT).show();
				tweets.clear();
				tweets.addAll(Tweet.recentTweets_25());
				lvTweets.smoothScrollToPosition(0);
			}
		}); 			
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			getRefresh();
			Toast.makeText(getBaseContext(), "Refreshed", Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_newTweet:
			Intent i = new Intent(getBaseContext(), ComposeTweetActivity.class);
			startActivityForResult(i, 0);
			break;

		default:
			break;
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 0) {
			Tweet tweet = (Tweet) data.getSerializableExtra("new_composed_tweet");
			//t.Adapter.add() method doesn't add new tweet as first row, Insert() method
			//support index, which is here 0
			tAdapter.insert(tweet, 0);
			//update it on the view
			lvTweets.setSelection(0);
			getTimeline();//bundan emin degilim
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

}
