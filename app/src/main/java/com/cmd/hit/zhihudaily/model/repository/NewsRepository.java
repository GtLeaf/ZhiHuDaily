package com.cmd.hit.zhihudaily.model.repository;

import com.cmd.hit.zhihudaily.model.bean.News;
import com.cmd.hit.zhihudaily.model.local.dao.NewsDao;
import com.cmd.hit.zhihudaily.model.remote.api.NewsService;

/**
 * Created by PC-0775 on 2019/4/29.
 */

public class NewsRepository {
    private NewsDao dao;
    private NewsService network;
    private
    NewsRepository(NewsDao dao, NewsService network){
        this.dao = dao;
        this.network = network;
    }

    public News getNewsItem(String newsId){

        return null;
    }

}
