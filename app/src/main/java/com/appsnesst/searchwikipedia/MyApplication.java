package com.appsnesst.searchwikipedia;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }
}
