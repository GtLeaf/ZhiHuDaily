package com.cmd.hit.zhihudaily.model.repository;

import android.content.Context;

import com.cmd.hit.zhihudaily.model.bean.LatestNews;
import com.cmd.hit.zhihudaily.model.bean.News;
import com.cmd.hit.zhihudaily.model.local.dao.NewsDao;
import com.cmd.hit.zhihudaily.model.remote.api.NewsService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;

/**
 * Created by PC-0775 on 2019/4/29.
 */

public class NewsRepository {
    private NewsDao dao;
    private NewsService network;

    public NewsRepository(NewsDao dao, NewsService network){
        this.dao = dao;
        this.network = network;
    }

    //获取单个新闻
    public Observable<News> getNewsItem(final int newsId){
        //访问本地数据
        return dao.getCacheNews(newsId+"")
                .onErrorResumeNext(
                        network.getNews(newsId)//本地数据为空，获取网络数据
                                .doOnNext(news -> dao.cacheData(news.getId()+"", news)));
    }

    //获取某日新闻摘要
    public Observable<LatestNews> getLatestNews(String key){
        //组装key
        return dao.getCache(key, LatestNews.class)
                .onErrorResumeNext(network.getLatestNews()
                        .doOnNext(latestNews -> dao.cacheData(latestNews.getDate(), latestNews))
                );
    }
}
