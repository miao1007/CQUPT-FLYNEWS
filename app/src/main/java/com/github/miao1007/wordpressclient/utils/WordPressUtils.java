/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.utils;


import com.github.miao1007.wordpressclient.model.api.CategoriesWithStatus;
import com.github.miao1007.wordpressclient.model.api.PostsWithStatus;
import com.github.miao1007.wordpressclient.model.api.SinglePostWithStatus;
import com.github.miao1007.wordpressclient.model.comment.CommentResult;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by leon on 14/9/11.
 */
public class WordPressUtils  {

    //WP JSON API DOC : http://wordpress.org/plugins/json-api/other_notes/
    //WP QUERY DOC : http://codex.wordpress.org/Class_Reference/WP_Query#Parameters
    //search by categories : http://leondemac.jd-app.com/api/get_category_posts/?slug=最新学校活动资讯
    //get category index : http://leondemac.jd-app.com/api/get_category_index/

    final static String END_POINT = "http://leondemac.jd-app.com/api";
    interface WordPressAPIService {

        @GET("/get_posts")
        PostsWithStatus getPostsByPage(@Query("page") String page);
        @GET("/get_category_posts")
        PostsWithStatus getPostsByCategory(@Query("slug") String category_slug, @Query("page") String page);
        @GET("/get_posts")
        PostsWithStatus getPostsBySearch(@Query("s") String query, @Query("page") String page);

        @GET("/get_post")
        SinglePostWithStatus getPostById(@Query("post_id") String id);
        @GET("/get_category_index")
        CategoriesWithStatus getCategoryIndex(@Query("parent") String category_parent_id);

        //http://leondemac.jd-app.com/api/submit_comment/?post_id=177&name=123&email=6621@qq.com&content=qawedsad
        @GET("/submit_comment")
        CommentResult submitComment(@Query("post_id") String post_id, @Query("name") String name, @Query("email") String email, @Query("content") String content);
    }
    final static RestAdapter adapter = new RestAdapter.Builder()
            .setEndpoint(END_POINT)
            //.setErrorHandler(new MyErrorHandler())
            //.setLogLevel(RestAdapter.LogLevel.FULL)
            .build();
    final static WordPressAPIService service = adapter.create(WordPressAPIService.class);

    static public PostsWithStatus getPostsByPage(String current_page){
        PostsWithStatus postsWithStatus = new PostsWithStatus();
        try {
            postsWithStatus = service.getPostsByPage(current_page);
            System.out.println("fetchtitle=" + postsWithStatus.getCount() + "/all="  + postsWithStatus.getCount_total() +"/currentPage=" + current_page);
            return postsWithStatus;
        } catch (RetrofitError e){
            e.printStackTrace();
            return postsWithStatus;
        }
    }

    static public SinglePostWithStatus getPostById(String id){
        SinglePostWithStatus singlePostWithStatus = new SinglePostWithStatus();
        try {
            singlePostWithStatus = service.getPostById(id);
            System.out.println("getPostByIdStatus:" + singlePostWithStatus.getStatus());
            return singlePostWithStatus;
        } catch (RetrofitError e){
            e.printStackTrace();
            return singlePostWithStatus;
        }
    }

    static public CategoriesWithStatus getCategoryIndex(String parent_id){
        CategoriesWithStatus categoriesWithStatus = new CategoriesWithStatus();
        try {
            categoriesWithStatus = service.getCategoryIndex(parent_id);
            System.out.println("fetch category = " + categoriesWithStatus.getCount());
            return  categoriesWithStatus;
        } catch (RetrofitError e){
            e.printStackTrace();
            return  categoriesWithStatus;
        }
    }

    static public PostsWithStatus getPostsByCategory(String category_slug , String currentPage){
        PostsWithStatus postsWithStatus = new PostsWithStatus();
        try {
            postsWithStatus = service.getPostsByCategory(category_slug , currentPage);
            System.out.println("getPostsByCategory count" + postsWithStatus.getCount());
            return postsWithStatus;
        } catch (RetrofitError e){
            e.printStackTrace();
            return postsWithStatus;
        }
    }

    static public PostsWithStatus getPostsBySearch(String searchQuery , String currentPage){
        PostsWithStatus postsWithStatus = new PostsWithStatus();
        try {
            postsWithStatus = service.getPostsBySearch(searchQuery , currentPage);
            return postsWithStatus;
        } catch (RetrofitError e){
            e.printStackTrace();
            return postsWithStatus;
        }
    }
    //http://leondemac.jd-app.com/api/submit_comment/?post_id=177&name=123&email=6621@qq.com&content=q213213123123
    static public CommentResult commitComment(String post_id, String name , String email , String content){
        try {
            return service.submitComment(post_id,name,email,content);
        } catch (Exception e){
            return null;
        }
    }

    static class MyErrorHandler implements ErrorHandler {
        @Override public Throwable handleError(RetrofitError cause) {
            Response r = cause.getResponse();
            switch (cause.getResponse().getStatus()){
                case 401 : return new Exception("Unauthorized Exception!");
                case 500 : return new NullPointerException("You Have Already Commented On This Page!");
                default:
                    return new Exception(cause.getResponse().getReason());
            }
        }
    }
}
