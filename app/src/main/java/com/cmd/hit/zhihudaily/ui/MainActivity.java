package com.cmd.hit.zhihudaily.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.model.bean.BeforeNews;
import com.cmd.hit.zhihudaily.model.bean.LatestNews;
import com.cmd.hit.zhihudaily.model.bean.News;
import com.cmd.hit.zhihudaily.model.local.dao.NewsDao;
import com.cmd.hit.zhihudaily.model.remote.ServiceCreator;
import com.cmd.hit.zhihudaily.model.remote.api.NewsService;
import com.cmd.hit.zhihudaily.model.repository.NewsRepository;
import com.cmd.hit.zhihudaily.other.PhotoCacheHelper;
import com.cmd.hit.zhihudaily.other.SPUtil;
import com.cmd.hit.zhihudaily.ui.Setting.SettingActivity;
import com.cmd.hit.zhihudaily.ui.adapter.HeaderAndFooterWrapper;
import com.cmd.hit.zhihudaily.ui.adapter.NewsAdapter;
import com.cmd.hit.zhihudaily.ui.bean.DateBean;
import com.cmd.hit.zhihudaily.ui.bean.NewsBean;
import com.cmd.hit.zhihudaily.ui.listener.RecyclerViewScrollListener;
import com.cmd.hit.zhihudaily.ui.view.ImageBannerFarmLayout;
import com.cmd.hit.zhihudaily.viewModel.MainViewModel;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by PC-0775 on 2019/4/26.
 */
//链接MUMU模拟器的命令行，命令：adb connect 127.0.0.1:7555
public class MainActivity extends AppCompatActivity{

    private static WeakReference<MainActivity> instance;
    public static MainActivity getInstance(){
        return instance.get();
    }

    //view
    private ImageBannerFarmLayout mGroup;
    private TextView tv_offlineDownload;
    private NavigationView nav_headerView;
    private DrawerLayout drawer_layout;

    ActionBarDrawerToggle mDrawerToggle;

    //resourcess
    private List<Bitmap> topBitmapList = new ArrayList<>();
    private List<Bitmap> bitmapList = new ArrayList<>();
    private List<News> topNewsList = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();
    private LatestNews mLatestNews;

    //model
    private MainViewModel model;

    //what
    public static final int NEWS = 1;
    public static final int TOP_NEWS = 2;
    public static final int TOP_COVER_IMAGE = 3;
    public static final int COVER_IMAGE = 4;

    private RecyclerView newsListRecyclerView;
    private NewsAdapter newsAdapter;
    private Calendar currentDate = Calendar.getInstance();
    private HeaderAndFooterWrapper headerAndFooterWrapper;

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
                    activity.mGroup.addBitmap(topBitmap,"");//添加文本

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

        instance = new WeakReference<>(this);

