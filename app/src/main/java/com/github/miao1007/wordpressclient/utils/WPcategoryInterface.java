package com.github.miao1007.wordpressclient.utils;

import com.github.miao1007.wordpressclient.info.api.CategoriesWithStatus;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by leon on 11/9/14.
 */
public interface WPcategoryInterface {
    @GET("/get_category_index")
    void getCategoryIndex(@Query("parent") String category_parent_id, Callback<CategoriesWithStatus> callback);
}
