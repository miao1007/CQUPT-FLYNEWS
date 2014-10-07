package com.github.miao1007.wordpressclient.fragment;

import android.os.Bundle;
import android.util.Log;

import com.github.miao1007.wordpressclient.model.post.Post;
import com.github.miao1007.wordpressclient.utils.WordPressUtils;

import java.util.List;

/**
 * Created by leon on 14/9/30.
 * Function : 重写抽象类，实现UI等其他模块复用,只用重写更新数据线程和更新TITLE的Handler即可
 */
public class CategoryFragment extends BaseFragment {

    @Override
    protected List<Post> loadPage(Bundle bundle ,int currentPage) {

        if (bundle != null){
            if (bundle.containsKey("category")){
                Log.i("CategoryFragment found for",bundle.getString("category"));
                handler.sendMessage(handler.obtainMessage(1,bundle.getString("category")));
                return WordPressUtils.getPostsByCategory(bundle.getString("category"),String.valueOf(currentPage)).getPosts();
            } else if (bundle.containsKey("query")){
                Log.w("Search for ",bundle.getString("query"));
                handler.sendMessage(handler.obtainMessage(1,"对 \"" + bundle.getString("query") + "\" 的搜索结果"));
                return WordPressUtils.getPostsBySearch(bundle.getString("query"),String.valueOf(currentPage)).getPosts();
            } else {
                Log.w("nofound for category search",bundle.toString());
                handler.sendMessage(handler.obtainMessage(1,"主页"));
                return WordPressUtils.getPostsByPage(String.valueOf(currentPage)).getPosts();
            }
        } else {
            Log.w("bundle is null , try to use default",String.valueOf(currentPage));
            handler.sendMessage(handler.obtainMessage(1,"主页"));
            return WordPressUtils.getPostsByPage(String.valueOf(currentPage)).getPosts();
        }

    }
}
