package com.cmd.hit.zhihudaily.ui.view;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class ImageBarnnerViewGroup extends ViewGroup {

    private int children_Number = 0;//子视图个数
    private int child_Height = 0;//子视图高度
    private int child_Weight = 0;//子视图宽度

    private  int x;//当前的x的值，代表 第一次 按下的位置的横坐标。每一次移动过程中 移动之前的位置横坐标
    private  int index = 0;//代表此时图片的索引。

    private Scroller scroller;

    //实现单击
    private boolean isClick;


    public interface ImageBarnnerListener{
        void clickImageIndex(int pos);//当前图片索引
    }
    private ImageBarnnerListener listener;
    public ImageBarnnerListener getListener(){
        return listener;
    }
    public void setListener(ImageBarnnerListener listener){
        this.listener = listener;
    }



    private ImageBarnnerViewGroupListener barnnerViewGroupListener;

    public ImageBarnnerViewGroupListener getBarnnerViewGroupListener() {
        return barnnerViewGroupListener;
    }

    public void setBarnnerViewGroupListener(ImageBarnnerViewGroupListener barnnerViewGroupListener) {
        this.barnnerViewGroupListener = barnnerViewGroupListener;
    }

    /**自动轮播
     */
    private boolean isAuto = true;
    private Timer timer = new Timer();
    private TimerTask task;
    private Handler autoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0://实现轮播
                    if(++index >= children_Number){//到最后一张是返回第一张
                        index = 0;
                    }
                    scrollTo(child_Weight * index , 0);
                    barnnerViewGroupListener.selectImage(index);
                    break;
                    default:break;
            }
        }
    };
    private void startAuto(){
        isAuto = true;
    }
    private void stopAuto(){
        isAuto = false;
    }

    public ImageBarnnerViewGroup(Context context) {
        super(context);
        initObj();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initObj();
    }

    public ImageBarnnerViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initObj();
    }

    private  void  initObj(){
        scroller = new Scroller(getContext());

        task = new TimerTask() {
            @Override
            public void run() {
                if(isAuto){
                    autoHandler.sendEmptyMessage(0);
                }
            }
        };
        timer.schedule(task,100,2000);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            invalidate();
        }
    }

    //进行：测量-》布局-》绘制
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //求子视图的个数
        children_Number = getChildCount();
        if( 0 == children_Number){
            setMeasuredDimension(0,0);
        }else {
            //测量子视图的高度和宽度
            measureChildren(widthMeasureSpec,heightMeasureSpec);

            //根据子视图的高度和宽度，求该容器的宽度和高度
            View view = getChildAt(0);
            child_Height = view.getMeasuredHeight();
            child_Weight = view.getMeasuredWidth();
            int width  = child_Weight * children_Number;//容器的宽度
            setMeasuredDimension(width, child_Height);
        }
    }

    /**
    * 事件的传递过程中调用的方法。为TRUE时，介绍事件处理，false
     * 当返回TRUE是，处理事件的方法为onTouchEvent
    * */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**处理事件
     * 1. 使用scrollTo scrollBy 完成手动轮播
     * 2. 使用scroller对象 完成手动轮播
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN://用户按下
                stopAuto();
                if(!scroller.isFinished()){
                    scroller.abortAnimation();
                }
                isClick = true;
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE://按下后移动
                int moveX = (int)event.getX();
                int distance = moveX - x;
                scrollBy(-distance, 0);
                x = moveX;
                isClick = false;
                break;
            case MotionEvent.ACTION_UP://用户抬起
                int scrollX = getScrollX();
                index = (scrollX + child_Weight / 2)/child_Weight;
                if(index < 0){//到了最左边的第一张
                    index = 0;
                }else if(index > children_Number - 1){//到了最右边的第一张
                    index = children_Number -1;
                }
                //点击事件
                if(isClick){
                    listener.clickImageIndex(index);
                }else {
                    //scrollTo(index * child_Weight, 0);
                    int dx = index*child_Weight - scrollX ;//移动距离
                    scroller.startScroll(scrollX, 0, dx, 0);
                    postInvalidate();//重绘
                    barnnerViewGroupListener.selectImage(index);
                    barnnerViewGroupListener.selectText(index);
                }
                startAuto();
                break;
                default:break;
        }
        return true;
    }

    /**实现布局
    * @param bool  当viewGroup布局位置发送改变时为True,否则false不接受时间，向下传递
     * @
    * */
    @Override
    protected void onLayout(boolean bool, int l, int t, int r, int b) {
        if(bool){
            int leftMargin = 0;
            for(int i = 0; i < children_Number; i++){
                View view = getChildAt(i);
                view.layout(leftMargin,0, leftMargin + child_Weight, child_Height);
                leftMargin += child_Weight;
            }
        }
    }

    public interface ImageBarnnerViewGroupListener{
        void selectImage(int index);
        void selectText(int index);
    }

}

