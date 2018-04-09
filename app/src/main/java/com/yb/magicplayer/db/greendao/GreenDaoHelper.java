package com.yb.magicplayer.db.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yb.magicplayer.db.greendao.base.DaoMaster;
import com.yb.magicplayer.db.greendao.base.DaoSession;

/**
 * GreenDao 帮助类
 * 用于数据库初始化
 * Created by yb on 2017/4/25.
 */
public class GreenDaoHelper {
    //DAO Data Access Object 数据访问对象，封装的是对数据库的访问操作。
    //DaoMaster 使用GreenDao的入口，DaoMaster持有着SQLite数据库对象 和 管理DAO类(对数据库进行访问的操作类)，创建数据库、表等
    private static DbOpenHelper mHelper;
    private static SQLiteDatabase db;
    private static DaoMaster mDaoMaster;
    //DaoSession 管理着所有可以使用的DAO对象，可通过Get方法获取，
    private static DaoSession mDaoSession;

    public static void initHelper(Context context, String dbName) {
        mHelper = new DbOpenHelper(context, dbName, null);
        db = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
}
