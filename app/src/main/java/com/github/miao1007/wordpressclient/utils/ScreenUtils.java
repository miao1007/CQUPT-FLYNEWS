package com.github.miao1007.wordpressclient.utils;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by leon on 11/7/14.
 */
public class ScreenUtils {
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
