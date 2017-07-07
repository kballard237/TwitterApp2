package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.ReplyActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.id.ivProfileImage;

public class TweetDetailsActivity extends AppCompatActivity {

    TwitterClient client = TwitterApplication.getRestClient();
    Tweet tweet;
    TextView tvName;
    TextView tvScreenName;
    TextView tvBody;
    TextView tvRelativeTime;
    ImageButton retweet;
    ImageButton favorite;
    ImageButton reply;
    ImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);
        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet_info"));


        tvName = (TextView) findViewById(R.id.tvUsername2);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName2);
        tvBody = (TextView) findViewById(R.id.tvBody2);
        tvRelativeTime = (TextView) findViewById(R.id.tvRelativeTime);

        retweet = (ImageButton) findViewById(R.id.retweetButton);
        favorite = (ImageButton) findViewById(R.id.favoriteButton);
        reply = (ImageButton) findViewById(R.id.replyButton);
        ivProfile = (ImageView) findViewById(ivProfileImage);

        tvName.setText(tweet.user.name);
        tvScreenName.setText("@" + tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvRelativeTime.setText(tweet.relativeTime);

        Glide.with(this).load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(this, 25, 0))
                .into(ivProfile);

        if (tweet.favorited) {
            favorite.setImageResource(R.mipmap.favorite_pink);
        } else {
            favorite.setImageResource(R.mipmap.favorite_gray);
        }

        if (tweet.retweeted) {
            retweet.setImageResource(R.mipmap.retweet_green);
        } else {
            retweet.setImageResource(R.mipmap.retweet_gray);
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.favorite(!tweet.favorited, tweet.uid, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Change color of the heart
                        if (tweet.favorited) {
                            favorite.setImageResource(R.mipmap.favorite_pink);
                        } else {
                            favorite.setImageResource(R.mipmap.favorite_gray);
                        }

                        // Update model
                        tweet.setFavorited(!tweet.favorited);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("Favoriting", errorResponse.toString());
                    }
                });

            }
        });

        retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.retweet(!tweet.retweeted, tweet.uid, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        if (tweet.retweeted) {
                            retweet.setImageResource(R.mipmap.retweet_green);
                            //Toast.makeText(this, "unretweeted", Toast.LENGTH_SHORT).show();
                        } else {
                            retweet.setImageResource(R.mipmap.retweet_gray);
                            //Toast.makeText(this, "retweeted", Toast.LENGTH_SHORT).show();
                        }

                        tweet.retweeted = !tweet.retweeted;

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("Retweeting", errorResponse.toString());
                    }
                });

            }
        });
    }

    public void onReplyClick(View view) {
        Intent intent = new Intent(this, ReplyActivity.class);
        intent.putExtra("tweet_info", Parcels.wrap(tweet));
        startActivity(intent);
    }

}
