package com.yb.magicplayer.application;

import android.app.Application;
import android.content.Context;

import com.yb.magicplayer.db.greendao.GreenDaoHelper;

/**
 * Created by yb on 2017/3/9.
 */
public class MagicPlayerApplication extends Application {
    public static Context applicationContext;
    public static final String DB_NAME = "music";//数据库名字
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        initDatabase();
    }

    private void initDatabase() {
        GreenDaoHelper.initHelper(getApplicationContext(),DB_NAME);
    }
}
