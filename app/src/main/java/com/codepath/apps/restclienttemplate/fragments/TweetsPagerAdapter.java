package com.codepath.apps.restclienttemplate.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by kballard on 7/3/17.
 */

public class TweetsPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] {"Home", "Mentions"};
    private Context context;
    public TweetsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }
    private HomeTimelineFragment timelineFragment = new HomeTimelineFragment();
    private MentionsTimelineFragment mentionsTimelineFragment = new MentionsTimelineFragment();

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return timelineFragment;
            case 1:
                return mentionsTimelineFragment;
            default:
                return null;
        }
//
//        if(position == 0) {
//            return new HomeTimelineFragment();
//            //timelineFragment = getTimelineInstance();
//        }else if(position == 1){
//            return new MentionsTimelineFragment();
//        }else{
//            return null;
//        }
    }

//    public void getTimelineInstance(){
//        if(timelineFragment == null)
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
