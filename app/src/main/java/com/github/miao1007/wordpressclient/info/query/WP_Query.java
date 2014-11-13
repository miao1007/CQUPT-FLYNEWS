package com.github.miao1007.wordpressclient.info.query;

import java.util.HashMap;

/**
 * Created by leon on 14/11/3.
 */
public class WP_Query extends HashMap<String,Object> {
    public static String QUERY = "query";
    public static String CATEGORY_NAME = "category_name";
    public static String PAGE = "page";
    private String query;
    private String category_name;
    private int page;

}
