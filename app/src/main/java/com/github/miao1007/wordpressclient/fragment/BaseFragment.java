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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.activity.DetailedPostActivity;
import com.github.miao1007.wordpressclient.adapter.PostAdapter;
import com.github.miao1007.wordpressclient.model.post.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 14/9/19.
 */
public abstract class BaseFragment extends Fragment implements  AdapterView.OnItemClickListener,AbsListView.OnScrollListener {


    List<Post> posts = new ArrayList<Post>();
    ListView listView;
    PostAdapter adapter;
    private int currentPage = 1;

    static final private boolean STATE_LOAD_MORE = true;
    static final private boolean STATE_REFRESH = false;
    static final private int REFRESH_TITLE = 1;
    private boolean STATE = false;
    Handler handler;


    //loadPage function Callback for UI update
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == REFRESH_TITLE){
                    String title = (String)msg.obj == null ? (String)msg.obj : "主页";
                    getActivity().getActionBar().setTitle(title);
                }
            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_news, container, false);
        listView = (ListView) view.findViewById(R.id.main_listview);
        listView.setOnItemClickListener(this);
        adapter = new PostAdapter(posts, getActivity());
//        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
//        swingBottomInAnimationAdapter.setAbsListView(listView);
//        swingBottomInAnimationAdapter.getViewAnimator().setAnimationDelayMillis(350);
        listView.setOnScrollListener(this);
//        listView.setAdapter(swingBottomInAnimationAdapter);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        new GetPostTask(STATE_LOAD_MORE).execute(String.valueOf(currentPage));
        //loadPage(getArguments());

    }

    protected abstract List<Post> loadPage(Bundle bundle ,int currentPage);


    private class GetPostTask extends AsyncTask<String, Void, List<Post>> {
        boolean isRefresh = false;

        private GetPostTask(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        @Override
        protected void onPreExecute() {
            //swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected List<Post> doInBackground(String... paras) {
            return loadPage(getArguments() , currentPage);
        }

        @Override
        protected void onPostExecute(List<Post> tmpposts) {
            posts.addAll(tmpposts);
            adapter.notifyDataSetChanged();
            //swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Post post = posts.get(i);
        Log.i(getClass().getSimpleName() + " send bundle", String.valueOf(post.getId()) + "/commentC=" + String.valueOf(post.getComment_count()));
        try {
            Intent it = new Intent(getActivity(), DetailedPostActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", String.valueOf(post.getId()));
            bundle.putString("title",post.getTitle());
            bundle.putString("date",post.getDate());
            bundle.putString("author",post.getAuthor().getName());
            bundle.putString("excerpt",post.getExcerpt());
            bundle.putString("url",post.getUrl());
            System.out.println("post.getThumbnail_images().getMedium().getUrl() = " + post.getThumbnail_images().getMedium().getUrl());
            bundle.putString("thumb",post.getThumbnail_images().getMedium().getUrl());
            if (!post.getCategories().isEmpty()){
                bundle.putString("category",post.getCategories().get(0).getTitle());
            }
            //bundle.putCharSequenceArrayList("sharesdk",socialization.getTopicOutline(String.valueOf(post.getId())));
            it.putExtras(bundle);       // it.putExtra(“test”, "shuju”);
            getActivity().startActivity(it);            // startActivityForResult(it,REQUEST_CODE);
        } catch (ActivityNotFoundException e){
            Log.e(getClass().getSimpleName(),"ActionNoFound!");
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (STATE == STATE_LOAD_MORE) {
            currentPage++;
            Log.i("OnScroll",String.valueOf(i));
            new GetPostTask(STATE_LOAD_MORE).execute(String.valueOf(currentPage));
        }
        STATE = STATE_REFRESH;
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        //i : recycled items , i2 : iems in visible , i3 : all items
        //Log.w("onScroll",i + "/" + i2 + "/" + i3);
        if ((i3 - i2 - i) <= 4) {
            STATE = STATE_LOAD_MORE;
        }
    }
}
