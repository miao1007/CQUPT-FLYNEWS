
/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.model.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {

    private int id;
    private String type;
    private String slug;
    private String url;
    private String status;
    private String title;
    private String titlePlain;
    private String content;
    private String excerpt;
    private String date;
    private String modified;
    private List<Category> categories = new ArrayList<Category>();
    private List<Tag> tags = new ArrayList<Tag>();
    private Author author;
    private List<Comment> comments = new ArrayList<Comment>();
    private List<Attachment> attachments = new ArrayList<Attachment>();
    private int comment_count;
    private String commentStatus;
    private String thumbnail;
    private CustomFields customFields;
    private String thumbnailSize;
    private ThumbnailImages thumbnail_images;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    //ShareSDK : comment and like
    private String likecount;
    private String comtcount;
    private String sharecount;

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public String getComtcount() {
        return comtcount;
    }

    public void setComtcount(String comtcount) {
        this.comtcount = comtcount;
    }

    public String getSharecount() {
        return sharecount;
    }

    public void setSharecount(String sharecount) {
        this.sharecount = sharecount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitlePlain() {
        return titlePlain;
    }

    public void setTitlePlain(String titlePlain) {
        this.titlePlain = titlePlain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public CustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(CustomFields customFields) {
        this.customFields = customFields;
    }

    public String getThumbnailSize() {
        return thumbnailSize;
    }

    public void setThumbnailSize(String thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
    }

    public ThumbnailImages getThumbnail_images() {
        return thumbnail_images;
    }

    public void setThumbnail_images(ThumbnailImages thumbnail_images) {
        this.thumbnail_images = thumbnail_images;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
