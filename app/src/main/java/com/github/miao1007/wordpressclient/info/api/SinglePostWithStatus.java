/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.info.api;


import com.github.miao1007.wordpressclient.info.post.Post;

/**
 * Created by leon on 14/9/13.
 */
public class SinglePostWithStatus {
    private String status;
    private Post post;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
