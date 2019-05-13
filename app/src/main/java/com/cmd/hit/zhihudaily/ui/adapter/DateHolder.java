package com.cmd.hit.zhihudaily.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.ui.bean.DateBean;

public class DateHolder extends RecyclerView.ViewHolder {
    private TextView date;
    private TextView week;
    public DateHolder(View itemView) {
        super(itemView);
        date = itemView.findViewById(R.id.news_date);
        week = itemView.findViewById(R.id.news_week);
    }
    public void bindHolder(DateBean bean){
        date.setText(bean.getDate());
        week.setText(bean.getWeek());
    }
}
