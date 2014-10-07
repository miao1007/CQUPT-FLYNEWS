/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.model.api;


import com.github.miao1007.wordpressclient.model.post.Post;
import com.github.miao1007.wordpressclient.model.search.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 14/9/13.
 */
public class PostsWithStatus {
    private String status ;
    private int count;
    private int count_total;
    private int pages;
    private List<Post> posts = new ArrayList<Post>();
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount_total() {
        return count_total;
    }

    public void setCount_total(int count_total) {
        this.count_total = count_total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
