package com.cmd.hit.zhihudaily.model.remote.api;

import com.cmd.hit.zhihudaily.model.bean.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by PC-0775 on 2019/4/29.
 */

public interface NewsService {

    @GET("/api/4/news/{newsId}")
    Call<News> getLatestNews(@Path("newsId") int newsId);

}
