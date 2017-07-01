package com.codepath.apps.restclienttemplate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;
    TextView tvName;
    TextView tvScreenName;
    TextView tvBody;
    TextView tvRelativeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet_info"));


        tvName = (TextView) findViewById(R.id.tvUsername2);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName2);
        tvBody = (TextView) findViewById(R.id.tvBody2);
        tvRelativeTime = (TextView) findViewById(R.id.tvRelativeTime);

        tvName.setText(tweet.user.name);
        tvScreenName.setText(tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvRelativeTime.setText(tweet.relativeTime);


    }

}