        init();
        setListener();
        doBusiness();
    }

    private void init(){
        //toolbar
        Toolbar toolbar = findViewById(R.id.tl_custom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar){
            actionBar.setTitle("首页");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //设置图片完全填充
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        ImageBannerFarmLayout.WIDTH = dm.widthPixels;

        //侧滑栏头部
        nav_headerView = findViewById(R.id.nav_header_view);
        //侧滑栏
        drawer_layout = findViewById(R.id.drawer_layout);
        //离线下载按钮
        tv_offlineDownload = nav_headerView.getHeaderView(0).findViewById(R.id.tv_offline_download);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar
                ,R.string.abc_capital_on , R.string.abc_capital_off);

        drawer_layout.addDrawerListener(mDrawerToggle);

        //SPUtil
        SPUtil.setContext(this);

        //model
        NewsDao dao = new NewsDao();
        NewsService service = ServiceCreator.getInstance().create(NewsService.class);
        NewsRepository repository = new NewsRepository(dao, service);
        PhotoCacheHelper helper = new PhotoCacheHelper(this, handler);
        model = new MainViewModel(repository, helper);

        //初始化recyclerView
        newsListRecyclerView = findViewById(R.id.rv_home_list);

        newsAdapter = new NewsAdapter();

        headerAndFooterWrapper = new HeaderAndFooterWrapper(newsAdapter);

        mGroup = new ImageBannerFarmLayout(this);

        headerAndFooterWrapper.addHeaderView(mGroup);

        newsListRecyclerView.setAdapter(headerAndFooterWrapper);
        newsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsListRecyclerView.addOnScrollListener(new RecyclerViewScrollListener(){
            @Override
            public void onScrollToBottom() {
                // 加载更多
                addNewsToRecyclerView();
            }
        });
    }

    //设置事件监听
    private void setListener(){
        //设置轮播图点击事件
        mGroup.setListener(pos -> {
            Toast.makeText(this,"pos=" + pos, Toast.LENGTH_SHORT).show();
            NewsContentActivity.actionStart(mLatestNews.getTop_stories().get(pos).getId(), this);
        });
        //离线缓存点击事件
        tv_offlineDownload.setOnClickListener(v -> {
            //轮播新闻id
            List<Integer> topNewsId = new ArrayList<>();
            //普通新闻id
            List<Integer> newsId = new ArrayList<>();

            for (LatestNews.TopStoriesBean bean : mLatestNews.getTop_stories()){
                //获取图片,缓存在本地即可
                model.loadBitmap(bean.getImage(), 0);
                topNewsId.add(bean.getId());
            }
            for (LatestNews.StoriesBean bean : mLatestNews.getStories()){
                //获取图片,缓存在本地即可
                model.loadBitmap(bean.getImages().get(0), 0);
                newsId.add(bean.getId());
            }
            requestNews(topNewsId, topNewsList);
            requestNews(newsId, newsList);
        });
    }

    private void addNewsToRecyclerView(){
        currentDate.add(Calendar.DAY_OF_MONTH,-1);
        String key = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(currentDate.getTime());
        DateBean dateBean = new DateBean(new SimpleDateFormat("yyyy/MM/dd").format(currentDate.getTime()),
                new SimpleDateFormat("EEEE").format(currentDate.getTime()));

        model.getBeforeNewsObservable(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BeforeNews>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BeforeNews beforeNews) {
                        newsAdapter.addNews(dateBean);
                        for(int i=0;i<beforeNews.getStories().size();i++) {
                            NewsBean news = new NewsBean(beforeNews.getStories().get(i).getId(),
                                    beforeNews.getStories().get(i).getTitle(),
                                    beforeNews.getStories().get(i).getImages().get(0));
                            newsAdapter.addNews(news);
                            headerAndFooterWrapper.notifyItemChanged(newsAdapter.getItemCount());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void doBusiness(){
        String key = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date());
        //请求新闻摘要
        DateBean dateBean = new DateBean("今日热闻","");
        newsAdapter.addNews(dateBean);
        model.getLatestNewsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LatestNews>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LatestNews latestNews) {
                        mLatestNews = latestNews;
                        for (LatestNews.TopStoriesBean bean : latestNews.getTop_stories()){
                            //获取轮播图片
                            model.loadBitmap(bean.getImage(), TOP_COVER_IMAGE);
                        }
                        for (LatestNews.StoriesBean bean : latestNews.getStories()){
                            //获取图片
                            model.loadBitmap(bean.getImages().get(0), COVER_IMAGE);
                        }
                        for(int i=0;i<latestNews.getStories().size();i++){
                            NewsBean news = new NewsBean(latestNews.getStories().get(i).getId(),
                                    latestNews.getStories().get(i).getTitle(),
                                    latestNews.getStories().get(i).getImages().get(0));
                            newsAdapter.addNews(news);
                            headerAndFooterWrapper.notifyItemChanged(newsList.size()-1);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 根据id获取news
     * @param newsIdList    news的ID
     * @param newsList      news
     */
    @SuppressLint("CheckResult")
    public void requestNews(List<Integer> newsIdList, List<News> newsList){
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            for (int id : newsIdList){
                emitter.onNext(id);
            }
            emitter.onComplete();
        })
        .flatMap(integer -> model.getNewsObservable(integer))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<News>() {
            @Override
            public void onSubscribe(Disposable d) {
                Toast.makeText(MainActivity.this, "开始缓存", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(News news) {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                Toast.makeText(MainActivity.this, "缓存完成", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.home_setting:
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        mDrawerToggle.syncState();
        super.onPostCreate(savedInstanceState);
    }
}
