package com.yakdere.apps.mytwitterapp;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yakdere.apps.mytwitterapp.models.Tweet;

public class TweetsAdapter extends ArrayAdapter<Tweet> {

	public TweetsAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		Tweet tweet = getItem(position);

		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);

		
		ImageLoader loader = ImageLoader.getInstance();
		//imageView.setImageResource(android.R.color.background_light); 10 doesn't support that feature
		loader.displayImage(tweet.getUser().getProfileImageUrl(), imageView);
		
		nameView.setText(Html.fromHtml("<b>" + tweet.getUser().getName()
				+ "<br>" + "<small><font color='#777777'>@"
				+tweet.getUser().getScreenName() + "</font></small>"));

		bodyView.setText(Html.fromHtml(tweet.getBody()));

		return view;

	}

}
