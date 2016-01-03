package com.tll.araproje.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {
    private static final String SHARED_PREFERENCES = "Ontology_app";
    public static void saveString(Context context,String key,String value){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putString(key,value).apply();
    }
    public static void saveInteger(Context context,String key,int value){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putInt(key, value).apply();
    }
    public static String readString(Context context,String key){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(key,"");
    }
    public static int readInteger(Context context,String key){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getInt(key, -1);
    }
    public static void saveBoolean(Context context,String key,boolean value){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(key,value).apply();
    }
    public static boolean readBoolean(Context context,String key){
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }
}
