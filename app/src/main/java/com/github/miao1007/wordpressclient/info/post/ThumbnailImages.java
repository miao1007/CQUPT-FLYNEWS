
/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.info.post;

import java.util.HashMap;
import java.util.Map;

public class ThumbnailImages {

    private Full_ full;
    private Thumbnail_ thumbnail;
    private Medium_ medium;
    private Large_ large;
    private PostThumbnail_ postThumbnail;
    private TwentyfourteenFullWidth_ twentyfourteenFullWidth;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Full_ getFull() {
        return full;
    }

    public void setFull(Full_ full) {
        this.full = full;
    }

    public Thumbnail_ getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail_ thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Medium_ getMedium() {
        return medium;
    }

    public void setMedium(Medium_ medium) {
        this.medium = medium;
    }

    public Large_ getLarge() {
        return large;
    }

    public void setLarge(Large_ large) {
        this.large = large;
    }

    public PostThumbnail_ getPostThumbnail() {
        return postThumbnail;
    }

    public void setPostThumbnail(PostThumbnail_ postThumbnail) {
        this.postThumbnail = postThumbnail;
    }

    public TwentyfourteenFullWidth_ getTwentyfourteenFullWidth() {
        return twentyfourteenFullWidth;
    }

    public void setTwentyfourteenFullWidth(TwentyfourteenFullWidth_ twentyfourteenFullWidth) {
        this.twentyfourteenFullWidth = twentyfourteenFullWidth;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
