package com.github.miao1007.wordpressclient.utils;

import com.github.miao1007.wordpressclient.info.comment.CommentResult;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by leon on 11/6/14.
 */
public interface WPcommitInterface {

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String CONTENT = "content";
    public static final String POST_ID = "post_id";
    public static final String POST_ID_COMMIT = "980";

    @GET("/submit_comment")
    void submitComment(@QueryMap Map<String, Object> option, Callback<CommentResult> commentResultCallback);
}
