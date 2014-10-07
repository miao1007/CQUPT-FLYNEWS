package com.github.miao1007.wordpressclient.fragment;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by leon on 14/10/5.
 */
public class LoginFragment extends Fragment implements Handler.Callback,
        View.OnClickListener, PlatformActionListener {

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;

    private static final String PREFS_NAME = "avaterandname";

    public interface Listener {
        public void somethingHappenedInFragment(HashMap<String, Object> stringObjectHashMap);
    }

    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);
        view.findViewById(R.id.activity_login_by_qzone).setOnClickListener(this);
        view.findViewById(R.id.activity_login_by_sinaweibo).setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(getActivity());

    }

    //Handler.Callback
    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(getActivity(), "你已经登陆成功", Toast.LENGTH_LONG).show();
            }
            break;
            case MSG_LOGIN: {

                Toast.makeText(getActivity(), "MSG_LOGIN", Toast.LENGTH_SHORT).show();

            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(getActivity(), "MSG_AUTH_CANCEL", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(getActivity(), "MSG_AUTH_ERROR", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(getActivity(), "MSG_AUTH_COMPLETE", Toast.LENGTH_SHORT).show();
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
                authorize(new QZone(getActivity()));
                break;
            case R.id.activity_login_by_sinaweibo:
                authorize(new SinaWeibo(getActivity()));
                break;
        }
    }

    private void authorize(Platform plat) {
        if (plat == null) {
            Log.e("authorize","plat is null");
        } else {
            if (plat.isValid()) {
                String userId = plat.getDb().getUserId();
                System.out.println("plat = " + plat.getDb().getUserName());
                System.out.println("plat.getDb().getUserIcon() = " + plat.getDb().getUserIcon());
                if (!TextUtils.isEmpty(userId)) {
                    UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                    login(plat.getName(), userId, null);
                    //sso login
                    plat.SSOSetting(true);
                    plat.setPlatformActionListener(this);
                    plat.showUser(null);
                }
            } else {
                Log.e("authorize","plat is not valid");
            }
        }

    }

    //PlatformActionListener
    @Override
    public void onComplete(Platform plat, int action, HashMap<String, Object> stringObjectHashMap) {
        if (plat == null) {
            return;
        }

        if (plat.isValid()) {
            String userId = plat.getDb().getUserId();
            System.out.println("userId = " + userId);
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat.getName(), userId, null);
                //send to HomeActivity to refresh avatar and name
                mListener.somethingHappenedInFragment(stringObjectHashMap);
                String name = String.valueOf(stringObjectHashMap.get("name"));
                String avatar = String.valueOf(stringObjectHashMap.get("avatar_large"));
                SharedPreferences preferences = getActivity().getSharedPreferences("weibo",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("name",name);
                editor.putString("avatar",avatar);
                editor.commit();


                return;
            }
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

    private void login(String plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }


}
