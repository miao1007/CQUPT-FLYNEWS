package com.github.miao1007.wordpressclient.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.fragment.CategoryFragment;
import com.github.miao1007.wordpressclient.fragment.CommitFragmnet;
import com.github.miao1007.wordpressclient.fragment.SettingFragment;
import com.github.miao1007.wordpressclient.model.post.Category;
import com.github.miao1007.wordpressclient.slidingmenu.MenuAdapter;
import com.github.miao1007.wordpressclient.utils.WordPressUtils;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeActivity extends Activity implements MenuAdapter.MenuListener, MenuDrawer.OnDrawerStateChangeListener, AdapterView.OnItemClickListener {

    public MenuDrawer mMenuDrawer;
    protected MenuAdapter mAdapter;
    protected ListView mListView;
    protected List<Category> categories = new ArrayList<Category>();
    protected HashMap<String, String> avatarandname = new HashMap<String, String>();
    String currentTAG = "home";

    private ImageView imageView;
    private TextView textView;

    //notice : the first is a Category , and it is not clickable
    private int mActivePosition = 1;

    private static final String STATE_CURRENT_POSITION = "STATE_CURRENT_POSITION";
    private int mSavedPosition;

    Handler handler;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_POSITION, mSavedPosition);
    }

    @Override
    protected void onCreate(Bundle inState) {
        super.onCreate(inState);
        View menuView = LayoutInflater.from(this).inflate(R.layout.slidingmenu_list, null);
        View contentView = LayoutInflater.from(HomeActivity.this).inflate(R.layout.container_frame, null);
        mListView = (ListView) menuView.findViewById(R.id.slidingmenu_content_listview);


        mAdapter = new MenuAdapter(this, categories, avatarandname);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setOnDrawerStateChangeListener(this);
        mMenuDrawer.setSlideDrawable(R.drawable.ic_drawer);
        mMenuDrawer.setDrawerIndicatorEnabled(true);
        mMenuDrawer.setMenuView(menuView);
        mMenuDrawer.setContentView(contentView);
        mListView = (ListView) mMenuDrawer.getMenuView();
        syncMenuItems();
    }

    //Download Menu Item List from server
    public void syncMenuItems() {
        new AsyncTask<Void, Void, List<Category>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getFragmentManager().beginTransaction().replace(mMenuDrawer.getContentContainer().getId(),new CategoryFragment(),"home").commit();
            }

            @Override
            protected List<Category> doInBackground(Void... voids) {
                return WordPressUtils.getCategoryIndex("").getCategories();
            }

            @Override
            protected void onPostExecute(List<Category> tmpcategories) {
                super.onPostExecute(tmpcategories);
                categories.addAll(tmpcategories);
                SharedPreferences preferences = getSharedPreferences("sharesdk", 0);
                String name = preferences.getString("name", "请登陆");
                String avatar = preferences.getString("avatar", "http://www.iteye.com/images/user-logo-thumb.gif?1324994303");
                avatarandname.put("name", name);
                avatarandname.put("avatar", avatar);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        System.out.println("currentTAG = " + currentTAG);
        if (i < 5) {
            switch (i) {
                //login
                case 0:
//                    LoginFragment loginFragment = new LoginFragment();
//                    loginFragment.setListener(this);
//                    transaction.replace(mMenuDrawer.getContentContainer().getId(), loginFragment, "login");
                    Intent intent = new Intent(HomeActivity.this , LoginActivity.class);
                    startActivityForResult(intent,3);
                    currentTAG = "login";
                    break;
                //home
                case 1:
                    if (manager.findFragmentByTag(currentTAG) != null) {
                        if (currentTAG == "home"){
                            Log.i("onItemClick","same tag" + i);break;
                        } else {
                            transaction.detach(manager.findFragmentByTag(currentTAG))
                                    .add(mMenuDrawer.getContentContainer().getId(), new CategoryFragment(), "home");
                        }
                    } else {
                        transaction.replace(mMenuDrawer.getContentContainer().getId(), new CategoryFragment(), "home");
                        transaction.addToBackStack(currentTAG);
                    }
                    currentTAG = "home";
                    mMenuDrawer.closeMenu();
                    break;
                //search
                case 2:
                    intent = new Intent(HomeActivity.this , SearchActivity.class);
                    startActivityForResult(intent,2);
                    currentTAG = "search";
                    mMenuDrawer.closeMenu();
                    break;
                //settings
                case 3:
                    if (manager.findFragmentByTag(currentTAG) != null) {
                        if (currentTAG == "settings"){
                            Log.i("onItemClick","same tag");break;
                        } else {
                            transaction.detach(manager.findFragmentByTag(currentTAG))
                                    .add(mMenuDrawer.getContentContainer().getId(), new SettingFragment(), "settings");
                            transaction.addToBackStack(currentTAG);
                        }
                    } else {
                        transaction.replace(mMenuDrawer.getContentContainer().getId(), new SettingFragment(), "settings");
                        transaction.addToBackStack(currentTAG);
                    }
                    currentTAG = "settings";
                    mMenuDrawer.closeMenu();
                    break;
                //commit
                case 4:
                    if (manager.findFragmentByTag(currentTAG) != null) {
                        if (currentTAG == "commit"){
                            Log.i("onItemClick","same tag");break;
                        } else {
                        transaction.detach(manager.findFragmentByTag(currentTAG))
                                .add(mMenuDrawer.getContentContainer().getId(), new CommitFragmnet(), "commit");
                        transaction.addToBackStack(currentTAG);}
                    } else {
                        transaction.replace(mMenuDrawer.getContentContainer().getId(), new CommitFragmnet(), "commit");
                        transaction.addToBackStack(currentTAG);
                    }
                    currentTAG = "commit";
                    mMenuDrawer.closeMenu();
                    break;

            }
        } else {
            CategoryFragment fragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category", categories.get(i - 5).getTitle());
            fragment.setArguments(bundle);
            transaction.replace(mMenuDrawer.getContentContainer().getId(), fragment, "category");
            transaction.addToBackStack(currentTAG);
            getActionBar().setTitle(categories.get(i - 5).getTitle());
            currentTAG = "category";
            mMenuDrawer.closeMenu();
        }
        transaction.commit();
        System.out.println("currentTAG(After commit) = " + currentTAG);
    }

    @Override
    public void onActiveViewChanged(View v) {
        mMenuDrawer.setActiveView(v, mActivePosition);
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

//    @Override
//    public void onBackPressed() {
//        if (mMenuDrawer.isMenuVisible()) {
//            mMenuDrawer.toggleMenu();
//        } else {
//            if (!isReadllyQuiet) {
//                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
//                isReadllyQuiet = true;
//            } else {
//                super.onBackPressed();
//            }
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("requestCode + resultCode = " + requestCode +  " " + resultCode);
        //login request = 3 , result = 2
        if (requestCode == 3 && resultCode == 2){
            String name = data.getStringExtra("name");
            String avatar = data.getStringExtra("avatar");
            System.out.println("onActivityResult = " + name + "/" + avatar);
            avatarandname.clear();
            avatarandname.put("name",name);
            avatarandname.put("avatar",avatar);
            mAdapter.notifyDataSetChanged();
        }
        //search request = 2 result =3
        else if (requestCode == 2 && resultCode == 3){
            CategoryFragment fragment = new CategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putString("query",data.getStringExtra("query"));
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(mMenuDrawer.getContentContainer().getId(),fragment).commit();
        }
    }

}
