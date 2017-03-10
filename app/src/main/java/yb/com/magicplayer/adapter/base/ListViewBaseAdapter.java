package yb.com.magicplayer.adapter.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * adapter基类
 * Created by yb on 2017/2/17.
 */
public abstract class ListViewBaseAdapter<T> extends BaseAdapter{
    protected List<T> mList;//存放数据的list集合
    protected Context mContext;
    /**
     * 设置数据
     * @param list
     */
    public void setData(List<T> list){
        mList = list;
        notifyDataSetChanged();//刷新界面
    }

    /**
     * 追加数据
     * @param list
     */
    public void addData(List<T> list){
        if(mList == null){
            mList = list;
        }else{
            if(list != null){
                for(T item : list){
                    mList.add(item);
                }
            }
        }
        notifyDataSetChanged();//刷新界面
    }

    /**
     * 获取全部数据
     * @return
     */
    public void insertDatasAtPosition(List<T> list,int position){
        if(list == null|| position > getCount()){
            return;
        }
        if(mList == null){
            mList = list;
        }else{
            mList.addAll(position,list);
        }
        notifyDataSetChanged();
    }
    /**
     * 获取某条数据内容，供外界调用
     * @param position
     * @return
     */
    public T getData(int position){
        if(getCount() <= position){
            return null;
        }
        return getItem(position);
    }

    /**
     * 根据layout id获取view
     * @param id
     * @return
     */
    protected View getView(int id){
        if(mContext == null){
            return null;
        }
        return LayoutInflater.from(mContext).inflate(id,null);
    }
    @Override
    public int getCount() {
        return mList == null?0:mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = createView();
        }
        if(convertView == null){
            return null;
        }
        updateView(getItem(position), position, convertView);
        return convertView;
    }

    /**
     * 创建新的view
     * @return
     */
    protected abstract View createView();

    /**
     * 更新view
     * @param item
     * @param position
     * @param convertView
     */
    protected abstract void updateView(T item,int position,View convertView);
}
