package com.base.baselibrary.adapter.listview;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.base.baselibrary.entity.BaseItem;

import java.util.List;

/**
 * adapter基类
 * Created by yb on 2017/2/17.
 */
public class ListViewBaseAdapter<T extends BaseItem> extends BaseAdapter {
    protected SparseArray<ListViewItemBase<T>> mItemViews = new SparseArray<ListViewItemBase<T>>();

    protected List<T> mList;//存放数据的list集合
    protected Activity mContext;

    public ListViewBaseAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    public void addItemView(int type, ListViewItemBase<T> itemView) {
        if (itemView == null) {
            return;
        }
        itemView.setActivity(mContext);
        mItemViews.put(type, itemView);
    }

    /**
     * 根据layout id获取view
     *
     * @param id
     * @return
     */
    protected View getView(int id) {
        if (mContext == null) {
            return null;
        }
        return LayoutInflater.from(mContext).inflate(id, null);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int position) {
        if (getCount() <= position) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewItemBase<T> itemView = null;
        try {
            itemView = mItemViews.get(getItemViewType(position));
        } catch (Exception e) {
            return null;
        }
        if (itemView == null) {
            return null;
        }
        if (convertView == null) {
            convertView = itemView.createView();
        }
        if (convertView == null) {
            return null;
        }
        itemView.updateView(getItem(position), position, convertView);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        T item = getItem(position);
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        return item.getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return mItemViews == null ? 0 : mItemViews.size();
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<T> list) {
        mList = list;
        notifyDataSetChanged();//刷新界面
    }

    /**
     * 追加数据
     *
     * @param list
     */
    public void addData(List<T> list) {
        if (mList == null) {
            mList = list;
        } else {
            if (list != null) {
                for (T item : list) {
                    mList.add(item);
                }
            }
        }
        notifyDataSetChanged();//刷新界面
    }

}
