package com.github.miao1007.wordpressclient.fragment;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.activity.DetailedPostActivity;
import com.github.miao1007.wordpressclient.adapter.PostAdapter;
import com.github.miao1007.wordpressclient.info.post.Post;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.utils.NetworkUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 14/9/19.
 */
public abstract class BaseFragment extends Fragment implements AdapterView.OnItemClickListener {


    List<Post> posts = new ArrayList<Post>();
    PullToRefreshListView listView;
    PostAdapter adapter;
    private int currentPage = 1;
    static final private int REFRESH_TITLE = 1;
    Handler handler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == REFRESH_TITLE && msg.obj != null && msg.obj instanceof String) {

                }
            }
        };
    }

    protected void setTitle(String title) {
        handler.sendMessage(handler.obtainMessage(REFRESH_TITLE, title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = (PullToRefreshListView) inflater.inflate(R.layout.frag_news, container, false);
        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.home_listview_emptyview, null);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listView.setRefreshing(true);
            }
        });
        listView.setOnItemClickListener(this);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadNextPage(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                currentPage = loadNextPage(currentPage);
            }
        });
        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                currentPage = loadNextPage(currentPage);

                System.out.println("LastItemVisibleListener");

            }
        });
        adapter = new PostAdapter(posts, getActivity());
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setEmptyView(emptyView);
        listView.setAdapter(adapter);
        //loadNextPage(0);
        Log.d("onCreateView", "view created");
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.setRefreshing(true);
            }
        }, 400);
        return listView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //loadNextPage(0);
    }

    //@Override the way to loadDate
    protected abstract List<Post> loadDate(Bundle bundle, int currentPage);


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //PullToRefresh Required A header ,so i- = i;
        i--;

        Post post = posts.get(i);
        Intent it = new Intent(getActivity(), DetailedPostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Model.POST_ID, String.valueOf(post.getId()));
        bundle.putString(Model.POST_TITLE, post.getTitle());
        bundle.putString(Model.POST_DATE, post.getDate());
        bundle.putString(Model.POST_AUTHOR, post.getAuthor().getName());
        bundle.putString(Model.POST_EXCERPT, post.getExcerpt());
        bundle.putString(Model.POST_URL, post.getUrl());
        bundle.putString(Model.POST_THUMB, post.getThumbnail_images().getMedium().getUrl());
        if (!post.getCategories().isEmpty()) {
            bundle.putString(Model.POST_CATEGORY, post.getCategories().get(0).getTitle());
        }
        it.putExtras(bundle);
        try {
            getActivity().startActivity(it);
        } catch (ActivityNotFoundException e) {
            Log.e(getClass().getSimpleName(), "ActionNoFound!");
        }
    }


    int loadNextPage(int current) {
        Log.d("loadNextPage", String.valueOf(current));
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            current++;
            new AsyncTask<Integer, Void, List<Post>>() {
                @Override
                protected void onPreExecute() {
                    //listView.setRefreshing(true);
                }

                @Override
                protected List<Post> doInBackground(Integer... paras) {
                    return loadDate(getArguments(), paras[0]);
                }

                @Override
                protected void onPostExecute(List<Post> tmpposts) {
                    if (tmpposts.size() != 0) {
                        posts.addAll(tmpposts);
                        adapter.notifyDataSetChanged();
                    } else {
                        currentPage--;
                        Toast.makeText(getActivity(), getString(R.string.post_isempty), Toast.LENGTH_SHORT).show();
                    }
                    listView.onRefreshComplete();
                }
            }.execute(current);

            return current;
        } else {
            Toast.makeText(getActivity(), getString(R.string.networkerr), Toast.LENGTH_SHORT).show();
            listView.onRefreshComplete();
            return current;
        }
    }


}
