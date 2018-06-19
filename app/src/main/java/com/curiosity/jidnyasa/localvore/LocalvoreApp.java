package com.curiosity.jidnyasa.localvore;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

public class LocalvoreApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }

}