package com.cmd.hit.zhihudaily.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.ui.bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter {

    private List<NewsBean> newsList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_tem,parent,false);
        RecyclerView.ViewHolder holder = new NewsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((NewsHolder)holder).bindHolder(newsList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public NewsAdapter() {
        this.newsList = new ArrayList<>();
    }

    public void addNews(NewsBean news){
        this.newsList.add(news);
        notifyItemInserted(this.newsList.size()-1);
    }
}
