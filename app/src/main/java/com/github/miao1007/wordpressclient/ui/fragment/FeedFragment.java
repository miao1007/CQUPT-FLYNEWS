package com.github.miao1007.wordpressclient.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.api.PostsWithStatus;
import com.github.miao1007.wordpressclient.info.post.Post;
import com.github.miao1007.wordpressclient.ui.adapter.FeedAdapter;
import com.github.miao1007.wordpressclient.utils.NetworkUtils;
import com.github.miao1007.wordpressclient.utils.UIutils;
import com.github.miao1007.wordpressclient.utils.WPpostInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.github.miao1007.wordpressclient.utils.LogUtils.LOGD;
import static com.github.miao1007.wordpressclient.utils.LogUtils.makeLogTag;


/**
 * Created by leon on 14/9/19.
 */

public class FeedFragment extends Fragment {


    String TAG = makeLogTag(FeedFragment.class);


    /**
     * Injected Vies
     */
    @InjectView(R.id.feed_recyclerview)
    RecyclerView mRecyclerView;

    @InjectView(R.id.feed_swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Data
     */
    List<Post> posts = new ArrayList<Post>();
    FeedAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    private boolean isRefresh = false;
    HashMap<String, Object> currentQueryMap = new HashMap<String, Object>();

    public static FeedFragment newInstance(String title, String slug,String query) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(WPpostInterface.TITLE, title);
        args.putString(WPpostInterface.PAGE, slug);
        args.putString(WPpostInterface.SEARCH, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.inject(this, view);

        currentQueryMap.put(WPpostInterface.CATEGORY, getArguments().getString(WPpostInterface.CATEGORY));
        currentQueryMap.put(WPpostInterface.SEARCH,getArguments().getString(WPpostInterface.SEARCH));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentQueryMap.put(WPpostInterface.PAGE, 1);
                loadPage(currentQueryMap);

            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        adapter = new FeedAdapter(posts, getActivity());

        mLayoutManager = new LinearLayoutManager(getActivity());
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        fadeIn.setDuration(250);
        LayoutAnimationController layoutAnimationController = new LayoutAnimationController(fadeIn);
        mRecyclerView.setLayoutAnimation(layoutAnimationController);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    loadPage(currentQueryMap);
                }
            }
        });

//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//                loadPage(new HashMap<String, Object>());
//            }
//        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LOGD(TAG, "onDestroyView");
        ButterKnife.reset(this);
    }


    public void loadPage(HashMap<String, Object> stringObjectMap) {
        LOGD(TAG, "Http-Get Query is " + stringObjectMap.toString());
        currentQueryMap = stringObjectMap;
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            if (isRefresh) {
                LOGD(TAG, "swipe is Refresh page : ,Ignore mannual refresh!");
            } else {

                new RestAdapter.Builder()
                        .setEndpoint("http://162.243.252.57/api")
                        .setLogLevel(RestAdapter.LogLevel.BASIC)
                        .build()
                        .create(WPpostInterface.class)
                        .getPostsByPage(stringObjectMap, new Callback<PostsWithStatus>() {

                            @Override
                            public void success(PostsWithStatus postsWithStatus, Response response) {
                                isRefresh = false;
                                if (mSwipeRefreshLayout != null){
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                                //Update page query
                                int page = Integer.valueOf(currentQueryMap.get(WPpostInterface.PAGE).toString());
                                //Update UI
                                List<Post> tmpPosts = postsWithStatus.getPosts();
                                if (page == 1) {
                                    Log.d(TAG, "current page is 1,try clear() the list");
                                    posts.clear();
                                }
                                posts.addAll(tmpPosts);
                                adapter.updateItems(true);


                                if (postsWithStatus.getCount() == 0) {
                                    UIutils.disMsg(getActivity(), getString(R.string.post_isempty));

                                } else {
                                    currentQueryMap.put(WPpostInterface.PAGE, page + 1);
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                isRefresh = false;
                                if (mSwipeRefreshLayout != null){
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                                UIutils.disErr(getActivity(), error);
                            }
                        });
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.networkerr), Toast.LENGTH_SHORT).show();
            isRefresh = false;
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


}
