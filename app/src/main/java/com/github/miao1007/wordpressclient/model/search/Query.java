package com.github.miao1007.wordpressclient.model.search;

/**
 * Created by leon on 14/9/23.
 */
public class Query {
    boolean ignore_sticky_posts;
    String s;
    String page;

    public boolean isIgnore_sticky_posts() {
        return ignore_sticky_posts;
    }

    public void setIgnore_sticky_posts(boolean ignore_sticky_posts) {
        this.ignore_sticky_posts = ignore_sticky_posts;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
