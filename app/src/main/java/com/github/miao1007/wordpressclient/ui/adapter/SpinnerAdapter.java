package com.github.miao1007.wordpressclient.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.post.Category;

import java.util.List;
import java.util.Random;

/**
 * Created by leon on 11/9/14.
 */
public class SpinnerAdapter extends BaseAdapter {

    private List<Category> categories;
    private Context context;

    public SpinnerAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.slidingmenu_categories, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.category_image_tag);
        Random random=new Random();
        ColorFilter cf = new PorterDuffColorFilter(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)), PorterDuff.Mode.OVERLAY);
        imageView.setColorFilter(cf);
        TextView category_type = (TextView) view.findViewById(R.id.slidingmenu_categories_tpye);
        category_type.setText(getItem(i).getTitle());
        return view;
    }
}
