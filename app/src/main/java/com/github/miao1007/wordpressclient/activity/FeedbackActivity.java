package com.github.miao1007.wordpressclient.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.model.comment.CommentResult;
import com.github.miao1007.wordpressclient.utils.WordPressUtils;

public class FeedbackActivity extends Activity {

//    EditText editText_email;
//    EditText editText_name;
//    EditText editText_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_commit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView_content = (TextView)findViewById(R.id.frag_commit_textview_content);
        textView_content.setText("我要反馈");
        final EditText editText_content = (EditText)findViewById(R.id.frag_commit_editText_content);
        final EditText editText_email = (EditText)findViewById(R.id.frag_commit_editText_email);
        final EditText editText_name = (EditText)findViewById(R.id.frag_commit_editText_name);

        findViewById(R.id.frag_commit_commit).setOnClickListener(new View.OnClickListener() {
            CommentResult commentResult;
            //post id = 2
            //http://leondemac.jd-app.com/wp-admin/post.php?post=2&action=edit

            @Override
            public void onClick(View view) {
                String name = editText_name.getText().toString();
                String email = editText_email.getText().toString();
                String content = editText_content.getText().toString();

                if (name.isEmpty() || email.isEmpty() || content.isEmpty()) {
                    Toast.makeText(FeedbackActivity.this, "请正确填写", Toast.LENGTH_SHORT).show();
                } else {

                    new AsyncTask<String, Void, CommentResult>() {
                        @Override
                        protected CommentResult doInBackground(String... strings) {
                            return WordPressUtils.commitComment("2", strings[0], strings[1], strings[2]);
                        }

                        @Override
                        protected void onPostExecute(CommentResult commentResult) {
                            super.onPostExecute(commentResult);
                            if (commentResult != null) {
                                if (commentResult.getStatus().equals("pending")) {
                                    Toast.makeText(FeedbackActivity.this, "Server:非常感谢,我们将及时回复您", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(FeedbackActivity.this, "Server:" + commentResult.getStatus(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(FeedbackActivity.this, "Server:检测到重复数据", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }.execute(name, email, content);
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
        }else if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
