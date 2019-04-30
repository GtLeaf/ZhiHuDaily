package com.cmd.hit.zhihudaily.model.local.dao;

import android.preference.PreferenceManager;
import android.util.ArrayMap;

import com.cmd.hit.zhihudaily.model.bean.News;
import com.cmd.hit.zhihudaily.other.SPUtil;
import com.cmd.hit.zhihudaily.ui.ZhiHuApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC-0775 on 2019/4/29.
 */

public class NewsDao {
    //key映射对应的id
    private HashMap<String, String> newsKey2Id = new HashMap<>();
    //id 1,2,3,4..映射对应的key
    private HashMap<String, String> newsId2key = new HashMap<>();

    /*
    * 缓存新闻消息
    * */
    public void cacheNews(News news){
        if (null == news) return;
        String newsInfo = new Gson().toJson(news);
        SPUtil.put(String.valueOf(news.getId()), newsInfo);
    }

    /*
    * 读取一个新闻消息
    * */
    public News getCacheNews(String key){
        String newsInfo = SPUtil.get(key, "");
        if (null != newsInfo){
            return new Gson().fromJson(newsInfo, News.class);
        }
        return null;
    }


    public void cacheNewsPic(){

    }
}
