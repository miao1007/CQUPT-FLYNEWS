package com.github.miao1007.wordpressclient.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

/**
 * Created by leon on 14/10/1.
 * Function : 基于ShareSDK的授权回调Activity
 */
public class LoginActivity extends ActionBarActivity implements Handler.Callback,
        View.OnClickListener, PlatformActionListener {

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.fragment_login);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.activity_login_by_qzone).setOnClickListener(this);
        findViewById(R.id.activity_login_by_sinaweibo).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        }
        return super.onOptionsItemSelected(item);
    }

    //Handler.Callback
    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, "您已经成功登陆", Toast.LENGTH_LONG).show();
                finish();
            }
            break;
            case MSG_LOGIN: {

                Toast.makeText(this, "MSG_LOGIN", Toast.LENGTH_SHORT).show();

            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, "取消授权", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, "授权错误", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, "授权完成", Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return false;
    }

    //View.OnClickListener
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.activity_login_by_qzone:
                authorize(new QZone(this));
                break;
            case R.id.activity_login_by_sinaweibo:
                authorize(new SinaWeibo(this));
                break;
        }
    }

    private void authorize(Platform plat) {
        if (plat == null) {
            Log.e("authorize", "plat is Empty");
        } else if (!plat.isValid()) {
            Log.e("authorize", "trying to log in");
            plat.SSOSetting(true);
            plat.showUser(null);
            plat.setPlatformActionListener(this);
        } else {
            String userId = plat.getDb().getUserId();
            //如果id不为空，就视为用户已经登录
            String avatar = plat.getDb().getUserIcon();
            String name = plat.getDb().getUserName();
            if (!TextUtils.isEmpty(userId)) {
                Log.e("authorize", "You have already logined in");
                setResult(name, avatar);
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
            } else {
                Log.e("authorize", "userId isEmpty");
            }
        }
    }

    //这个只有第一次登陆能使用，登陆成功后就没用了
    @Override
    public void onComplete(Platform plat, int action, HashMap<String, Object> stringObjectHashMap) {
        String name;
        String avatar;
        UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
        if (plat instanceof SinaWeibo) {
            name = String.valueOf(stringObjectHashMap.get("name"));
            avatar = String.valueOf(stringObjectHashMap.get("avatar_large"));
            setResult(name, avatar);
        } else if (plat instanceof QZone) {
            System.out.println("stringObjectHashMap = " + stringObjectHashMap);
            name = String.valueOf(stringObjectHashMap.get("nickname"));
            avatar = String.valueOf(stringObjectHashMap.get("figureurl_qq_2"));
            setResult(name, avatar);
        } else {
            Log.e("plat cast failed", plat.getName());
        }

    }

    //PlatformActionListener
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        throwable.printStackTrace();
    }

    //PlatformActionListener
    @Override
    public void onCancel(Platform platform, int i) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    //StartActivity For Result Callback
    private void setResult(String name, String avatar) {
        Log.e("setResult", name + "+" + avatar);
        //shared performance
        SharedPreferences preferences = LoginActivity.this.getSharedPreferences("sharesdk", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("avatar", avatar);
        editor.commit();
    }



}
