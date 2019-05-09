package com.cmd.hit.zhihudaily.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by PC-0775 on 2019/5/7.
 */

public class HeaderAndFooterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    //header or footer
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter){
        mInnerAdapter = adapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null){

//            return new ImageBannerHolder(mHeaderViews.get(viewType));
            return new TextHolder(mHeaderViews.get(viewType));
        }else if (mFootViews.get(viewType) != null){
            return new ImageBannerHolder(mHeaderViews.get(viewType));
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)){
            return;
        }
        if (isFooterViewPos(position)){
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)){
            return mHeaderViews.keyAt(position);
        }else if (isFooterViewPos(position)){
            return mFootViews.keyAt(position - getRealItemCount() - getFootersCount());
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    public boolean isHeaderViewPos(int position){
        return position < getHeadersCount();
    }

    public boolean isFooterViewPos(int position){
        return position >= getHeadersCount() + getRealItemCount();
    }

    /**
     * 添加展示在头部的view
     * @param view 需添加的View
     */
    public void addHeaderView(View view){
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }


    /**
     * 添加展示在尾部的view
     * @param view 需添加的View
     */
    public void addFooterView(View view){
        mHeaderViews.put(mFootViews.size() + BASE_ITEM_TYPE_HEADER, view);
    }

    /**
     * 获取headerView的数量
     * @return  headerView的数量
     */
    public int getHeadersCount(){
        return mHeaderViews.size();
    }

    /**
     * 获取footerView的数量
     * @return  footerView的数量
     */
    public int getFootersCount(){
        return mFootViews.size();
    }

    /**
     * 获取除头、尾view外，中间itemView的数量
     * @return itemView的数量
     */
    public int getRealItemCount(){
        return mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemCount() {
        return mHeaderViews.size() + mInnerAdapter.getItemCount() + mFootViews.size();
    }
}
