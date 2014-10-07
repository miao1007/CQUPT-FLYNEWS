
/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.model.post;

import java.util.HashMap;
import java.util.Map;

public class TwentyfourteenFullWidth_ {

    private String url;
    private int width;
    private int height;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
