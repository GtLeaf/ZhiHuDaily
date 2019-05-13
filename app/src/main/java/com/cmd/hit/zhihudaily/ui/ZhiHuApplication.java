package com.cmd.hit.zhihudaily.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.cmd.hit.zhihudaily.other.PhotoCacheHelper;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by PC-0775 on 2019/4/29.
 */

public class ZhiHuApplication extends Application {

    private static SoftReference<Context> contextSoftReference;

    @Override
    public void onCreate() {
        super.onCreate();
        contextSoftReference = new SoftReference<>(getApplicationContext());
        PhotoCacheHelper.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PhotoCacheHelper.getInstance().clearDiskMemory();
    }

    public static Context getContext(){
        return contextSoftReference.get();
    }
}
