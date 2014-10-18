package com.github.miao1007.wordpressclient.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.activity.AboutActivity;
import com.github.miao1007.wordpressclient.activity.FeedbackActivity;
import com.github.miao1007.wordpressclient.activity.LoginActivity;
import com.github.miao1007.wordpressclient.utils.CleanUtils;

/**
 * Created by leon on 14/10/1.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_setting, null);
        view.findViewById(R.id.frag_setting_login).setOnClickListener(this);
        view.findViewById(R.id.frag_setting_clean_cache).setOnClickListener(this);
        view.findViewById(R.id.frag_setting_feedback).setOnClickListener(this);
        view.findViewById(R.id.frag_setting_about_us).setOnClickListener(this);
        //coming soon
        //view.findViewById(R.id.frag_setting_check_update).setOnClickListener(this);
        view.findViewById(R.id.frag_setting_exit).setOnClickListener(this);
        //Reused include view
        View includeview = view.findViewById(R.id.frag_setting_include_text_size);
        includeview.findViewById(R.id.button_font_small).setOnClickListener(this);
        includeview.findViewById(R.id.button_font_large).setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getActionBar().setTitle(getString(R.string.action_settings));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_setting_login:
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.frag_setting_feedback:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.frag_setting_about_us:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.frag_setting_exit:
                getActivity().finish();
                break;
            //Set And Save the Text Size
            case R.id.button_font_small:
                preferences = getActivity().getSharedPreferences("textsize", 0);
                editor = preferences.edit();
                editor.putInt("size", preferences.getInt("size", 100) - 20 > 20 ? preferences.getInt("size", 100) - 20 : 0);
                editor.commit();
                Toast.makeText(getActivity(), "当前缩放为" + String.valueOf(preferences.getInt("size", 100)), Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_font_large:
                preferences = getActivity().getSharedPreferences("textsize", 0);
                editor = preferences.edit();
                editor.putInt("size", preferences.getInt("size", 100) + 20 < 200 ? preferences.getInt("size", 100) + 20 : 0);
                editor.commit();
                Toast.makeText(getActivity(), "当前缩放为" + String.valueOf(preferences.getInt("size", 100)), Toast.LENGTH_SHORT).show();
                break;
            case R.id.frag_setting_clean_cache:
                //create a dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示")
                        .setMessage("确定清楚缓存吗")
                        .setPositiveButton("确认",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CleanUtils.cleanInternalCache(getActivity());
                                CleanUtils.cleanExternalCache(getActivity());
                                CleanUtils.cleanDatabases(getActivity());
                                CleanUtils.cleanSharedPreference(getActivity());
                                Toast.makeText(getActivity(), "清理完成" ,Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();break;

        }
    }

}
