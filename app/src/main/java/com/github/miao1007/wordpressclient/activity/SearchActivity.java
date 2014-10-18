package com.github.miao1007.wordpressclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;

public class SearchActivity extends Activity {

    EditText editText_query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        editText_query = (EditText)findViewById(R.id.frag_search_edittext_content);
        findViewById(R.id.frag_search_dosearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = editText_query.getText().toString();
                if ( !query.isEmpty()){
                    setResult(query);
                    finish();
                } else {
                    Toast.makeText(SearchActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //StartActivity For Result Callback
    private void setResult(String query){
        //start activity for result
        Intent mIntent = getIntent();
        int resultcode = 3;
        mIntent.putExtra("query", query);
        // 设置结果，并进行传送
        SearchActivity.this.setResult(resultcode, mIntent);
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
        }
        return super.onOptionsItemSelected(item);
    }
}
