package com.cmd.hit.zhihudaily.ui.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by PC-0775 on 2019/5/6.
 */

public class MyScrollView extends NestedScrollView {
    private int calCount = 0;


    private OnScrollBottomListener listener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollBottomListener(OnScrollBottomListener listener){
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
//        super.onScrollChanged(l, t, oldl, oldt);
        View view = this.getChildAt(0);
        if (getHeight() + getScrollY() == view.getHeight()){
            calCount++;
            if (1 == calCount){
                if (listener != null){
                    listener.scrollToBottom();
                }
            }
        }else {
            calCount = 0;
        }
    }

    public interface  OnScrollBottomListener{
        void scrollToBottom();
    }
}
