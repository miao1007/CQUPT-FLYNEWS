package com.github.miao1007.wordpressclient.ui.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.post.Post;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.ui.activity.DetailedPostActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by leon on 14/9/11.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<Post> posts;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image_thumb;
        TextView content_title;
        TextView content_excerpt;
        TextView content_pubdate;
        TextView content_category;

        public MyViewHolder(View itemView) {
            super(itemView);
            image_thumb = (ImageView) itemView.findViewById(R.id.imageview_thumb);
            content_category = (TextView) itemView.findViewById(R.id.textview_category);
            content_excerpt = (TextView) itemView.findViewById(R.id.textview_excerpt);
            content_pubdate = (TextView) itemView.findViewById(R.id.textview_date);
            content_title = (TextView) itemView.findViewById(R.id.textview_title);
        }

    }


    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.adapter_card, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        final Post post = posts.get(i);
        if (post.getThumbnail_images() != null){
            Picasso.with(context).load(post.getThumbnail_images().getFull().getUrl())
                    .placeholder(R.drawable.holder)
                    .into(myViewHolder.image_thumb);
        }
        myViewHolder.content_title.setText(post.getTitle());
        myViewHolder.content_excerpt.setText(post.getExcerpt());
        myViewHolder.content_pubdate.setText(post.getDate());
        if (!post.getCategories().isEmpty()){
            myViewHolder.content_category.setText(post.getCategories().get(0).getTitle());
        }
        myViewHolder.image_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, DetailedPostActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Model.POST_ID, String.valueOf(post.getId()));
                bundle.putString(Model.POST_TITLE, post.getTitle());
                bundle.putString(Model.POST_DATE, post.getDate());
                bundle.putString(Model.POST_AUTHOR, post.getAuthor().getName());
                bundle.putString(Model.POST_EXCERPT, post.getExcerpt());
                bundle.putString(Model.POST_URL, post.getUrl());
                if (!post.getCategories().isEmpty()) {
                    bundle.putString(Model.POST_CATEGORY, post.getCategories().get(0).getTitle());
                }
                it.putExtras(bundle);
                try {
                    context.startActivity(it);
                } catch (ActivityNotFoundException e) {
                    Log.e(getClass().getSimpleName(), "ActionNoFound!");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}

