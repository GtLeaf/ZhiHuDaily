package com.cmd.hit.zhihudaily.ui.view;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cmd.hit.zhihudaily.R;

import java.util.List;

public class ImageBannerFarmLayout extends FrameLayout implements ImageBarnnerViewGroup.ImageBarnnerViewGroupListener,ImageBarnnerViewGroup.ImageBarnnerListener {

    private ImageBarnnerViewGroup imageBarnnerViewGroup;
    private LinearLayout linearLayout;

    public static int WIDTH = 0;

    private FramLayoutListener listener;

    public FramLayoutListener getListener() {
        return listener;
    }

    public void setListener(FramLayoutListener listener) {
        this.listener = listener;
    }

    public ImageBannerFarmLayout(@NonNull Context context) {
        super(context);
        initImageBarnnerViewGroup();
        initDotLinearlayout();
    }

    public ImageBannerFarmLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initImageBarnnerViewGroup();
        initDotLinearlayout();
    }

    public ImageBannerFarmLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImageBarnnerViewGroup();
        initDotLinearlayout();
    }

    public void addBitmaps(List<Bitmap> list){
        for (int i = 0; i < list.size(); i++){
            Bitmap bitmap = list.get(i);
            addBitmapToImageBarnnerViewGroup(bitmap);
            addDotToLinearlayout();
        }
    }

    private void addDotToLinearlayout(){
        ImageView iv = new ImageView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5,5,5,5);
        iv.setLayoutParams(lp);
        iv.setImageResource(R.drawable.dot_normal);
        linearLayout.addView(iv);

    }

    private void addBitmapToImageBarnnerViewGroup(Bitmap bitmap){
        ImageView iv = new ImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setLayoutParams(new ViewGroup.LayoutParams(WIDTH,ViewGroup.LayoutParams.WRAP_CONTENT));
        iv.setImageBitmap(bitmap);
        imageBarnnerViewGroup.addView(iv);
    }

    //初始化自定义的图片轮播功能核心类
    private void initImageBarnnerViewGroup(){
        imageBarnnerViewGroup = new ImageBarnnerViewGroup(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        imageBarnnerViewGroup.setLayoutParams(lp);
        imageBarnnerViewGroup.setBarnnerViewGroupListener(this);
        imageBarnnerViewGroup.setListener(this);
        addView(imageBarnnerViewGroup);
    }

    //初始化底部原点
    private void initDotLinearlayout(){
        linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                40);
        linearLayout.setLayoutParams(lp);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        //linearLayout.setBackgroundColor(Color.RED);
        addView(linearLayout);

        //重新设置布局
        FrameLayout.LayoutParams layoutParams = (LayoutParams)linearLayout.getLayoutParams();
        layoutParams.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(layoutParams);

        //设置透明度
        linearLayout.setAlpha(0.5f);
    }

    @Override
    public void selectImage(int index) {
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count ; i++){
            ImageView iv = (ImageView)linearLayout.getChildAt(i);
            if( i == index){
                iv.setImageResource(R.drawable.dot_select);
            }else {
                iv.setImageResource(R.drawable.dot_normal);
            }
        }
    }

    @Override
    public void clickImageIndex(int pos) {
        listener.clickImageIndex(pos);
    }

    public interface FramLayoutListener{
        void clickImageIndex(int pos);
    }
}
