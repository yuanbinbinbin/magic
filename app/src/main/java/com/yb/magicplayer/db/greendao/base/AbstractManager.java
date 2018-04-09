package com.yb.magicplayer.db.greendao.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yb.magicplayer.db.base.IDatabase;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Collection;
import java.util.List;

/**
 * Created by yb on 2017/5/2.
 */
public abstract class AbstractManager<M, K> implements IDatabase<M, K> {

    @Override
    public boolean insert(@NonNull M m) {
        try {
            if (m == null)
                return false;
            getAbstractDao().insert(m);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean insertOrReplace(@NonNull M m) {
        try {
            if (m == null)
                return false;
            getAbstractDao().insertOrReplace(m);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(@NonNull M m) {
        try {
            if (m == null)
                return false;
            getAbstractDao().delete(m);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteByKey(K key) {
        try {
            if (TextUtils.isEmpty(key.toString()))
                return false;
            getAbstractDao().deleteByKey(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteByKeyInTx(K... key) {
        try {
            getAbstractDao().deleteByKeyInTx(key);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteList(List<M> mList) {
        try {
            if (mList == null || mList.size() == 0)
                return false;
            getAbstractDao().deleteInTx(mList);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteAll() {
        try {
            getAbstractDao().deleteAll();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean update(@NonNull M m) {
        try {
            if (m == null)
                return false;
            getAbstractDao().update(m);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateInTx(M... m) {
        try {
            if (m == null)
                return false;
            getAbstractDao().updateInTx(m);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean updateList(List<M> mList) {
        try {
            if (mList == null || mList.size() == 0)
                return false;
            getAbstractDao().updateInTx(mList);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public M selectByPrimaryKey(@NonNull K key) {
        try {
            return getAbstractDao().load(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<M> loadAll() {
        return getAbstractDao().loadAll();
    }

    @Override
    public boolean refresh(@NonNull M m) {
        try {
            if (m == null)
                return false;
            getAbstractDao().refresh(m);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean insertList(@NonNull List<M> list) {
        try {
            if (list == null || list.size() == 0)
                return false;
            getAbstractDao().insertInTx(list);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * @param list
     * @return
     */
    @Override
    public boolean insertOrReplaceList(@NonNull List<M> list) {
        try {
            if (list == null || list.size() == 0)
                return false;
            getAbstractDao().insertOrReplaceInTx(list);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public QueryBuilder<M> getQueryBuilder() {
        return getAbstractDao().queryBuilder();
    }

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    @Override
    public List<M> queryRaw(String where, String... selectionArg) {
        return getAbstractDao().queryRaw(where, selectionArg);
    }

    public Query<M> queryRawCreate(String where, Object... selectionArg) {
        return getAbstractDao().queryRawCreate(where, selectionArg);
    }

    public Query<M> queryRawCreateListArgs(String where, Collection<Object> selectionArg) {
        return getAbstractDao().queryRawCreateListArgs(where, selectionArg);
    }

    /**
     * 获取Dao
     *
     * @return
     */
    protected abstract AbstractDao<M, K> getAbstractDao();
}
