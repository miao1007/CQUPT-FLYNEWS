package com.github.miao1007.wordpressclient.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.comment.CommentResult;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.utils.UIutils;
import com.github.miao1007.wordpressclient.utils.WPcommitInterface;
import com.github.miao1007.wordpressclient.utils.WPpostInterface;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FeedbackActivity extends Activity {

    //    EditText editText_email;
//    EditText editText_name;
//    EditText editText_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_commit);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText editText_name = (EditText) findViewById(R.id.frag_commit_editText_name);
        final EditText editText_email = (EditText) findViewById(R.id.frag_commit_editText_email);
        final EditText editText_content = (EditText) findViewById(R.id.frag_commit_editText_content);
        findViewById(R.id.frag_commit_commit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = editText_name.getText().toString();
                String email = editText_email.getText().toString();
                String content = editText_content.getText().toString();
                if (name.isEmpty() || email.isEmpty() || content.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "请完整填写", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> commitMap = new HashMap<String, Object>();
                    commitMap.put(WPcommitInterface.NAME, name);
                    commitMap.put(WPcommitInterface.EMAIL, email);
                    commitMap.put(WPcommitInterface.POST_ID, WPcommitInterface.POST_ID_COMMIT);
                    commitMap.put(WPcommitInterface.CONTENT, content);
                    new RestAdapter.Builder()
                            .setLogLevel(RestAdapter.LogLevel.FULL)
                            .setEndpoint(Model.END_POINT)
                            .build()
                            .create(WPpostInterface.class)
                            .submitComment(commitMap, new Callback<CommentResult>() {
                                @Override
                                public void success(CommentResult commentResult, Response response) {
                                    if (commentResult.getStatus().equals("error")) {
                                        UIutils.disMsg(FeedbackActivity.this, commentResult.getError());
                                    } else {
                                        UIutils.disMsg(FeedbackActivity.this, commentResult.getDate() + " 提交成功");
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    UIutils.disErr(FeedbackActivity.this, error);
                                }
                            });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.feedback, menu);
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
        } else if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }
        return super.onOptionsItemSelected(item);
    }
}
