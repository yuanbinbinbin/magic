package com.yb.magicplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import com.yb.magicplayer.R;
import com.yb.magicplayer.adapter.base.ListViewBaseAdapter;
import com.yb.magicplayer.entity.LocalMusic;

public class LocalMusicInfoAdpater extends ListViewBaseAdapter {

    private LocalMusic playingMusic;
    private int playingColor;

    public LocalMusicInfoAdpater(List<LocalMusic> musics, Context context) {
        super();
        this.mList = musics;
        mContext = context;
        playingColor = Color.rgb(239, 180, 62);
    }

    @Override
    protected View createView() {
        View convertView = getView(R.layout.item_local_music_info);
        if (convertView == null) {
            return null;
        }
        ViewHolder mViewHolder = new ViewHolder();
        mViewHolder.mTextViewName = (TextView) convertView
                .findViewById(R.id.id_local_music_info_name);
        mViewHolder.mTextViewAuthor = (TextView) convertView
                .findViewById(R.id.id_local_music_info_author);
        convertView.setTag(mViewHolder);
        return convertView;
    }

    @Override
    protected void updateView(Object item, int position, View convertView) {
        ViewHolder mViewHolder = (ViewHolder) convertView.getTag();
        LocalMusic tempMusic = (LocalMusic) getItem(position);
        if (playingMusic != null && playingMusic.getName().equals(tempMusic.getName())) {
            mViewHolder.mTextViewName.setTextColor(playingColor);
        } else {
            mViewHolder.mTextViewName.setTextColor(Color.GRAY);
        }
        mViewHolder.mTextViewName.setText(tempMusic.getName());
        mViewHolder.mTextViewAuthor.setText(tempMusic.getAuthor());
    }

    class ViewHolder {
        TextView mTextViewName;
        TextView mTextViewAuthor;
    }

    public void playingMusicChanged(LocalMusic localMusic){
        playingMusic = localMusic;
        notifyDataSetChanged();
    }
}
