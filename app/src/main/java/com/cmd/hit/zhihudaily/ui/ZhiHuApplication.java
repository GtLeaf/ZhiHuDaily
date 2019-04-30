package com.cmd.hit.zhihudaily.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by PC-0775 on 2019/4/29.
 */

public class ZhiHuApplication extends Application {
    //存在内存泄漏
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    //尝试使用软引用
    public static SoftReference<ZhiHuApplication> reference;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        reference = new SoftReference<ZhiHuApplication>(this);
    }

}
