package com.github.miao1007.wordpressclient.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.model.post.Post;
import com.github.miao1007.wordpressclient.utils.WordPressUtils;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.CommentListPage;
import cn.sharesdk.socialization.Socialization;

public class DetailedPostActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    static final String MIME_TYPE = "text/html";
    static final String ENCODING = "utf-8";

    OnekeyShare oks;
    Socialization socialization;

    ScrollView content_srollview;
    WebView content_webview;
    TextView content_title;
    TextView content_date;
    TextView content_category;

    CheckBox menu_like;
    CheckBox menu_share;
    CheckBox menu_more;
    CheckBox menu_reply;
    RelativeLayout layout;

    //Intent Extra
    private Post post;
    private String id;
    private String title;
    private String date;
    private String author;
    private String category;
    private String url;
    private String excerpt;
    private String thumb_image;

    private int likecount;
    //private ArrayList<String,Objects>


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        oks = new OnekeyShare();
        ShareSDK.registerService(Socialization.class);
        setContentView(R.layout.activity_detailed_post);

        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        author = getIntent().getStringExtra("author");
        excerpt = getIntent().getStringExtra("excerpt");
        url = getIntent().getStringExtra("url");
        thumb_image = getIntent().getStringExtra("thumb");
        if (getIntent().getStringExtra("category") != null) {
            category = getIntent().getStringExtra("category");
        } else {
            category = "未分类";
        }

        content_srollview = (ScrollView) findViewById(R.id.content_srollview);
        content_webview = (WebView) findViewById(R.id.content_webview);
        content_title = (TextView) findViewById(R.id.activity_detailed_textview_title);
        content_title.setText(title);
        content_date = (TextView) findViewById(R.id.activity_detailed_textview_date);
        content_date.setText(date);
        content_category = (TextView) findViewById(R.id.activity_detailed_textview_category);
        content_category.setText(category);

        menu_like = (CheckBox) findViewById(R.id.activity_detailed_button_like);
        menu_more = (CheckBox) findViewById(R.id.activity_detailed_button_more);
        menu_share = (CheckBox) findViewById(R.id.activity_detailed_button_share);
        menu_reply = (CheckBox) findViewById(R.id.activity_detailed_button_reply);
        layout = (RelativeLayout) findViewById(R.id.activity_detailed_button_more_layout);
        View includeView = layout.findViewById(R.id.activity_detailed_post_include_settings);
        includeView.findViewById(R.id.button_font_small).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content_webview.getSettings().setTextZoom((content_webview.getSettings().getTextZoom() - 20) > 20 ? (content_webview.getSettings().getTextZoom() - 20) : 20);
                SharedPreferences preferences = getSharedPreferences("textsize", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("size", content_webview.getSettings().getTextZoom());
                editor.commit();
            }
        });
        includeView.findViewById(R.id.button_font_large).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content_webview.getSettings().setTextZoom((content_webview.getSettings().getTextZoom() + 20) < 200 ? (content_webview.getSettings().getTextZoom() + 20) : 200);
                SharedPreferences preferences = getSharedPreferences("textsize", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("size", content_webview.getSettings().getTextZoom());
                editor.commit();
            }
        });


        //menu_like.setOnClickListener(this);
        //menu_reply.setOnClickListener(this);
        menu_like.setOnCheckedChangeListener(this);
        menu_reply.setOnCheckedChangeListener(this);
        menu_share.setOnCheckedChangeListener(this);
        menu_more.setOnCheckedChangeListener(this);


        new AsyncTask<String, Void, Post>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Post doInBackground(String... strings) {
                post = WordPressUtils.getPostById(strings[0]).getPost();
                return post;
            }

            @Override
            protected void onPostExecute(final Post post) {
                super.onPostExecute(post);

                //load date with customer stylesheet in assets

                String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /> <body class= \"gloable\"> " + post.getContent() + "</body>";
                WebSettings settings = content_webview.getSettings();
                settings.setTextZoom(getSharedPreferences("textsize", 0).getInt("size", 100));
                content_webview.loadDataWithBaseURL("file:///android_asset/", htmlData, MIME_TYPE, ENCODING, null);
            }

        }.execute(id);
        new AsyncTask<String, Void, HashMap<String, Integer>>() {
            @Override
            protected HashMap<String, Integer> doInBackground(String... strings) {
                socialization = ShareSDK.getService(Socialization.class);
                return socialization.getTopicOutline(strings[0]);
            }

            @Override
            protected void onPostExecute(HashMap<String, Integer> stringObjectsHashMap) {
                super.onPostExecute(stringObjectsHashMap);
                likecount = stringObjectsHashMap.get("likecount").intValue();
                menu_like.setText(String.valueOf(stringObjectsHashMap.get("likecount")));
                if (stringObjectsHashMap.get("likecount") != 0){
                    menu_reply.setText(String.valueOf(stringObjectsHashMap.get("comtcount")));
                }
                if (stringObjectsHashMap.get("sharecount") != 0){
                    menu_share.setText(String.valueOf(stringObjectsHashMap.get("sharecount")));
                }
            }


        }.execute(id);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(final CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.activity_detailed_button_like:
                if (b) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            socialization.likeTopic(id, title);
                            System.out.println("赞了一次！");
                        }
                    }).start();
                    Toast.makeText(this, "点赞是一种态度", Toast.LENGTH_SHORT).show();
                    menu_like.setText(String.valueOf(likecount + 1));
                } else {
                    menu_like.setClickable(false);
                    menu_like.setButtonDrawable(R.drawable.ssdk_social_toolbar_like);
                    Toast.makeText(this, "你已经赞过了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.activity_detailed_button_reply:
                if (b) {
                    CommentListPage page = new CommentListPage();
                    page.setTopic(id, title, date, author);
                    page.setOnekeyShare(oks);
                    page.show(DetailedPostActivity.this, null);
                } else {

                }
                break;
            case R.id.activity_detailed_button_share:
                if (b) {
                    createOKS();
                } else {
                }
                break;
            case R.id.activity_detailed_button_more:
//                PopupWindow popupWindow;
//                View menuView =getLayoutInflater().inflate(R.layout.activity_detailed_post_settings, null);
//                popupWindow = new PopupWindow(menuView);
////                popupWindow.showAtLocation(findViewById(R.id.activity_detailed_button_more), Gravity.CENTER
////                        | Gravity.CENTER, 0, 0);
                if (b) {
                    layout.setVisibility(View.VISIBLE);

                } else {
                    layout.setVisibility(View.GONE);

                }
                break;
        }
    }

    void createOKS() {
        //Share sdk : One Key Share
        oks.setNotification(R.drawable.ic_launcher, "分享");
        //设置分享的标题和URL，URL供Qzone调用
        oks.setTitle(title);
        oks.setTitleUrl(url);
        //设置分享的简介
        oks.setText("我在重邮飞讯上看到一个不错的文章:《" + title + "》,点击查看:" + url);
        //设置分享的图片外链，供微信调用
        oks.setImageUrl(thumb_image);
        System.out.println("thumb_image = " + thumb_image);
        //设置URL,供微信调用
        oks.setUrl(url);
        //设置站点名称，供Qzone调用
        oks.setSite("重邮飞讯");
        oks.show(DetailedPostActivity.this);
    }
}
