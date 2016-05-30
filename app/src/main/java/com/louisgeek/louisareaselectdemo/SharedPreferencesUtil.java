package com.louisgeek.louisareaselectdemo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by louisgeek on 2016/5/24.
 */
public class SharedPreferencesUtil {
    private final static String SHARED_PREFERENCES_FILENAME="SP_Default";
    public  final static String NOW_KEY="SSQ_json";

    /**将数据保存至SharedPreferences*/
    public static void  saveValue(Context context,String nowValue){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(NOW_KEY, nowValue);
        editor.commit();
    }
    /**从SharedPreferences获取数据*/
    public static String  getValue(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        String nowValue=sharedPreferences.getString(NOW_KEY, "");
        return nowValue;
    }

    public static boolean  containsKey(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREFERENCES_FILENAME, Context.MODE_PRIVATE);
        boolean isHasKey=sharedPreferences.contains(NOW_KEY);
        return isHasKey;
    }
}
