package com.yb.magicplayer.db.greendao.base;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.yb.magicplayer.entity.PlayingQuene;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PLAYING_QUENE".
*/
public class PlayingQueneDao extends AbstractDao<PlayingQuene, Long> {

    public static final String TABLENAME = "PLAYING_QUENE";

    /**
     * Properties of entity PlayingQuene.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
    }

    private DaoSession daoSession;


    public PlayingQueneDao(DaoConfig config) {
        super(config);
    }
    
    public PlayingQueneDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PLAYING_QUENE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL );"); // 0: id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PLAYING_QUENE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PlayingQuene entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PlayingQuene entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
    }

    @Override
    protected final void attachEntity(PlayingQuene entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public PlayingQuene readEntity(Cursor cursor, int offset) {
        PlayingQuene entity = new PlayingQuene( //
            cursor.getLong(offset + 0) // id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PlayingQuene entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(PlayingQuene entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(PlayingQuene entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PlayingQuene entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
