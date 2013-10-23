package com.yakdere.apps.mytwitterapp.models;

import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model {

	BaseModel base;

	@Column(name="Name")
	private String name;

	@Column(name="ScreenName")
	private String screenName;

	@Column(name="ProfileImageUrl")
	private String profileImageUrl;

	public User() {
		super();
		base = new BaseModel();
	}

	public String getName() {
		if (name == null) {
			name = base.getString("name");
		}
		return name;
	} 

	public String getScreenName() {
		if (screenName == null) {
			screenName = base.getString("screen_name");
		}
		return screenName;
	}

	public String getProfileBackgroundImageUrl() {
		return base.getString("profile_background_image_url");
	}

	public String getProfileImageUrl() {
		if (profileImageUrl ==  null) {
			profileImageUrl = base.getString("profile_image_url");
		}
		return profileImageUrl;
	}

	public int getNumTweets() {
		return base.getInt("statuses_count");
	}

	public int getFollowersCount() {
		return base.getInt("followers_count");
	}

	public int getFriendsCount() {
		return base.getInt("friends_count");
	}

	public String toString() {
		return "name: " + name + " screen name " + screenName + "profile url: " + profileImageUrl;
	}

	public static User fromJson(JSONObject json) {
		User u = new User();
		try {
			u.base.jsonObject = json;
			u.getProfileImageUrl();
			u.getScreenName();
			u.getName();
		} catch (Exception e) { 
			e.printStackTrace();
		}

		return u;
	}


}