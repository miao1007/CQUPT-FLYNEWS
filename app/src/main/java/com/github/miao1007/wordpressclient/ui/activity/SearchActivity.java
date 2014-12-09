package com.github.miao1007.wordpressclient.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.ui.fragment.FeedFragment;
import com.github.miao1007.wordpressclient.ui.widget.SendCommentButton;
import com.github.miao1007.wordpressclient.utils.UIutils;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchActivity extends BackableActivity {

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
                if (!query.isEmpty()) {
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.search_frame,
                            FeedFragment.newInstance(
                                    null,
                                    null,
                                    query)
                    ).commit();

                    v.setCurrentState(SendCommentButton.STATE_DONE);
                    ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                } else {
                    v.startAnimation(AnimationUtils.loadAnimation(SearchActivity.this, R.anim.shake_error));
                    UIutils.disMsg(SearchActivity.this, "请输入内容");
                }
            }
        });

    }


}
