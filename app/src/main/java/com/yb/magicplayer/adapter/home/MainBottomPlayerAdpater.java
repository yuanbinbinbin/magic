package com.yb.magicplayer.adapter.home;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yb.magicplayer.R;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.listener.OnItemClickListener;
import com.yb.magicplayer.utils.GlobalVariables;
import com.yb.magicplayer.utils.ImageUtil;
import com.yb.magicplayer.utils.SafeConvertUtil;
import com.yb.magicplayer.weights.ShapeImageView;

import java.util.LinkedList;
import java.util.List;

public class MainBottomPlayerAdpater extends PagerAdapter implements OnClickListener {

    private LayoutInflater mInflater;
    private Context mContext;
    private LinkedList<View> mViewCache = new LinkedList<View>();
    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public MainBottomPlayerAdpater(List<Music> musics, Context mContext) {
        super();
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v = (View) object;
        container.removeView(v);
        mViewCache.add(v);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder mViewHolder;
        View convertView;
        if (mViewCache.size() == 0) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_main_bottom_music_info, null);
            mViewHolder.mSIvPlayingMusicAvatar = (ShapeImageView) convertView
                    .findViewById(R.id.id_main_playing_avatar);
            mViewHolder.mTvPlayingMusicTitle = (TextView) convertView
                    .findViewById(R.id.id_main_playing_title);
            mViewHolder.mTvPlayingMusicLrc = (TextView) convertView
                    .findViewById(R.id.id_main_playing_lrc);
            mViewHolder.mViewPlayingMusicContainer = convertView.findViewById(R.id.id_main_playing_container);
            convertView.setTag(mViewHolder);
        } else {
            convertView = mViewCache.removeFirst();
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        List<Music> musics = GlobalVariables.playQuene;
        if (musics != null && musics.size() > position) {
            Music music = musics.get(position);
            if (music == null) {
                return convertView;
            }
            mViewHolder.mTvPlayingMusicTitle.setText(SafeConvertUtil.convertToString(music.getName(), ""));
            mViewHolder.mTvPlayingMusicLrc.setVisibility(View.GONE);
            if (musics.get(position).getImage() != null)
                ImageUtil.loadImage(mContext, music.getImage(), mViewHolder.mSIvPlayingMusicAvatar, R.drawable.ic_default, R.drawable.ic_default);
            mViewHolder.mViewPlayingMusicContainer.setTag(position);
            mViewHolder.mViewPlayingMusicContainer.setOnClickListener(this);
        } else {
            mViewHolder.mViewPlayingMusicContainer.setOnClickListener(null);
        }
        container.addView(convertView);
        return convertView;
    }

    @Override
    public int getCount() {
        return GlobalVariables.playQuene == null ? 0 : GlobalVariables.playQuene.size();
    }

    @Override
    public void onClick(View v) {
        int position = SafeConvertUtil.convertToInt(v.getTag(), -1);
        if (position < 0 || position >= getCount() || onItemClickListener == null) {
            return;
        }
        onItemClickListener.onItemClick(position, GlobalVariables.playQuene.get(position));
    }

    class ViewHolder {
        private View mViewPlayingMusicContainer;
        private ShapeImageView mSIvPlayingMusicAvatar;
        private TextView mTvPlayingMusicTitle;
        private TextView mTvPlayingMusicLrc;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
