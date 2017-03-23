package com.yb.magicplayer.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by yb on 2017/3/9.
 */
public class MagicPlayerApplication extends Application {
    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}
