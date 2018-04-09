package com.yb.magicplayer.db.base;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * 数据库操作规范接口
 * Created by yb on 2017/5/2.
 */
public interface IDatabase<M, K> {
    /**
     * 插入一条数据
     */
    boolean insert(M m);

    /***
     * 删除一条数据
     * @param m
     * @return
     */
    boolean delete(M m);

    /**
     * 删除一条数据通过key
     * @param key
     * @return
     */
    boolean deleteByKey(K key);

    /**
     * 删除一批数据
     * @param mList
     * @return
     */
    boolean deleteList(List<M> mList);

    boolean deleteByKeyInTx(K... key);

    /**
     * 删除所有数据
     * @return
     */
    boolean deleteAll();

    /**
     * 如果数据库中有则替换数据否则插入数据
     * @param m
     * @return
     */
    boolean insertOrReplace(@NonNull M m);

    /**
     * 数据库字段更新，数据库中的字段完全更新为entity中的数据，包括null
     * @param m
     * @return
     */
    boolean update(M m);

    boolean updateInTx(M... m);

    /**
     * 更新数据
     * @param mList
     * @return
     */
    boolean updateList(List<M> mList);

    /**
     *
     * @param key
     * @return
     */
    M selectByPrimaryKey(K key);

    /**
     * 加载所有数据
     * @return
     */
    List<M> loadAll();

    /**
     * 从数据库中加载一份新的数据到Bean缓存中，即对该Bean缓存缓存进行刷新。
     * @param m
     * @return
     */
    boolean refresh(M m);

    /**
     * 添加集合
     *
     * @param mList
     */
    boolean insertList(List<M> mList);

    /**
     * 添加集合
     *
     * @param mList
     */
    boolean insertOrReplaceList(List<M> mList);

    /**
     * 自定义查询
     *
     * @return
     */
    Object getQueryBuilder();

    /**
     * @param where
     * @param selectionArg
     * @return
     */
    List<M> queryRaw(String where, String... selectionArg);
}
