package com.yb.magicplayer.adapter.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.baselibrary.adapter.recyclerview.RecyclerViewSingleItemBaseAdapter;
import com.yb.magicplayer.R;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.entity.PlayList;
import com.yb.magicplayer.utils.ImageUtil;
import com.yb.magicplayer.utils.SafeConvertUtil;

import java.util.List;

/**
 * MainActivity 播放列表Adapter
 * Created by yb on 2017/3/22.
 */
public class MainPlayListAdapter extends RecyclerViewSingleItemBaseAdapter<PlayList, MainPlayListAdapter.ViewHolder> {
    private Context mContext;

    public MainPlayListAdapter(Context context, List<PlayList> list) {
        super(list);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_play_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlayList playList = mList.get(position);
        if (playList != null) {
            List<Music> list = playList.getPlayList();
            if (list != null && list.size() > 0) {
                int i = 0;
                for (Music music : list) {
                    if (i >= 4) {
                        break;
                    }
                    if (music != null) {
                        ImageUtil.loadImage(mContext, music.getImage(), holder.mIvs[i], R.drawable.music_default_img, R.drawable.music_default_img);
                        i++;
                    }
                }
            }
            holder.mTvTitle.setText(SafeConvertUtil.convertToString(playList.getName(), ""));
            holder.mTvCount.setText(playList.getPlayList().size() + " Song");
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView[] mIvs;
        TextView mTvTitle;
        TextView mTvCount;

        public ViewHolder(View itemView) {
            super(itemView);
            mIvs = new ImageView[4];
            mIvs[0] = (ImageView) itemView.findViewById(R.id.id_item_main_play_list_img1);
            mIvs[1] = (ImageView) itemView.findViewById(R.id.id_item_main_play_list_img2);
            mIvs[2] = (ImageView) itemView.findViewById(R.id.id_item_main_play_list_img3);
            mIvs[3] = (ImageView) itemView.findViewById(R.id.id_item_main_play_list_img4);
            mTvTitle = (TextView) itemView.findViewById(R.id.id_item_main_play_list_name);
            mTvCount = (TextView) itemView.findViewById(R.id.id_item_main_play_list_count);
        }
    }
}
