package com.github.miao1007.wordpressclient.ui.fragment;

import android.app.Activity;
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
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.api.WPpostInterface;
import com.github.miao1007.wordpressclient.info.api.PostsWithStatus;
import com.github.miao1007.wordpressclient.info.post.Post;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.ui.adapter.FeedAdapter;
import com.github.miao1007.wordpressclient.utils.NetworkUtils;
import com.github.miao1007.wordpressclient.utils.UIutils;

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
    String CURRENT_END_POINT_BAK = Model.END_POINT_BAK;

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

    public static FeedFragment newInstance(String title, String slug, String query) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(WPpostInterface.TITLE, title);
        args.putString(WPpostInterface.CATEGORY_SLUG, slug);
        args.putString(WPpostInterface.SEARCH, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate" + getArguments().getString(WPpostInterface.TITLE));
        currentQueryMap.put(WPpostInterface.CATEGORY_SLUG, getArguments().getString(WPpostInterface.CATEGORY_SLUG));
        currentQueryMap.put(WPpostInterface.SEARCH, getArguments().getString(WPpostInterface.SEARCH));
        currentQueryMap.put(WPpostInterface.PAGE, 1);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        ButterKnife.inject(this, view);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentQueryMap.put(WPpostInterface.PAGE, 1);
                loadPage();

            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        adapter = new FeedAdapter(posts, getActivity());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setHasFixedSize(true);
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
                    loadPage();
                    UIutils.disMsg(getActivity(), getString(R.string.fragment_feed_loading));
                }
            }
        });

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                loadPage();
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LOGD(TAG, "onDestroyView");
        ButterKnife.reset(this);
    }


    public void loadPage() {
        LOGD(TAG, currentQueryMap.toString());
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            if (isRefresh) {
                LOGD(TAG, "swipe is Refresh page : ,Ignore mannual refresh!");
            } else {

                new RestAdapter.Builder()
                        .setEndpoint(CURRENT_END_POINT_BAK)
                        .setLogLevel(RestAdapter.LogLevel.BASIC)
                        .build()
                        .create(WPpostInterface.class)
                        .getPostsByPage(currentQueryMap, new Callback<PostsWithStatus>() {

                            @Override
                            public void success(PostsWithStatus postsWithStatus, Response response) {
                                isRefresh = false;
                                if (mSwipeRefreshLayout != null) {
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
                                if (mSwipeRefreshLayout != null) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                                UIutils.disErr(getActivity(), error);
                                CURRENT_END_POINT_BAK = Model.END_POINT;
                                UIutils.disMsg(getActivity(),"使用备用服务器，请刷新！");
                            }
                        });
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.networkerr), Toast.LENGTH_SHORT).show();
            isRefresh = false;
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LOGD(TAG,"onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LOGD(TAG,"onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LOGD(TAG,"onDestroy");
    }
}
