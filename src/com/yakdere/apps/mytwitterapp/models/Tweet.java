package com.yakdere.apps.mytwitterapp.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
@Table(name = "Tweet")
public class Tweet extends Model implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7593286872344935825L;

	BaseModel base;

	@Column(name = "User")
	private User user;

	@Column(name = "Body")
	private String body;

	public Tweet() {
		super();
		base = new BaseModel();
	}

	public User getUser() {
		return user;
	}

	public String getBody() {
		if (body == null) {
			body = base.getString("text");
		}
		return body;
	}

	public boolean isFavorited() {
		return base.getBoolean("favorited");
	}

	public boolean isRetweeted() {
		return base.getBoolean("retweeted");
	}

	public String getCreatedAt() {
		return base.getString("created_at");        
	}
	/**
		@Override
		public void save() {
			if (user != null) {
				user.save();
			}
			super.save();
		} 
	 */

	public String toString() {
		return "body: " + body + " user: " + (user == null? "null" : user);
	}
	// Decodes array of tweet json results into tweet model objects
	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.base.jsonObject = jsonObject;
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet.getBody();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		for (int i=0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Tweet tweet = Tweet.fromJson(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}

		return tweets;
	}

	public static void overwriteTweetsLimited_25(ArrayList<Tweet> tweets) {
		//delete previous tweets first
		ArrayList<Tweet> allStoredTweets = new Select().all().from(Tweet.class).execute();
		if(allStoredTweets != null) {
			for(Tweet t : allStoredTweets){
				Tweet.delete(Tweet.class, t.getId());
			}                    
		}

		//store 25 tweets
		for(int i = 0; i < tweets.size() && i < 25; i++){
			Tweet t = tweets.get(i);
			User u = tweets.get(i).user;
			u.save();
			t.save();
		}
	}        

	public static void save(ArrayList<Tweet> tweets) {
		//store 25 tweets
		for(int i = 0; i < tweets.size() && i < 25; i++){
			Tweet t = tweets.get(i);
			User u = tweets.get(i).user;
			u.save();
			t.save();
		}

	}

	public static List<Tweet> recentTweets_25() {
		return new Select().from(Tweet.class).limit("25").execute();
	}
}
/**
	public JSONObject toJSONObject() {
		JSONObject jsonUser = new JSONObject();
		try { 
			jsonUser.put("name", this.user_name);
			jsonUser.put("screen_name", this.user_screenname);
			jsonUser.put("text", this.tweet_text);
			jsonUser.put("profile_image_url", this.pic_url);
		} catch (JSONException e) {
			e.printStackTrace();

		}
		return jsonUser;
	}
 */

