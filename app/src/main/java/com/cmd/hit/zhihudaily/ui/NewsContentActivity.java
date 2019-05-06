package com.cmd.hit.zhihudaily.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.cmd.hit.zhihudaily.R;

/**
 * Created by PC-0775 on 2019/5/6.
 */

public class NewsContentActivity extends AppCompatActivity{

    private WebView wv_newsContent;

    private int newsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    private void setWebView(){

    }

    private void setListener(){

    }

    private void doBusiness(){


        setWebView();

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
