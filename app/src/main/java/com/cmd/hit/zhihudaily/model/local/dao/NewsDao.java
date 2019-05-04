package com.cmd.hit.zhihudaily.model.local.dao;

import android.arch.persistence.room.EmptyResultSetException;

import com.cmd.hit.zhihudaily.model.bean.LatestNews;
import com.cmd.hit.zhihudaily.model.bean.News;
import com.cmd.hit.zhihudaily.other.SPUtil;
import com.cmd.hit.zhihudaily.ui.ZhiHuApplication;
import com.google.gson.Gson;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by PC-0775 on 2019/4/29.
 */

public class NewsDao {
    private Gson gson;

    public NewsDao(){
        this.gson = new Gson();
    }

    /*
    * 读取一个新闻消息
    * */
    public Observable<News> getCacheNews(String key){
        String newsInfo = SPUtil.get(key, "");
        if (  !newsInfo.equals("")){
            return Observable.just(gson.fromJson(newsInfo, News.class));
        }else {
            return Observable.error(new EmptyResultSetException("result is null"));
        }
    }

    /*
    * 获取最新消息
    * */
    public <T> Observable<T> getCache(String key, Class<T> classOfT){
        String objInfo = SPUtil.get(key, "");
        if (!objInfo.equals("")){
            return Observable.just(gson.fromJson(objInfo, classOfT));
        }else {
            return Observable.error(new EmptyResultSetException("result is null"));
        }
    }

    /*
    * 缓存数据
    * */
    public <T> void cacheData(String key, T t){
        if (null == t) return;
        String objInfo = gson.toJson(t);
        SPUtil.put(key, objInfo);
    }

    /*
    * 缓存图片
    * */
    public void cacheNewsPic(){

    }

    /*
    * 获取图片
    * */
    public Observable<Response<String>> getNewsPic(String url){
        return null;
    }

}
