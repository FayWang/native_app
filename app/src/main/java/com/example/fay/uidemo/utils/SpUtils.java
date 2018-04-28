package com.example.fay.uidemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.fay.uidemo.constant.SP;

/**
 * Created by xiaofei9 on 2018/4/28.
 * SharedPreference操作的工具类
 */
public class SpUtils {

    /**
     * save string value to sharedPreference with string value
     * @param context
     * @param key
     * @param value
     */
    public static void saveToSP(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SP.FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * get string value by string key from sharedPreference
     * @param context
     * @param key
     * @return
     */
//    public static String getSpValue(Context context, String key) {
//        SharedPreferences sp = context.getSharedPreferences(SP.FILE_NAME, Context.MODE_PRIVATE);
//        return sp.getString(key, "");
//    }

}
