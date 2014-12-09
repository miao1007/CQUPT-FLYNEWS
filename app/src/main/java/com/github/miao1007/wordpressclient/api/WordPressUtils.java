/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.api;


import com.github.miao1007.wordpressclient.info.api.CategoriesWithStatus;
import com.github.miao1007.wordpressclient.info.api.PostsWithStatus;
import com.github.miao1007.wordpressclient.info.api.SinglePostWithStatus;
import com.github.miao1007.wordpressclient.info.comment.CommentResult;
import com.github.miao1007.wordpressclient.model.Model;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by leon on 14/9/11.
 */
public class WordPressUtils {

    //WP JSON API DOC : http://wordpress.org/plugins/json-api/other_notes/


    interface WordPressAPIService {

        @GET("/get_posts")
        void getPostsByPage(@Query("page") int pages,@Query("category_name") String category_name, @Query("s") String query,Callback<PostsWithStatus> postsWithStatusCallback);

        @GET("/get_category_posts")
        PostsWithStatus getPostsByCategory(@Query("slug") String category_slug, @Query("page") String page);

        @GET("/get_posts")
        PostsWithStatus getPostsBySearch(@Query("s") String query, @Query("page") String page);

        @GET("/get_post")
        SinglePostWithStatus getPostById(@Query("post_id") String id);

        @GET("/get_category_index")
        CategoriesWithStatus getCategoryIndex(@Query("parent") String category_parent_id);


        @GET("/submit_comment")
        void submitComment(@Query("post_id") String post_id, @Query("name") String name, @Query("email") String email, @Query("content") String content, Callback<CommentResult> commentResultCallback);
    }

    public static RestAdapter adapter = new RestAdapter.Builder()
            .setEndpoint(Model.END_POINT)
            //.setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
    static final public WordPressAPIService service = adapter.create(WordPressAPIService.class);


    static public SinglePostWithStatus getPostById(String id) {
        SinglePostWithStatus singlePostWithStatus = new SinglePostWithStatus();
        try {
            singlePostWithStatus = service.getPostById(id);
            System.out.println("getPostByIdStatus:" + singlePostWithStatus.getStatus());
            return singlePostWithStatus;
        } catch (RetrofitError e) {
            e.printStackTrace();
            return singlePostWithStatus;
        }
    }

    static public CategoriesWithStatus getCategoryIndex(String parent_id) {
        CategoriesWithStatus categoriesWithStatus = new CategoriesWithStatus();
        try {
            categoriesWithStatus = service.getCategoryIndex(parent_id);
            System.out.println("fetch category = " + categoriesWithStatus.getCount());
            return categoriesWithStatus;
        } catch (RetrofitError e) {
            e.printStackTrace();
            return categoriesWithStatus;
        }
    }

    static public PostsWithStatus getPostsByCategory(String category_slug, String currentPage) {
        PostsWithStatus postsWithStatus = new PostsWithStatus();
        try {
            postsWithStatus = service.getPostsByCategory(category_slug, currentPage);
            System.out.println("getPostsByCategory/" + category_slug + "/count=" + postsWithStatus.getCount() + "/page=" + currentPage);
            return postsWithStatus;
        } catch (RetrofitError e) {
            e.printStackTrace();
            return postsWithStatus;
        }
    }

    static public PostsWithStatus getPostsBySearch(String searchQuery, String currentPage) {
        PostsWithStatus postsWithStatus = new PostsWithStatus();
        try {
            postsWithStatus = service.getPostsBySearch(searchQuery, currentPage);
            return postsWithStatus;
        } catch (RetrofitError e) {
            e.printStackTrace();
            return postsWithStatus;
        }
    }

    //http://leondemac.jd-app.com/api/submit_comment/?post_id=177&name=123&email=6621@qq.com&content=q213213123123
    static public void commitComment(String post_id, String name, String email, String content, Callback<CommentResult> commentResultCallback) {
        service.submitComment(post_id, name, email, content, commentResultCallback);
    }
}