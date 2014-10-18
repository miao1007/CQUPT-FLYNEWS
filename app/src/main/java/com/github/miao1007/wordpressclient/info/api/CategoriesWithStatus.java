/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.info.api;


import com.github.miao1007.wordpressclient.info.post.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 14/9/19.
 */
public class CategoriesWithStatus {
    String status;
    int count;
    List<Category> categories = new ArrayList<Category>();

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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
