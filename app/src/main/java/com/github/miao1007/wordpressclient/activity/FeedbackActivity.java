package com.github.miao1007.wordpressclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.fragment.CommitFragmnet;

public class FeedbackActivity extends Activity {

//    EditText editText_email;
//    EditText editText_name;
//    EditText editText_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container_frame);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        CommitFragmnet commitFragmnet = new CommitFragmnet();
        Bundle bundle = new Bundle();
        bundle.putString("type","我要反馈");
        commitFragmnet.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container_frame , commitFragmnet).commit();
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
