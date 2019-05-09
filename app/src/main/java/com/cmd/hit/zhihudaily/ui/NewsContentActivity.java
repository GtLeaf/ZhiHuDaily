package com.cmd.hit.zhihudaily.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.model.local.dao.NewsDao;
import com.cmd.hit.zhihudaily.model.remote.ServiceCreator;
import com.cmd.hit.zhihudaily.model.remote.api.NewsService;
import com.cmd.hit.zhihudaily.model.repository.NewsRepository;
import com.cmd.hit.zhihudaily.viewModel.NewsContentViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by PC-0775 on 2019/5/6.
 */

public class NewsContentActivity extends AppCompatActivity{

    private WebView wv_newsContent;
    private NewsContentViewModel model;


    private int newsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        setContentView(R.layout.activity_news_content);
        init();
        setListener();
        doBusiness();
    }

    private void initParams(){
        newsId = getIntent().getIntExtra("newsId", 0);
    }

    private void init(){
        wv_newsContent = findViewById(R.id.wv_news_content);
        model = new NewsContentViewModel(new NewsRepository(new NewsDao()
                , ServiceCreator.getInstance().create(NewsService.class)));
    }

    private void setWebView(){

    }

    private void setListener(){

    }

    private void doBusiness(){
        setWebView();
        model.getNewsObservable(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                    wv_newsContent.loadDataWithBaseURL(null, news.getBody(), "text/html"
                            , "utf-8", null);
                });

    }

    public static void actionStart(int newsId, Context context){
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("newsId", newsId);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyWebView();
    }

    private void destroyWebView(){
        if (wv_newsContent != null){
            wv_newsContent.destroy();
            wv_newsContent = null;
        }

    }
}
