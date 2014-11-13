package com.github.miao1007.wordpressclient.ui.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.ui.fragment.BaseFragment;
import com.github.miao1007.wordpressclient.ui.fragment.NavigationDrawerFragment;

import cn.sharesdk.framework.ShareSDK;

public class HomeActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.activity_home);
        //initial view
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.inflateMenu(R.menu.home_menu);
            // Set Navigation Toggle
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0://Login
//                if (!getSharedPreferences("sharesdk",0).contains("avatar")){
//                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
//                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//                } else {
//                    UIutils.disMsg(this,getString(R.string.user_already_login));
//                }
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                if (mDrawerLayout != null){
                    mDrawerLayout.closeDrawers();
                }
                break;
            case 1://Home
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new BaseFragment())
                        .commit();
                if (mDrawerLayout != null){
                    mDrawerLayout.closeDrawers();
                }
                break;
            case 2:
                //TODO a search frag
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

}
