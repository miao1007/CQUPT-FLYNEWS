package com.github.miao1007.wordpressclient.slidingmenu;

public class Item {

    String title;
    int iconres;

    //key-value pair
    static final public String QUERY_TYPE = "QUERY_TYPE";
    static final public String QUERY_CMD = "QUERY_CMD";
    static final public int QUERY_TYPE_SEARCH = 0;
    static final public int QUERY_TYPE_CATEGORY = 1;
    static final public int QUERY_TYPE_HOME = 2;
    static final public int QUERY_TYPE_SETTINGS = 3;
    private int mQueryType;

    private String mQueryTypeSearch;
    private String mQueryCmdSearch;
    private String mQueryTypeCategory;
    private String mQueryCmdCategory;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconres() {
        return iconres;
    }

    public void setIconres(int iconres) {
        this.iconres = iconres;
    }

    public int getmQueryType() {
        return mQueryType;
    }

    public Item(String title,int mQueryType  , int iconres ) {
        this.title = title;
        this.iconres = iconres;
        this.mQueryType = mQueryType;
    }
}
