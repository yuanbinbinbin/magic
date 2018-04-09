package com.yb.magicplayer.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.base.baselibrary.adapter.recyclerview.RecyclerViewItemBase;
import com.base.baselibrary.entity.BaseItem;
import com.yb.magicplayer.R;
import com.yb.magicplayer.entity.Music;

public class LocalMusicInfoRecyclerViewItem extends RecyclerViewItemBase<BaseItem> {
    private int playingColor;

    public LocalMusicInfoRecyclerViewItem() {
        super();
        playingColor = Color.rgb(239, 180, 62);
    }

    @Override
    public RecyclerView.ViewHolder createView() {
        View convertView = getView(R.layout.item_local_music_info);
        if (convertView == null) {
            return null;
        }
        return new ViewHolder(convertView);
    }

    @Override
    public void updateView(BaseItem item, int position, RecyclerView.ViewHolder holder) {
        if (item instanceof Music && holder instanceof ViewHolder) {
            ViewHolder mViewHolder = (ViewHolder) holder;
            Music music = (Music) item;
            if (music.getIsPlaying()) {
                mViewHolder.mTextViewName.setTextColor(playingColor);
            } else {
                mViewHolder.mTextViewName.setTextColor(Color.GRAY);
            }
            mViewHolder.mTextViewName.setText(music.getName());
            mViewHolder.mTextViewAuthor.setText(music.getAuthor());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewName;
        TextView mTextViewAuthor;

        public ViewHolder(View convertView) {
            super(convertView);
            mTextViewName = (TextView) convertView
                    .findViewById(R.id.id_local_music_info_name);
            mTextViewAuthor = (TextView) convertView
                    .findViewById(R.id.id_local_music_info_author);
        }
    }
}
