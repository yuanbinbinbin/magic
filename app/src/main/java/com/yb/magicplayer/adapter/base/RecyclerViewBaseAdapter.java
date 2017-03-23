package com.yb.magicplayer.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter Base
 * Created by yb on 2017/3/21.
 */
public abstract class RecyclerViewBaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> mList;

    public RecyclerViewBaseAdapter(List<T> list) {
        super();
        mList = list;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * 末尾添加数据
     *
     * @param list
     */
    public void addData(List<T> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        if (getItemCount() <= 0) {
            mList = list;
        } else {
            for (T t : list) {
                mList.add(t);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 末尾添加一条数据
     *
     * @param item
     */
    public void addData(T item) {
        if (item == null) {
            return;
        }
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.add(item);
        notifyDataSetChanged();
    }

    /**
     * 在指定位置添加一条数据
     *
     * @param item
     * @param position
     */
    public void insertDataAtPosition(T item, int position) {
        if (position < 0 || item == null || position > getItemCount()) {
            return;
        }
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.add(position, item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
