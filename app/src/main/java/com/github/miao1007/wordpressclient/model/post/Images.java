
/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.model.post;

import java.util.HashMap;
import java.util.Map;

public class Images {

    private Full full;
    private Thumbnail thumbnail;
    private Medium medium;
    private Large large;
    private PostThumbnail postThumbnail;
    private TwentyfourteenFullWidth twentyfourteenFullWidth;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Full getFull() {
        return full;
    }

    public void setFull(Full full) {
        this.full = full;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    public Large getLarge() {
        return large;
    }

    public void setLarge(Large large) {
        this.large = large;
    }

    public PostThumbnail getPostThumbnail() {
        return postThumbnail;
    }

    public void setPostThumbnail(PostThumbnail postThumbnail) {
        this.postThumbnail = postThumbnail;
    }

    public TwentyfourteenFullWidth getTwentyfourteenFullWidth() {
        return twentyfourteenFullWidth;
    }

    public void setTwentyfourteenFullWidth(TwentyfourteenFullWidth twentyfourteenFullWidth) {
        this.twentyfourteenFullWidth = twentyfourteenFullWidth;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
