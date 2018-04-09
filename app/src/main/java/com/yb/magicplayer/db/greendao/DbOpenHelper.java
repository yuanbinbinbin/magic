package com.yb.magicplayer.db.greendao;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yb.magicplayer.db.greendao.base.DaoMaster;
import com.yb.magicplayer.db.greendao.base.MusicDao;
import com.yb.magicplayer.utils.LogUtil;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.internal.DaoConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主要用于数据库升级
 * Created by yb on 2017/5/2.
 */
public class DbOpenHelper extends DaoMaster.DevOpenHelper {

    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i("DbOpenHelper", "onUpgrade-----");
        try {
            Class<? extends AbstractDao<?, ?>> daoClasses = null;//用于数据更新的表
            Database database = new StandardDatabase(db);
            generateTempTables(database, daoClasses); //备份数据（表结构改变的表）
            dropAllTables(database, true);  //删除旧表（结构修改/新增）
            createAllTables(database, true); //创建 新表（结构修改/新增）
            restoreData(database, daoClasses);//恢复数据（结构修改）
        } catch (Exception e) {
        }
    }
    //删除所有需要更新的表
    private void dropAllTables(Database db, boolean b) {
//        if (daoClasses != null) {
//            reflectMethod(db, "dropTable", b, daoClasses);
//        }
//        String sql = "DROP TABLE " + (b ? "IF EXISTS " : "") + "\"MUSIC\"";
//        db.execSQL(sql);
    }
    //新建所有需要更新的表
    private void createAllTables(Database db, boolean b) {
//        if (daoClasses != null) {
//            reflectMethod(db, "createTable", b, daoClasses);
//        }
//        String constraint = b? "IF NOT EXISTS ": "";
//        db.execSQL("CREATE TABLE " + constraint + "\"MUSIC\" (" + //
//                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
//                "\"NAME\" TEXT," + // 1: name
//                "\"AUTHOR\" TEXT," + // 2: author
//                "\"ALL_TIME\" INTEGER NOT NULL ," + // 3: allTime
//                "\"SIZE\" INTEGER NOT NULL ," + // 4: size
//                "\"ADDR\" TEXT," + // 5: addr
//                "\"ALBUM_ID\" INTEGER NOT NULL ," + // 6: album_id
//                "\"ALBUM\" TEXT," + // 7: album
//                "\"IMAGE\" TEXT," + // 8: image
//                "\"IS_LIKE\" INTEGER NOT NULL ," + // 9: isLike
//                "\"LRC\" TEXT," + // 10: lrc
//                "\"TYPE\" INTEGER NOT NULL ," + // 11: type
//                "\"CESHI\" INTEGER  );"); // 12: ceshi
    }

    /**
     * 备份数据
     *
     * @param db
     * @param daoClasses（表结构改变的表）
     */
    //备份数据
    private void generateTempTables(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses == null) {
            return;
        }
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String divider = "";
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList<>();
            StringBuilder createTableStringBuilder = new StringBuilder();
            createTableStringBuilder.append("CREATE TABLE IF NOT EXISTS ").append(tempTableName).append(" (");

            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tableName).contains(columnName)) {
                    properties.add(columnName);
                    String type = null;
                    try {
                        type = getTypeByClass(daoConfig.properties[j].type);
                    } catch (Exception exception) {
//                        Crashlytics.logException(exception);
                    }
                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);
                    if (daoConfig.properties[j].primaryKey) {
                        createTableStringBuilder.append(" PRIMARY KEY");
                    }
                    divider = ",";
                }
            }
            createTableStringBuilder.append(");");
            db.execSQL(createTableStringBuilder.toString());
            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tableName).append(";");
            db.execSQL(insertTableStringBuilder.toString());
        }
    }

    /**
     * 恢复数据
     *
     * @param db
     * @param daoClasses（结构修改的表）
     */
    //恢复数据
    private void restoreData(Database db, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoClasses == null) {
            return;
        }
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig = new DaoConfig(db, daoClasses[i]);
            String tableName = daoConfig.tablename;
            String tempTableName = daoConfig.tablename.concat("_TEMP");
            ArrayList<String> properties = new ArrayList();

            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;

                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);
                }
            }

            StringBuilder insertTableStringBuilder = new StringBuilder();

            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tempTableName);

            StringBuilder dropTableStringBuilder = new StringBuilder();

            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName).append(" ;");

            db.execSQL(insertTableStringBuilder.toString());
            db.execSQL(dropTableStringBuilder.toString());
        }
    }

    private String getTypeByClass(Class<?> type) throws Exception {
        if (type.equals(String.class)) {
            return "TEXT";
        }
        if (type.equals(Long.class) || type.equals(Integer.class) || type.equals(long.class)) {
            return "INTEGER";
        }
        if (type.equals(Boolean.class)) {
            return "BOOLEAN";
        }

        Exception exception = new Exception("TYPE " + type.toString() + " is not found");
//        Crashlytics.logException(exception);
        throw exception;
    }

    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<String>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return columns;
    }
}
