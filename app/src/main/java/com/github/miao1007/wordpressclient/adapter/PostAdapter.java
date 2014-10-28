package com.github.miao1007.wordpressclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.post.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by leon on 14/9/11.
 */
public class PostAdapter extends BaseAdapter {

    private List<Post> posts;
    private Context context;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    class ViewHolder {
        ImageView image_thumb;
        TextView content_title;
        TextView content_excerpt;
        TextView content_pubdate;
        TextView content_category;
    }

    @Override

    public int getCount() {
        return posts.size();
    }

    @Override
    public Post getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_card, null);
            holder.image_thumb = (ImageView)view.findViewById(R.id.listview_news_imageview_thumb);
            holder.content_category = (TextView)view.findViewById(R.id.listview_news_textview_category);
            holder.content_excerpt = (TextView)view.findViewById(R.id.listview_news_textview_excerpt);
            holder.content_pubdate = (TextView)view.findViewById(R.id.listview_news_textview_date);
            holder.content_title = (TextView)view.findViewById(R.id.listview_news_textview_title);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Post post = getItem(i);
        holder.content_title.setText(post.getTitle());
        holder.content_excerpt.setText(post.getExcerpt());
        holder.content_pubdate.setText(post.getDate());
        if (post.getCategories().isEmpty()){
            holder.content_category.setText("作者很懒，没有分类");
        } else {
            holder.content_category.setText(post.getCategories().get(0).getTitle());
        }

        if (post.getThumbnail_images() != null) {
            Picasso.with(context)
                    .load(post.getThumbnail_images().getMedium().getUrl())
                    .placeholder(R.drawable.android_load_loading)
                    .into(holder.image_thumb);
        }

        return view;
    }


}

