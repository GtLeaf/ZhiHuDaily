package com.cmd.hit.zhihudaily.viewModel;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.cmd.hit.zhihudaily.model.bean.BeforeNews;
import com.cmd.hit.zhihudaily.model.bean.LatestNews;
import com.cmd.hit.zhihudaily.model.bean.News;
import com.cmd.hit.zhihudaily.model.repository.NewsRepository;
import com.cmd.hit.zhihudaily.other.PhotoCacheHelper;
import com.cmd.hit.zhihudaily.ui.MainActivity;

import io.reactivex.Observable;

/**
 * Created by PC-0775 on 2019/4/26.
 */

public class MainViewModel {

    //News的数据仓库
    private NewsRepository repository;
    private PhotoCacheHelper photoCacheHelper;

    public MainViewModel(NewsRepository repository, PhotoCacheHelper photoCacheHelper){
        this.repository = repository;
        this.photoCacheHelper = photoCacheHelper;
    }


    /**
     * 获取图片
     * @param url
     * @param imageView
     */
    public void loadBitmap(String url, ImageView imageView){
        photoCacheHelper.loadBitmap(url, imageView);
    }

    public void loadBitmap(String url, int what){
        photoCacheHelper.loadBitmap(url, what);
    }

    public Observable<LatestNews> getLatestNewsObservable(){
        return repository.getLatestNews();
    }
    public Observable<BeforeNews> getBeforeNewsObservable(String key){
        return repository.getBeforeNews(key);
    }
    public Observable<News> getNewsObservable(int newsId){
        return repository.getNewsItem(newsId);
    }


}
