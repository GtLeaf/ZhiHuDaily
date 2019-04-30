package com.cmd.hit.zhihudaily.model.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by PC-0775 on 2019/4/29.
 */

public class ServiceCreator {

    private ServiceCreator instance;

    private static final String BASE_URL = "https://news-at.zhihu.com";

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private ServiceCreator(){}

    public ServiceCreator getInstance() {
        if (null == instance){
            synchronized (ServiceCreator.class){
                if (null == instance){
                    instance = new ServiceCreator();
                }
            }
        }
        return instance;
    }

    public void setInstance(ServiceCreator instance) {
        this.instance = instance;
    }

    public <T> T create(Class<T> serviceClass){
        return retrofit.create(serviceClass);
    }
}
