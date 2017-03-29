package com.yb.magicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yb.magicplayer.R;
import com.yb.magicplayer.adapter.base.RecyclerViewBaseAdapter;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.listener.OnItemClickListener;
import com.yb.magicplayer.utils.ImageUtil;
import com.yb.magicplayer.utils.LogUtil;
import com.yb.magicplayer.utils.SafeConvertUtil;

import java.util.List;

/**
 * MainActivity 最近播放Adapter
 * Created by yb on 2017/3/21.
 */
public class MainRecentPlayAdapter extends RecyclerViewBaseAdapter<Music, MainRecentPlayAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MainRecentPlayAdapter(Context context, List<Music> list) {
        super(list);
        mContext = context;
    }

    @Override
    public MainRecentPlayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_recent, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Music music = mList.get(position);
        if (music != null) {
            holder.mTvName.setText(SafeConvertUtil.convertToString(music.getName(), ""));
            holder.mTvAuthor.setText(SafeConvertUtil.convertToString(music.getAuthor(), ""));
            ImageUtil.loadImage(mContext, music.getImage(), holder.mIvAvatar, R.drawable.music_default_img, R.drawable.music_default_img);
            holder.mViewContainer.setTag(position);
            holder.mViewContainer.setOnClickListener(this);
        } else {
            holder.mViewContainer.setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        int position = SafeConvertUtil.convertToInt(v.getTag(), -1);
        if (position >= 0 && getItemCount() > position && onItemClickListener != null) {
            onItemClickListener.onItemClick(position, mList.get(position));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvAvatar;
        TextView mTvName;
        TextView mTvAuthor;
        View mViewContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvAvatar = (ImageView) itemView.findViewById(R.id.id_item_main_recent_avatar);
            mTvName = (TextView) itemView.findViewById(R.id.id_item_main_recent_name);
            mTvAuthor = (TextView) itemView.findViewById(R.id.id_item_main_recent_author);
            mViewContainer = itemView.findViewById(R.id.id_item_main_recent_container);
        }
    }
}
