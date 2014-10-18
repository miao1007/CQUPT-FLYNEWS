package com.github.miao1007.wordpressclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.adapter.MenuAdapter;
import com.github.miao1007.wordpressclient.fragment.CategoryFragment;
import com.github.miao1007.wordpressclient.fragment.CommitFragmnet;
import com.github.miao1007.wordpressclient.fragment.SettingFragment;
import com.github.miao1007.wordpressclient.info.post.Category;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.utils.NetworkUtils;
import com.github.miao1007.wordpressclient.utils.WordPressUtils;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeActivity extends Activity implements MenuDrawer.OnDrawerStateChangeListener, AdapterView.OnItemClickListener {

    public MenuDrawer mMenuDrawer;
    protected MenuAdapter mAdapter;
    protected ListView mListView;
    protected List<Category> categories = new ArrayList<Category>();
    protected HashMap<String, String> avatarandname = new HashMap<String, String>();
    String currentTAG = Model.FRAGMENT_HOME;

    private static final int INCLUDE_VIEW_COUNT = 5;
    private ImageView imageView;
    private TextView textView;

    //notice : the first is a Category , and it is not clickable
    private int mActivePosition = 1;

    private static final String STATE_CURRENT_POSITION = "STATE_CURRENT_POSITION";
    private int mSavedPosition;

    Handler handler;

    //Fragment
    FragmentManager manager;
    FragmentTransaction transaction;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_POSITION, mSavedPosition);
    }

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        //实例化菜单View和内容页，注意内容页只是一个Frame容器，用于替换Fragment
        View menuView = LayoutInflater.from(this).inflate(R.layout.slidingmenu_list, null);
        View contentView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.container_frame, null);
        //菜单view实际上就是一个listview
        mListView = (ListView) menuView.findViewById(R.id.slidingmenu_content_listview);
        mAdapter = new MenuAdapter(this, categories, avatarandname);
        mListView.setAdapter(mAdapter);
        //这个监听用于替换fragment
        mListView.setOnItemClickListener(this);
        //初始化MenuDrawer，可以通过API和英文字面上了解意义
        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setOnDrawerStateChangeListener(this);
        mMenuDrawer.setSlideDrawable(R.drawable.ic_drawer);
        mMenuDrawer.setDrawerIndicatorEnabled(true);
        mMenuDrawer.setMenuView(menuView);
        mMenuDrawer.setContentView(contentView);
        //同步主界面文章
        syncPosts();
        //同步菜单页的文章分类，从服务器抓取
        syncMenuItems();
    }

    //Download Menu Item List from server
    public void syncMenuItems() {
        new AsyncTask<Void, Void, List<Category>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (!NetworkUtils.isNetworkAvailable(HomeActivity.this)) {
                    this.cancel(true);
                }
            }

            @Override
            protected List<Category> doInBackground(Void... voids) {
                return WordPressUtils.getCategoryIndex("").getCategories();
            }

            @Override
            protected void onPostExecute(List<Category> tmpcategories) {
                super.onPostExecute(tmpcategories);
                categories.addAll(tmpcategories);
                SharedPreferences preferences = getSharedPreferences(Model.SHARE_SDK, 0);
                String name = preferences.getString(Model.USER_NAME, getString(R.string.user_login));
                String avatar = preferences.getString(Model.USER_AVATAR, getString(R.string.defaultavatar_link));
                avatarandname.put(Model.USER_NAME, name);
                avatarandname.put(Model.USER_AVATAR, avatar);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    public void syncPosts() {
        getFragmentManager().beginTransaction().replace(mMenuDrawer.getContentContainer().getId(), new CategoryFragment(), Model.FRAGMENT_HOME).commit();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        System.out.println("currentTAG = " + currentTAG);
        if (i < INCLUDE_VIEW_COUNT) {
            switch (i) {
                //login
                case 0:
//                    LoginFragment loginFragment = new LoginFragment();
//                    loginFragment.setListener(this);
//                    transaction.replace(mMenuDrawer.getContentContainer().getId(), loginFragment, "login");
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 3);
                    currentTAG = Model.FRAGMENT_LOGIN;
                    break;
                //case 1:break;
                //home
                case 1:
                    changeFragment(Model.FRAGMENT_HOME, null);
                    break;
                //search
                case 2:
                    final EditText editText = new EditText(this);
                    new AlertDialog.Builder(this).setTitle("请输入关键词").setIcon(R.drawable.ic_menu_search)
                            .setView(editText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    changeFragment(Model.FRAGMENT_SEARCH, editText.getText().toString());
                                }
                            })
                            .setNegativeButton("取消", null).show();

                    break;
                //settings
                case 3:
                    changeFragment(Model.FRAGMENT_SETTINGS, null);
                    break;
                //commit
                case 4:
                    changeFragment(Model.FRAGMENT_COMMIT, null);
                    break;

            }
        } else {
            changeFragment(Model.FRAGMENT_CATEGORY, categories.get(i-5).getTitle());
        }

        mMenuDrawer.closeMenu();
        System.out.println("currentTAG(After commit) = " + currentTAG);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                final int drawerState = mMenuDrawer.getDrawerState();
                if (drawerState == MenuDrawer.STATE_OPEN
                        || drawerState == MenuDrawer.STATE_OPENING) {
                    mMenuDrawer.closeMenu();
                } else {
                    mMenuDrawer.openMenu();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //MenuDrawer : setOnDrawerStateChangeListener
    @Override
    public void onDrawerStateChange(int oldState, int newState) {
        if (newState == MenuDrawer.STATE_CLOSED) {
            //commitTransactions();
        }
    }

    @Override
    public void onDrawerSlide(float openRatio, int offsetPixels) {
        // Do nothing
    }

    //one more click for quit
    private boolean isReadllyQuiet = false;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode + resultCode = " + requestCode + " " + resultCode);
        //login request = 3 , result = 2
        if (requestCode == 3 && resultCode == 2) {
            String name = data.getStringExtra(Model.USER_NAME);
            String avatar = data.getStringExtra(Model.USER_AVATAR);
            System.out.println("onActivityResult = " + name + "/" + avatar);
            avatarandname.clear();
            avatarandname.put(Model.USER_NAME, name);
            avatarandname.put(Model.USER_AVATAR, avatar);
            mAdapter.notifyDataSetChanged();
        }
        //search request = 2 result =3
        else if (requestCode == 2 && resultCode == 3) {
            CategoryFragment fragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("query", data.getStringExtra("query"));
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(mMenuDrawer.getContentContainer().getId(), fragment).commit();
        }
    }

    void changeFragment(String tag, String argument) {
        Log.d("changeFragment", tag + " " + argument);
        Fragment fragment;
        if (tag.equals(Model.FRAGMENT_SETTINGS)) {
            fragment = new SettingFragment();
        } else if (tag.equals(Model.FRAGMENT_COMMIT)) {
            fragment = new CommitFragmnet();
        } else if (tag.equals(Model.FRAGMENT_SEARCH)) {
            Log.d("FRAGMENT_SEARCH", tag + " " + argument);
            fragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Model.FRAGMENT_SEARCH, argument);
            fragment.setArguments(bundle);

        } else if (tag.equals(Model.FRAGMENT_CATEGORY)) {
            Log.d("FRAGMENT_CATEGORY", tag + " " + argument);
            fragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Model.FRAGMENT_CATEGORY, argument);
            fragment.setArguments(bundle);
        } else {
            fragment = new CategoryFragment();
        }

        if (manager.findFragmentByTag(currentTAG) != null) {
            Log.d("manager.findFragmentByTag(currentTAG) != null", tag);
            System.out.println("currentTAG = " + currentTAG);
            transaction.detach(manager.findFragmentByTag(currentTAG))
                    .add(mMenuDrawer.getContentContainer().getId(), fragment, tag);

        } else {
            Log.d("manager.findFragmentByTag(currentTAG) == null", tag);
            transaction.replace(mMenuDrawer.getContentContainer().getId(), fragment, tag);
            transaction.addToBackStack(currentTAG);
        }
        transaction.commit();
        currentTAG = tag;
    }
}
