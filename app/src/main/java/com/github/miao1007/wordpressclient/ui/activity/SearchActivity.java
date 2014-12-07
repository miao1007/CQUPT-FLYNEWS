package com.github.miao1007.wordpressclient.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.ui.fragment.FeedFragment;
import com.github.miao1007.wordpressclient.ui.widget.SendCommentButton;
import com.github.miao1007.wordpressclient.utils.UIutils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.search_edit_text)
    EditText editText_query;

    @InjectView(R.id.search_send_comment_btn)
    SendCommentButton sendCommentButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sendCommentButton.setOnSendClickListener(new SendCommentButton.OnSendClickListener() {
            @Override
            public void onSendClickListener(SendCommentButton v) {
                String query = editText_query.getText().toString();
                if ( !query.isEmpty()){
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.search_frame,
                            FeedFragment.newInstance("搜索","",query)
                    ).commit();

                    v.setCurrentState(SendCommentButton.STATE_DONE);

                } else {
                    v.startAnimation(AnimationUtils.loadAnimation(SearchActivity.this, R.anim.shake_error));
                    UIutils.disMsg(SearchActivity.this,"请输入内容");
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if (id == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        }
        return super.onOptionsItemSelected(item);
    }
}
