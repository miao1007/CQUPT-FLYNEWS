package com.github.miao1007.wordpressclient.ui.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.widget.TintSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.api.CategoriesWithStatus;
import com.github.miao1007.wordpressclient.info.api.PostsWithStatus;
import com.github.miao1007.wordpressclient.info.post.Category;
import com.github.miao1007.wordpressclient.info.post.Post;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.ui.adapter.PostAdapter;
import com.github.miao1007.wordpressclient.ui.adapter.SpinnerAdapter;
import com.github.miao1007.wordpressclient.utils.NetworkUtils;
import com.github.miao1007.wordpressclient.utils.ScreenUtils;
import com.github.miao1007.wordpressclient.utils.UIutils;
import com.github.miao1007.wordpressclient.utils.WPcategoryInterface;
import com.github.miao1007.wordpressclient.utils.WPpostInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.github.miao1007.wordpressclient.utils.LogUtils.LOGD;
import static com.github.miao1007.wordpressclient.utils.LogUtils.LOGE;
import static com.github.miao1007.wordpressclient.utils.LogUtils.LOGW;
import static com.github.miao1007.wordpressclient.utils.LogUtils.makeLogTag;

/**
 * Created by leon on 14/9/19.
 */
public class BaseFragment extends Fragment {


    String TAG = makeLogTag(BaseFragment.class);
    List<Post> posts = new ArrayList<Post>();
    RecyclerView recyclerView;
    PostAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.LayoutManager mLayoutManager;

    TintSpinner mSpinner;
    Toolbar mToolbar;
    List<Category> categories = new ArrayList<Category>();
    SpinnerAdapter mAdapter;

    HashMap<String, Object> currentQueryMap = new HashMap<String, Object>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.frag_news, container, false);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentQueryMap.put("page", 1);
                loadPage(currentQueryMap);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        adapter = new PostAdapter(posts, getActivity());
        trySetupSpinner();
        if (ScreenUtils.isTablet(getActivity())) {
            mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else {
            mLayoutManager = new LinearLayoutManager(getActivity());
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isLastPage = false;
            int totaldy = 0;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isLastPage) {
                    loadPage(currentQueryMap);
                    totaldy = 0;
                    //mToolbar.setBackgroundColor(Color.BLUE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //TODO : get a better way to load more
                if (dy > 0) {
                    totaldy += dy;
                } else {
                    totaldy = 0;
                    mToolbar.showOverflowMenu();
                }
                if (totaldy > 200) {
                    isLastPage = true;
                    totaldy = 0;
                    if (mToolbar.isOverflowMenuShowing() || mToolbar.isOverflowMenuShowPending()) {
                        mToolbar.hideOverflowMenu();
                    }
                }
            }
        });
        loadPage(new HashMap<String, Object>());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LOGD(TAG, "onDestroyView");
        if (mToolbar != null && mSpinner !=null) {
            mToolbar.removeView(mToolbar);
        } else {
            LOGE(TAG, "Toolbar or Spinner is null!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LOGD(TAG, "onDestroyView");
        if (mToolbar != null && mSpinner !=null) {
            mToolbar.removeView(mToolbar);
        } else {
            LOGE(TAG, "Toolbar is null!");
        }
    }

    public void loadPage(HashMap<String, Object> stringObjectMap) {
        LOGD(TAG,"Http-Get Query is "+ stringObjectMap.toString());
        if (!stringObjectMap.containsKey("page")) {
            stringObjectMap.put("page", 1);
            LOGW(TAG, "Http-Get Query is empty,try defalut query" +stringObjectMap.toString());
        }
        currentQueryMap = stringObjectMap;
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            if (swipeRefreshLayout.isRefreshing()) {
                LOGD(TAG, "swipeRefreshLayout is Refresh page : ,Ignore mannual refresh!");
            } else {
                swipeRefreshLayout.setRefreshing(true);
                new RestAdapter.Builder()
                        .setEndpoint(Model.END_POINT)
                        .build()
                        .create(WPpostInterface.class)
                        .getPostsByPage(stringObjectMap, new Callback<PostsWithStatus>() {

                            @Override
                            public void success(PostsWithStatus postsWithStatus, Response response) {
                                swipeRefreshLayout.setRefreshing(false);

                                //Update page query
                                int page = Integer.valueOf(currentQueryMap.get("page").toString());
                                //Update UI
                                List<Post> tmpPosts = postsWithStatus.getPosts();
                                if (page == 1) {
                                    Log.d(TAG, "current page is 1,try clear() the list");
                                    posts.clear();
                                }
                                posts.addAll(tmpPosts);
                                adapter.notifyDataSetChanged();


                                if (postsWithStatus.getCount() == 0) {
                                    UIutils.disMsg(getActivity(), getString(R.string.post_isempty));

                                } else {
                                    currentQueryMap.put("page", page + 1);
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                swipeRefreshLayout.setRefreshing(false);
                                UIutils.disErr(getActivity(), error);
                            }
                        });
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.networkerr), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void trySetupSpinner() {
        LOGD(TAG, "trySetupSpinner");
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mSpinner = new TintSpinner(getActivity());
        Category mAllCategory = new Category();
        mAllCategory.setTitle("所有分类");
        mAllCategory.setSlug("");
        categories.add(0, mAllCategory);
        mAdapter = new SpinnerAdapter(categories, getActivity());
        mSpinner.setAdapter(mAdapter);
        mToolbar.removeView(mSpinner);
        mToolbar.addView(mSpinner);
        updateCategories();
    }

    void updateCategories(){
        LOGD(TAG, "updateCategories");
        new RestAdapter.Builder()
                .setEndpoint(Model.END_POINT)
                .build()
                .create(WPcategoryInterface.class).getCategoryIndex("", new Callback<CategoriesWithStatus>() {
            @Override
            public void success(CategoriesWithStatus categoriesWithStatus, Response response) {
                List<Category> tmpcategories = categoriesWithStatus.getCategories();
                categories.addAll(tmpcategories);
                mAdapter.notifyDataSetChanged();
                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        currentQueryMap.put("category_name", categories.get(i).getSlug());
                        currentQueryMap.put("page", 1);
                        loadPage(currentQueryMap);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                UIutils.disErr(getActivity(), error);
            }
        });
    }


}
