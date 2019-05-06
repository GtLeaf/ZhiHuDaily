package com.cmd.hit.zhihudaily.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.model.bean.LatestNews;
import com.cmd.hit.zhihudaily.model.bean.News;
import com.cmd.hit.zhihudaily.model.local.dao.NewsDao;
import com.cmd.hit.zhihudaily.model.remote.ServiceCreator;
import com.cmd.hit.zhihudaily.model.remote.api.NewsService;
import com.cmd.hit.zhihudaily.model.repository.NewsRepository;
import com.cmd.hit.zhihudaily.other.PhotoCacheHelper;
import com.cmd.hit.zhihudaily.other.SPUtil;
import com.cmd.hit.zhihudaily.ui.view.ImageBannerFarmLayout;
import com.cmd.hit.zhihudaily.viewModel.MainActivityModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by PC-0775 on 2019/4/26.
 */
//链接MUMU模拟器的命令行，命令：adb connect 127.0.0.1:7555
public class MainActivity extends AppCompatActivity implements ImageBannerFarmLayout.FramLayoutListener {

    //view
    private ImageBannerFarmLayout mGroup;
    private TextView tv_offlineDownload;

    //resourcess
    private List<Bitmap> topBitmapList = new ArrayList<>();
    private List<Bitmap> bitmapList = new ArrayList<>();
    private List<News> topNewsList = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();
    private LatestNews latestNews;

    //model
    private MainActivityModel model;

    //what
    public static final int NEWS = 1;
    public static final int TOP_NEWS = 2;
    public static final int TOP_COVER_IMAGE = 3;
    public static final int COVER_IMAGE = 4;

    //Handler
    MyHandler handler = new MyHandler(this);
    static class MyHandler extends Handler{
        WeakReference<MainActivity> weakReference;

        MyHandler(MainActivity mainActivity){
            weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = weakReference.get();

            switch (msg.what){
                case TOP_NEWS:
                    break;
                case NEWS:
                    break;
                case TOP_COVER_IMAGE:
                    Bitmap topBitmap = (Bitmap) msg.obj;
                    activity.mGroup.addBitmap(topBitmap);
                    break;
                case COVER_IMAGE:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    activity.bitmapList.add(bitmap);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListener();
        doBusiness();
       /* for(int i = 0; i < imageUrl.length; i++){
            //拿到图片资源
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[i]);
            //将图片添加到list中
            list.add(bitmap);
            //可以再添加一个list存放图片的索引id
        }*/


    }

    private void init(){
        //设置图片完全填充
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ImageBannerFarmLayout.WIDTH = dm.widthPixels;

        //轮播图
        mGroup = findViewById(R.id.image_group);
        tv_offlineDownload = findViewById(R.id.tv_offline_download);

        //SPUtil
        SPUtil.setContext(this);

        //model
        NewsDao dao = new NewsDao();
        NewsService service = ServiceCreator.getInstance().create(NewsService.class);
        NewsRepository repository = new NewsRepository(dao, service);
        PhotoCacheHelper helper = new PhotoCacheHelper(this, handler);
        model = new MainActivityModel(repository, helper);
    }

    private void setListener(){
        mGroup.setListener(this);//设置点击事件
        //离线缓存点击事件
        tv_offlineDownload.setOnClickListener(v -> {
            //轮播新闻id
            List<Integer> topNewsId = new ArrayList<>();
            //普通新闻id
            List<Integer> newsId = new ArrayList<>();

            for (LatestNews.TopStoriesBean bean : latestNews.getTop_stories()){
                //获取图片
                model.loadBitmap(bean.getImage(), TOP_COVER_IMAGE);
                topNewsId.add(bean.getId());
            }
            for (LatestNews.StoriesBean bean : latestNews.getStories()){
                //获取图片
                model.loadBitmap(bean.getImages().get(0), COVER_IMAGE);
                newsId.add(bean.getId());
            }
            requestNews(topNewsId, topNewsList);
            requestNews(newsId, newsList);
        });
    }

    private void doBusiness(){
        //请求新闻摘要
        model.getLatestNewsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(latestNews -> {
                    this.latestNews = latestNews;
                    for (LatestNews.TopStoriesBean bean : latestNews.getTop_stories()){
                        //获取轮播图片
                        model.loadBitmap(bean.getImage(), TOP_COVER_IMAGE);
                    }
                    for (LatestNews.StoriesBean bean : latestNews.getStories()){
                        //获取图片
                        model.loadBitmap(bean.getImages().get(0), COVER_IMAGE);
                    }
                });

    }

    @Override
    public void clickImageIndex(int pos) {
        Toast.makeText(this,"pos=" + pos, Toast.LENGTH_SHORT).show();
    }


    /**
     * 根据id获取news
     * @param newsIdList    news的ID
     * @param newsList      news
     */
    public void requestNews(List<Integer> newsIdList, List<News> newsList){
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            for (int id : newsIdList){
                emitter.onNext(id);
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap(integer -> model.getNewsObservable(integer))
        .subscribe(newsList::add);
    }


}
