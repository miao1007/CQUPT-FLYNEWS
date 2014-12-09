package com.github.miao1007.wordpressclient.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.miao1007.wordpressclient.R;
import com.github.miao1007.wordpressclient.info.post.Post;
import com.github.miao1007.wordpressclient.model.Model;
import com.github.miao1007.wordpressclient.ui.activity.DetailedPostActivity;
import com.github.miao1007.wordpressclient.utils.ScreenUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by leon on 14/9/11.
 */
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder> {

    private List<Post> posts;
    private Context context;

    private int lastAnimatedPosition = -1;
    private int itemsCount = 0;
    private boolean animateItems = false;
    private static final int ANIMATED_ITEMS_COUNT = 2;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.imageview_thumb)
        ImageView image_thumb;

        @InjectView(R.id.textview_title)
        TextView content_title;

        @InjectView(R.id.textview_excerpt)
        TextView content_excerpt;

        @InjectView(R.id.textview_date)
        TextView content_pubdate;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }

    }

    private void runEnterAnimation(View view, int position) {
        if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(ScreenUtils.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }


    public FeedAdapter(List<Post> posts, Context context) {
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
        //runEnterAnimation(myViewHolder.itemView, i);
        final Post post = posts.get(i);
        if (post.getThumbnail_images() != null){
            Picasso.with(context).load(post.getThumbnail_images().getFull().getUrl())
                    //.placeholder(R.drawable.slidingmenu_header_bg)
                    .into(myViewHolder.image_thumb);
        }
        myViewHolder.content_title.setText(post.getTitle());
        myViewHolder.content_excerpt.setText(post.getExcerpt());
        myViewHolder.content_pubdate.setText(post.getDate());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updateItems(boolean animated) {
        itemsCount = posts.size();
        animateItems = animated;
        notifyDataSetChanged();
    }


}

