package com.github.miao1007.wordpressclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.post.Post;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.utils.NetworkUtils;
import com.github.miao1007.wordpressclient.utils.WordPressUtils;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.socialization.CommentListPage;
import cn.sharesdk.socialization.Socialization;

public class DetailedPostActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    static final String MIME_TYPE = "text/html";
    static final String ENCODING = "utf-8";


    ScrollView content_srollview;
    WebView content_webview;
    TextView content_title;
    TextView content_date;
    TextView content_category;


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

    //menu
    CheckBox menu_like;
    CheckBox menu_share;
    CheckBox menu_more;
    CheckBox menu_reply;
    RelativeLayout layout;
    OnekeyShare oks;
    Socialization socialization;
    private int likecount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        oks = new OnekeyShare();
        ShareSDK.registerService(Socialization.class);
        setContentView(R.layout.activity_detailed_post);

        id = getIntent().getStringExtra(Model.POST_ID);
        title = getIntent().getStringExtra(Model.POST_TITLE);
        date = getIntent().getStringExtra(Model.POST_DATE);
        author = getIntent().getStringExtra(Model.POST_AUTHOR);
        excerpt = getIntent().getStringExtra(Model.POST_EXCERPT);
        url = getIntent().getStringExtra(Model.POST_URL);
        thumb_image = getIntent().getStringExtra(Model.POST_THUMB);
        if (getIntent().getStringExtra(Model.POST_CATEGORY) != null) {
            category = getIntent().getStringExtra(Model.POST_CATEGORY);
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

        menu_like.setOnCheckedChangeListener(this);
        menu_reply.setOnCheckedChangeListener(this);
        menu_share.setOnCheckedChangeListener(this);
        menu_more.setOnCheckedChangeListener(this);


        if (!NetworkUtils.isNetworkAvailable(DetailedPostActivity.this)) {
            Toast.makeText(DetailedPostActivity.this, getString(R.string.networkerr), Toast.LENGTH_SHORT).show();
        } else {
            getContent(id);
            getComment(id);
        }
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
                View dialogView = LayoutInflater.from(this).inflate(R.layout.activity_detailed_post_settings, null);
                dialogView.findViewById(R.id.button_font_large).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeFootScan(true);
                    }
                });
                dialogView.findViewById(R.id.button_font_small).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeFootScan(false);
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

                wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;
                wmlp.x = 0;   //x position
                wmlp.y = 100;   //y position
                dialog.show();

                if (b) {
                    dialog.show();
                } else {
                    //dialog.dismiss();
                }
                break;
        }
    }

    void createOKS() {
        //Share sdk : One Key Share
        oks.setNotification(R.drawable.ic_launcher, getString(R.string.share));
        //设置分享的标题和URL，URL供Qzone调用
        oks.setTitle(title);
        oks.setTitleUrl(url);
        //设置分享的简介
        oks.setText("我在" + getString(R.string.app_name) + "上看到一个不错的文章:《" + title + "》,点击查看:" + url);
        //设置分享的图片外链，供微信调用
        oks.setImageUrl(thumb_image);
        //设置URL,供微信调用
        oks.setUrl(url);
        //设置站点名称，供Qzone调用
        oks.setSite(getString(R.string.app_name));
        oks.show(DetailedPostActivity.this);
    }

    void changeFootScan(boolean isLarger) {
        SharedPreferences preferences;
        SharedPreferences.Editor editor;
        if (isLarger) {
            content_webview.getSettings().setTextZoom((content_webview.getSettings().getTextZoom() + 20) < 200 ? (content_webview.getSettings().getTextZoom() + 20) : 200);
        } else {
            content_webview.getSettings().setTextZoom((content_webview.getSettings().getTextZoom() - 20) > 20 ? (content_webview.getSettings().getTextZoom() - 20) : 20);
        }
        preferences = getSharedPreferences(Model.WEBVIEW_SETTINGS, 0);
        editor = preferences.edit();
        editor.putInt(Model.WEBVIEW_SETTINGS_SIZE, content_webview.getSettings().getTextZoom());
        //Better use apply() rather than commit();
        editor.apply();
    }

    void getContent(String postid){
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
                if (post == null) {
                    Toast.makeText(DetailedPostActivity.this, getString(R.string.networkerr), Toast.LENGTH_SHORT).show();
                } else {
                    String htmlData = "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\" /> <body class= \"gloable\"> " + post.getContent() + "</body>";
                    WebSettings settings = content_webview.getSettings();
                    settings.setTextZoom(getSharedPreferences(Model.WEBVIEW_SETTINGS, 0).getInt(Model.WEBVIEW_SETTINGS_SIZE, 100));
                    content_webview.loadDataWithBaseURL("file:///android_asset/", htmlData, MIME_TYPE, ENCODING, null);
                }

            }

        }.execute(postid);
    }

    void getComment(String id){
        new AsyncTask<String, Void, HashMap<String, Integer>>() {
            @Override
            protected HashMap<String, Integer> doInBackground(String... strings) {
                socialization = ShareSDK.getService(Socialization.class);
                return socialization.getTopicOutline(strings[0]);
            }

            @Override
            protected void onPostExecute(HashMap<String, Integer> stringObjectsHashMap) {
                super.onPostExecute(stringObjectsHashMap);
                likecount = stringObjectsHashMap.get(Model.SHARE_LIKE_COUNT).intValue();
                menu_like.setText(String.valueOf(stringObjectsHashMap.get(Model.SHARE_LIKE_COUNT)));
                if (stringObjectsHashMap.get(Model.SHARE_COMMNET_COUNT) != 0) {
                    menu_reply.setText(String.valueOf(stringObjectsHashMap.get(Model.SHARE_COMMNET_COUNT)));
                }
                if (stringObjectsHashMap.get(Model.SHARE_SHARE_COUNT) != 0) {
                    menu_share.setText(String.valueOf(stringObjectsHashMap.get(Model.SHARE_SHARE_COUNT)));
                }
            }

        }.execute(id);
    }
}
