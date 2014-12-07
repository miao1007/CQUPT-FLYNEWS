package com.github.miao1007.wordpressclient.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.api.CategoriesWithStatus;
import com.github.miao1007.wordpressclient.info.post.Category;
import com.github.miao1007.wordpressclient.ui.adapter.PagerAdapter;
import com.github.miao1007.wordpressclient.ui.fragment.FeedFragment;
import com.github.miao1007.wordpressclient.ui.fragment.NavigationDrawerFragment;
import com.github.miao1007.wordpressclient.ui.widget.SlidingTabLayout;
import com.github.miao1007.wordpressclient.utils.NetworkUtils;
import com.github.miao1007.wordpressclient.utils.UIutils;
import com.github.miao1007.wordpressclient.utils.WPcategoryInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.framework.ShareSDK;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.github.miao1007.wordpressclient.utils.LogUtils.LOGD;
import static com.github.miao1007.wordpressclient.utils.LogUtils.makeLogTag;

public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private String TAG = makeLogTag(HomeActivity.class);

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.slidingtab)
    SlidingTabLayout mTabLayout;

    @InjectView(R.id.viewpager)
    ViewPager mViewPager;

    @InjectView(R.id.drawer)
    DrawerLayout mDrawerLayout;

    ActionBarDrawerToggle mDrawerToggle;
    PagerAdapter mPagerAdapter;

    ArrayList<FeedFragment> fragments = new ArrayList<FeedFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        trySyncTab();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0://Login
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                if (mDrawerLayout != null) {
                    mDrawerLayout.closeDrawers();
                }
                break;
            case 1://Home
                if (mDrawerLayout != null) {
                    mDrawerLayout.closeDrawers();
                }
                break;
            case 2:
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case 3://Settings
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case 4://Feedback
                startActivity(new Intent(HomeActivity.this, FeedbackActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
        }
    }

    private void trySyncTab() {

        mToolbar.setTranslationY(-mToolbar.getHeight());

        mToolbar.animate()
                .translationY(0)
                .setDuration(300)
                .setStartDelay(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        LOGD(TAG, "updateCategories");
                        fragments.add(FeedFragment.newInstance("所有分类", "",""));
                        if (NetworkUtils.isNetworkAvailable(HomeActivity.this)){
                            new RestAdapter.Builder()
                                    .setEndpoint("http://162.243.252.57/api")
                                    .build()
                                    .create(WPcategoryInterface.class).getCategoryIndex("", new Callback<CategoriesWithStatus>() {
                                @Override
                                public void success(CategoriesWithStatus categoriesWithStatus, Response response) {
                                    List<Category> categories = categoriesWithStatus.getCategories();
                                    for (int i = 0; i < categories.size(); i++) {

                                        fragments.add(FeedFragment.newInstance(
                                                categories.get(i).getTitle(),
                                                categories.get(i).getSlug(),
                                                ""
                                        ));
                                    }

                                    mTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                                        @Override
                                        public int getIndicatorColor(int position) {
                                            return Color.WHITE;
                                        }
                                    });

                                    mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
                                    mViewPager.setOffscreenPageLimit(fragments.size());
                                    mViewPager.setAdapter(mPagerAdapter);
                                    mTabLayout.setViewPager(mViewPager);
                                    mTabLayout.setTranslationY(-mToolbar.getHeight() - mTabLayout.getHeight());
                                    mTabLayout.animate()
                                            .translationY(0)
                                            .setDuration(300)
                                            .setStartDelay(300);


                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    UIutils.disErr(HomeActivity.this, error);
                                }
                            });
                        } else {
                            UIutils.disMsg(HomeActivity.this,getString(R.string.networkerr));
                        }

                    }
                });


    }

}
