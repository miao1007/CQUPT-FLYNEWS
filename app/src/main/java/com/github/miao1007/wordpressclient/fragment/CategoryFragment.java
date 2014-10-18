package com.github.miao1007.wordpressclient.fragment;

import android.os.Bundle;
import android.util.Log;

import com.github.miao1007.wordpressclient.info.post.Post;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.utils.WordPressUtils;

import java.util.List;

/**
 * Created by leon on 14/9/30.
 * Function : 重写抽象类，实现UI等其他模块复用,只用重写更新数据线程和更新TITLE的Handler即可
 */
public class CategoryFragment extends BaseFragment {

    @Override
    protected List<Post> loadDate(Bundle bundle, int currentPage) {

        if (bundle != null){
            if (bundle.containsKey(Model.FRAGMENT_CATEGORY)){
                Log.i("CategoryFragment found for",bundle.getString(Model.FRAGMENT_CATEGORY));
                setTitle(bundle.getString(Model.FRAGMENT_CATEGORY));
                return WordPressUtils.getPostsByCategory(bundle.getString(Model.FRAGMENT_CATEGORY),String.valueOf(currentPage)).getPosts();
            } else if (bundle.containsKey(Model.FRAGMENT_SEARCH)){
                Log.i("Search for ",bundle.getString(Model.FRAGMENT_SEARCH));
                setTitle("对 \"" + bundle.getString(Model.FRAGMENT_SEARCH) + "\" 的搜索结果");
                return WordPressUtils.getPostsBySearch(bundle.getString(Model.FRAGMENT_SEARCH),String.valueOf(currentPage)).getPosts();
            } else {
                Log.w("No found args for category or search",bundle.toString());
                setTitle("主页");
                return WordPressUtils.getPostsByPage(String.valueOf(currentPage)).getPosts();
            }
        } else {
            Log.w("bundle is null , try to use default",String.valueOf(currentPage));
            setTitle("主页");
            return WordPressUtils.getPostsByPage(String.valueOf(currentPage)).getPosts();
        }
    }


}
