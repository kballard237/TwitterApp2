package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.ReplyActivity;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by kballard on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    TwitterClient client;
    private List<Tweet> mTweets;
    Context context;
    private TweetAdapterListener mListener;

    public interface TweetAdapterListener {
        public void onItemSelected(View view, int position);

    }

    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        client = TwitterApplication.getRestClient();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final TweetAdapter.ViewHolder holder, final int position) {
        final Tweet tweet = mTweets.get(position);
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvScreenName.setText("@" + tweet.user.screenName); //added
        holder.tvRelativeTime.setText(tweet.relativeTime); //added

        Glide.with(context).load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .into(holder.ivProfileImage);

        if (tweet.favorited) {
            holder.favorite.setImageResource(R.mipmap.favorite_pink);
        } else {
            holder.favorite.setImageResource(R.mipmap.favorite_gray);
        }

        if (tweet.retweeted) {
            holder.retweet.setImageResource(R.mipmap.retweet_green);
        } else {
            holder.retweet.setImageResource(R.mipmap.retweet_gray);
        }

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.favorite(!tweet.favorited, tweet.uid, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        // Change color of the heart
                        if (tweet.favorited) {
                            holder.favorite.setImageResource(R.mipmap.favorite_pink);
                        } else {
                            holder.favorite.setImageResource(R.mipmap.favorite_gray);
                        }

                        // Update model
                        tweet.setFavorited(!tweet.favorited);

                        notifyItemChanged(position);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e("Favoriting", errorResponse.toString());
                    }
                });

            }
        });

        holder.retweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    client.retweet(!tweet.retweeted, tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            if (tweet.retweeted) {
                                holder.retweet.setImageResource(R.mipmap.retweet_green);
                                Toast.makeText(context, "unretweeted", Toast.LENGTH_SHORT).show();
                            } else {
                                holder.retweet.setImageResource(R.mipmap.retweet_gray);
                                Toast.makeText(context, "retweeted", Toast.LENGTH_SHORT).show();
                            }

                            tweet.retweeted = !tweet.retweeted;

                            notifyItemChanged(position);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.e("Retweeting", errorResponse.toString());
                        }
                    });

                }
        });

        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("screen_name", tweet.user.screenName);
                intent.putExtra("user_id", String.valueOf(tweet.user.uid));
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mTweets.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvScreenName; //added
        public TextView tvRelativeTime; //added
        public ImageButton reply;
        public ImageButton retweet;
        public ImageButton favorite;

        public ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername2);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody2);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName2); //added
            tvRelativeTime = (TextView) itemView.findViewById(R.id.tvRelativeTime); //added
            reply = (ImageButton) itemView.findViewById(R.id.replyButton);
            retweet = (ImageButton) itemView.findViewById(R.id.retweetButton);
            favorite = (ImageButton) itemView.findViewById(R.id.favoriteButton);


            reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tweet tweet = mTweets.get(getAdapterPosition());
                    Intent intent = new Intent(context, ReplyActivity.class);
                    intent.putExtra("tweet_info", Parcels.wrap(tweet));
                    context.startActivity(intent);
                    //fix this
                }
            });

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Tweet tweet = mTweets.get(position);
                // create intent for the new activity

                Intent intent = new Intent(context, TweetDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra("tweet_info", Parcels.wrap(tweet));
                // show the activity
                context.startActivity(intent);
            }
        }
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    public void onCancelReply(View view) {
        Intent intent = new Intent(context, TimelineActivity.class);
        context.startActivity(intent);
    }
}
