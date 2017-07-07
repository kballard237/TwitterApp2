package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.TweetsPagerAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class TimelineActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    TweetsPagerAdapter pagerAdapter;
    private ViewPager viewPager;
//    private HomeTimelineFragment homeTimelineFragment;
//    private MentionsTimelineFragment mentionsTimelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.logo_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

       //removed swipe refresh layout

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        //vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager(), this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    private final int REQUEST = 20;

    public void onComposeAction(MenuItem mi) {
        // handle click here
        Intent intent = new Intent(this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == resultCode) {
            Tweet tweet = (Tweet) Parcels.unwrap(data.getParcelableExtra("tweet"));
            ((HomeTimelineFragment) pagerAdapter.getItem(viewPager.getCurrentItem())).addTweet(tweet);
            //tweets.add(0, tweet);
            //tweetAdapter.notifyItemInserted(0);
            //rvTweets.scrollToPosition(0);
        }

        // REQUEST_CODE is defined above

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void onProfileView(MenuItem item) {
        Intent intent = new Intent(this, ProfileActivity.class);
        //intent.putExtra("screen_name", );
        startActivity(intent);
    }

    //removed fetch timeline async

}
