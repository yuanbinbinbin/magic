package com.yb.magicplayer.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.base.baselibrary.adapter.ItemViewTypes;
import com.base.baselibrary.adapter.listview.ListViewBaseAdapter;
import com.base.baselibrary.adapter.recyclerview.RecyclerViewDifferenceItemBaseAdapter;
import com.yb.magicplayer.R;
import com.yb.magicplayer.activity.base.BaseActivity;
import com.yb.magicplayer.adapter.LocalMusicInfoRecyclerViewItem;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.service.MusicPlayService;
import com.yb.magicplayer.utils.GlobalVariables;
import com.yb.magicplayer.utils.MediaUtils;

public class MusicListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private RecyclerView mRvMusicList;
    private TextView mTvContent;
    private int state = -1;
    private MsgReceiver msgReceiver;
    private ListViewBaseAdapter<Music> baseAdapter;

    private Intent mServiceIntent;
    private MusicPlayService.MusicPlayBinder mServiceBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceBinder = (MusicPlayService.MusicPlayBinder) service;
//            if (adapter != null) {
//                adapter.playingMusicChanged(mServiceBinder.getPlayingMusic());
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private RecyclerViewDifferenceItemBaseAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_list;
    }

    @Override
    protected void initView() {
        initCommonView();
        mRvMusicList = (RecyclerView) findViewById(R.id.id_music_list_recycler_view);
        mTvContent = (TextView) findViewById(R.id.id_music_list_content);
        mIvTopBarBack.setVisibility(View.VISIBLE);
        mTvTopBarTitle.setText("本地音乐");
    }

    @Override
    protected void initData() {
        state = 0;
        mRvMusicList.setAdapter(getAdapter());
        mRvMusicList.setLayoutManager(new LinearLayoutManager(this));
        new Thread(runnable).start();
        mServiceIntent = new Intent(this, MusicPlayService.class);
        msgReceiver = new MsgReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("RefreshInfor");
        filter.addAction("finish");
        registerReceiver(msgReceiver, filter);
    }

    @Override
    protected void initListener() {
        mIvTopBarBack.setOnClickListener(this);
    }

//    @Override
//    public void onRefresh() {
    //刷新
//        state = 1;
//        new Thread(runnable).start();
//    }
//
//    @Override
//    public void onLoadMore() {
//        //加载更多
////        mLv.setFooterView(ResultListView.FOOTER_NOT_SHOW);
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0 || adapter.getItemCount() < position) {
            return;
        }
        Music music = (Music) adapter.getItem(position - 1);
        if (music == null) {
            return;
        }
        MediaUtils.addMusic2PlayQuene(music);
        mServiceIntent.putExtra("playing", "playingbyid");
        mServiceIntent.putExtra("id", music.getId());
        startService(mServiceIntent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        unregisterReceiver(msgReceiver);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            GlobalVariables.listLocalMusic = MediaUtils.getLocalMusics(MusicListActivity.this);
            refreshHandler.sendEmptyMessage(0);
        }
    };

    private Handler refreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.setData(GlobalVariables.listLocalMusic);
            if (state == 1) {
//                mLv.onRefreshComplete();
                Toast.makeText(MusicListActivity.this, "刷新成功",
                        Toast.LENGTH_SHORT).show();
            } else if (state == 0) {
                /**
                 * 绑定服务
                 */
                bindService(mServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
            }
            state = -1;
        }
    };

    public RecyclerViewDifferenceItemBaseAdapter getAdapter() {
        adapter = new RecyclerViewDifferenceItemBaseAdapter(getActivity());
        adapter.addItemView(ItemViewTypes.ITEM_VIEW_TYPE_MUSIC, new LocalMusicInfoRecyclerViewItem());
        return adapter;
    }

    class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("RefreshInfor".equals(intent.getAction())) {
            } else if ("finish".equals(intent.getAction())) {
                MusicListActivity.this.finish();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_common_title_bar_back:
                finish();
                break;
        }
    }
}
