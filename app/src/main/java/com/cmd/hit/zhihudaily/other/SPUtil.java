package com.cmd.hit.zhihudaily.other;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.cmd.hit.zhihudaily.ui.ZhiHuApplication;

/**
 * Created by PC-0775 on 2019/4/29.
 */

public class SPUtil {
    private static final String FILE_NAME = "ZhiHuDaily_SP";

    /*
    * 保存数据
    * */
    public static void put(String key, Object value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ZhiHuApplication.reference.get());
        Editor editor = preferences.edit();
        if (value instanceof String){
            editor.putString(key, (String) value);

        }else if (value instanceof Integer){
            editor.putInt(key, (Integer) value);

        }else if (value instanceof Float){
            editor.putFloat(key, (Float) value);

        }else if (value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);

        }else if (value instanceof Long){
            editor.putLong(key, (Long) value);

        }else {
            editor.putString(key, value.toString());
        }
        editor.apply();
    }

    /*
    * 获取数据
    * */
    public static <T> T get(String key, T defaultValue){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ZhiHuApplication.reference.get());
        Object obj = null;
        if (defaultValue instanceof String){
            obj = preferences.getString(key, (String)defaultValue);
        }else if (defaultValue instanceof Integer){
            obj = preferences.getInt(key, (Integer)defaultValue);
        }else if (defaultValue instanceof Float){
            obj = preferences.getFloat(key, (Float)defaultValue);
        }else if (defaultValue instanceof Boolean){
            obj = preferences.getBoolean(key, (Boolean)defaultValue);
        }else if (defaultValue instanceof Long){
            obj = preferences.getLong(key, (Long)defaultValue);
        }
        return (T) obj;
    }

}
