package com.cmd.hit.zhihudaily.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.model.local.dao.NewsDao;
import com.cmd.hit.zhihudaily.model.remote.ServiceCreator;
import com.cmd.hit.zhihudaily.model.remote.api.NewsService;
import com.cmd.hit.zhihudaily.model.repository.NewsRepository;
import com.cmd.hit.zhihudaily.other.PhotoCacheHelper;
import com.cmd.hit.zhihudaily.viewModel.NewsContentViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by PC-0775 on 2019/5/6.
 */

public class NewsContentActivity extends AppCompatActivity{

    //view
    private WebView wv_newsContent;
    private ImageView iv_newsContentTitle;
    private Toolbar tb_newsContent;
    private CollapsingToolbarLayout ctl_newsContent;
    private TextView tv_newsContentTitle;

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
        iv_newsContentTitle = findViewById(R.id.iv_news_content_title);
        tb_newsContent = findViewById(R.id.tb_news_content);
        ctl_newsContent = findViewById(R.id.ctl_news_content);
        tv_newsContentTitle = findViewById(R.id.tv_news_content_title);
        initToolBar();

        model = new NewsContentViewModel(new NewsRepository(new NewsDao()
                , ServiceCreator.getInstance().create(NewsService.class)));
        setWebView();
    }

    private void setWebView(){
        wv_newsContent.getSettings().setJavaScriptEnabled(true);
        wv_newsContent.getSettings().setDefaultFontSize(16);
        wv_newsContent.setWebViewClient(new WebViewClient());
    }

    private void setListener(){

    }

    private void doBusiness(){
        model.getNewsObservable(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                    wv_newsContent.loadDataWithBaseURL(null, changeRowPitch(news.getBody(), 20), "text/html"
                            , "utf-8", null);
                    tv_newsContentTitle.setText(news.getTitle());
                    PhotoCacheHelper.getInstance().loadBitmap(news.getImage(), iv_newsContentTitle);
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

    private String changeRowPitch(String htmlData, int rowPitch)
    {
        htmlData = htmlData.replace("<img", "<img style ='max-width:90%;height:auto'");

        htmlData = "<html> \n" +
                "<head> \n" +
                "<style type=\"text/css\"> \n" +
                "body {text-align:justify; line-height: "+(rowPitch+6)+"px}\n" +
                "</style> \n" +
                "</head> \n" +
                "<body>"+htmlData+"</body> ";
        return htmlData;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    /*
    * 设置toolbar
    * */
    private void initToolBar()
    {
        setSupportActionBar(tb_newsContent);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }
}
