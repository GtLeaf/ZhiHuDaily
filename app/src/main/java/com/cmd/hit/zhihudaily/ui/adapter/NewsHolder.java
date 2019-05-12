package com.cmd.hit.zhihudaily.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.ui.bean.NewsBean;

public class NewsHolder extends RecyclerView.ViewHolder {
    private TextView textView;
    private ImageView imageView;
    private View itemView;
    public NewsHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        textView = (TextView)itemView.findViewById(R.id.news_item_title);
        imageView = (ImageView)itemView.findViewById(R.id.news_item_image);
    }
    public void bindHolder(NewsBean newsBean){
        textView.setText(newsBean.getTitle());
        Glide.with(itemView.getContext()).load(newsBean.getImage()).into(imageView);
    }
}
