package com.github.miao1007.wordpressclient.utils;

import com.github.miao1007.wordpressclient.info.api.PostsWithStatus;
import com.github.miao1007.wordpressclient.info.api.SinglePostWithStatus;
import com.github.miao1007.wordpressclient.info.comment.CommentResult;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by leon on 14/11/2.
 */
public interface WPpostInterface {

    public static String TITLE = "title";
    public static String PAGE = "page";
    public static String SEARCH = "s";
    public static String CATEGORY = "category";


    @GET("/get_posts")
    void getPostsByPage(@QueryMap Map<String,Object> option, Callback<PostsWithStatus> postsWithStatusCallback);


    @GET("/get_post")
    SinglePostWithStatus getPostById(@Query("post_id") String id);


    @GET("/submit_comment")
    void submitComment(@QueryMap Map<String,Object> option, Callback<CommentResult> commentResultCallback);

}
