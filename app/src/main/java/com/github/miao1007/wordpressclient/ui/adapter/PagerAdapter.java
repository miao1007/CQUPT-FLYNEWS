package com.github.miao1007.wordpressclient.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.miao1007.wordpressclient.ui.fragment.FeedFragment;
import com.github.miao1007.wordpressclient.utils.LogUtils;

import java.util.ArrayList;


/**
 * Created by leon on 11/30/14.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private String TAG = LogUtils.makeLogTag(PagerAdapter.class);

    private ArrayList<FeedFragment> fragments;


    public PagerAdapter(FragmentManager fm, ArrayList<FeedFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int pos) {
        return fragments.get(pos);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getArguments().getString("title");
    }


}
