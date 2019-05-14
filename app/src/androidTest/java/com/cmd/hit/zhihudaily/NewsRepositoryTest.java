package com.cmd.hit.zhihudaily;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.cmd.hit.zhihudaily.model.bean.BeforeNews;
import com.cmd.hit.zhihudaily.model.bean.LatestNews;
import com.cmd.hit.zhihudaily.model.bean.News;
import com.cmd.hit.zhihudaily.model.local.dao.NewsDao;
import com.cmd.hit.zhihudaily.model.remote.ServiceCreator;
import com.cmd.hit.zhihudaily.model.remote.api.NewsService;
import com.cmd.hit.zhihudaily.model.repository.NewsRepository;
import com.cmd.hit.zhihudaily.other.LogUtil;
import com.cmd.hit.zhihudaily.other.SPUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by PC-0775 on 2019/5/1.
 */
@RunWith(JUnit4.class)
public class NewsRepositoryTest {
    private static final String TAG = "NewsRepositoryTest";

    private Context context;


    @Before
    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
    }

    /*
    * 获取单个消息测试
    * */
    @Test
    public void getNewsTest(){
        SPUtil.setContext(context);
        NewsRepository newsRepository = new NewsRepository(new NewsDao(), ServiceCreator.getInstance().create(NewsService.class));
        newsRepository.getNewsItem(9710689)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<News>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(News news) {
                        Log.d(TAG, news.getTitle());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {}
                });
        LogUtil.d(TAG, "finish");
    }

    /*
    * 获取本地单个消息测试
    * */
    @Test
    public void getLocalNewsTest(){
        SPUtil.setContext(context);
        String newsInfo = SPUtil.get("9454158", "");
        LogUtil.d(TAG, newsInfo);
    }
    @Test
    public void dateTest(){
        String key = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date());
        LogUtil.d(TAG, key);
    }
    /*
    * 获取最新消息测试
    * */
    @Test
    public void getLatestNews(){
        SPUtil.setContext(context);
        NewsRepository newsRepository = new NewsRepository(new NewsDao(), ServiceCreator.getInstance().create(NewsService.class));
        newsRepository.getLatestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LatestNews>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LatestNews latestNews) {
                        Log.d(TAG, latestNews.getDate());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        LogUtil.d(TAG, "finish");
    }

    /**
     * 获取过往消息
     */
    @Test
    public void getBeforeNewsTest(){
        SPUtil.setContext(context);
        NewsRepository newsRepository = new NewsRepository(new NewsDao(), ServiceCreator.getInstance().create(NewsService.class));
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH,-2);
        String key = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(currentDate.getTime());
        newsRepository.getBeforeNews(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BeforeNews>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BeforeNews beforeNews) {
                        Log.d(TAG, beforeNews.getDate());
                        Log.d(TAG, beforeNews.getStories().get(0).getTitle());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        LogUtil.d(TAG, "finish");
    }

    /*
    * 获取本地最先消息测试
    * */
    @Test
    public void getCacheLatestNews(){
        String key = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date());
        SPUtil.setContext(context);
        String LatestNewsInfo = SPUtil.get(key, "");
        LogUtil.d(TAG, LatestNewsInfo);
    }
}
