package com.base.baselibrary.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.baselibrary.BaseGlobalVariable;
import com.base.baselibrary.interfaces.listeners.OnItemClickListener;

/**
 * Created by yb on 2017/8/16.
 */
public abstract class ListViewItemBase<T> {
    protected Activity mActivity;
    private OnItemClickListener onItemClickListener;

    public ListViewItemBase() {
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected void onItemClick(View v, Object... objects) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(v, objects);
        }
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public Context getContext() {
        if (getActivity() != null) {
            return getActivity();
        }
        return BaseGlobalVariable.getApplication();
    }

    protected View getView(int layoutid) {
        Context context = getContext();
        if (context != null) {
            return LayoutInflater.from(context).inflate(layoutid, null);
        } else {
            return null;
        }
    }

    public abstract View createView();

    public abstract void updateView(T t, int position, View convertView);
}
