
/*
 * Copyright (c) 2014.
 * Author : leon
 * Feel free to ues it!
 */

package com.github.miao1007.wordpressclient.model.comment;

import java.util.HashMap;
import java.util.Map;


public class Comments {

    private int iD;
    private int post;
    private String content;
    private String status;
    private String type;
    private int parent;
    private Author author;
    private String date;
    private String dateTz;
    private String dateGmt;
    private Meta_ meta;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public int getID() {
        return iD;
    }

    public void setID(int iD) {
        this.iD = iD;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTz() {
        return dateTz;
    }

    public void setDateTz(String dateTz) {
        this.dateTz = dateTz;
    }

    public String getDateGmt() {
        return dateGmt;
    }

    public void setDateGmt(String dateGmt) {
        this.dateGmt = dateGmt;
    }

    public Meta_ getMeta() {
        return meta;
    }

    public void setMeta(Meta_ meta) {
        this.meta = meta;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
