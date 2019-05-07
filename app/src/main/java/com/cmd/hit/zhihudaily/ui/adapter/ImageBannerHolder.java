package com.cmd.hit.zhihudaily.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.ui.view.ImageBannerFarmLayout;

/**
 * Created by PC-0775 on 2019/5/7.
 */

public class ImageBannerHolder extends RecyclerView.ViewHolder {

    private ImageBannerFarmLayout ibfl_item;

    public ImageBannerHolder(View itemView) {
        super(itemView);
        ibfl_item = itemView.findViewById(R.id.ibfl_item);
    }
}
