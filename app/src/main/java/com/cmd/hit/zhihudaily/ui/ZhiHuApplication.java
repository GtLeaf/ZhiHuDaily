package com.cmd.hit.zhihudaily.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.cmd.hit.zhihudaily.other.PhotoCacheHelper;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
        disableAPIDialog();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PhotoCacheHelper.getInstance().clearDiskMemory();
    }

    public static Context getContext(){
        return contextSoftReference.get();
    }

    /*
    * 反射 禁止android9.0以上弹窗
    * */
    private void disableAPIDialog(){
        if (Build.VERSION.SDK_INT < 28) return;
        try {
            Class clazz = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object activityThread = currentActivityThread.invoke(null);
            Field mHiddenApiWarningShown = clazz.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread,true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
