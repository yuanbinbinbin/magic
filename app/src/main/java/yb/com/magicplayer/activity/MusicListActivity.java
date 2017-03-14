package yb.com.magicplayer.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import yb.com.magicplayer.R;
import yb.com.magicplayer.adapter.LocalMusicInfoAdpater;
import yb.com.magicplayer.entity.LocalMusic;
import yb.com.magicplayer.service.MusicPlayService;
import yb.com.magicplayer.utils.ConfigData;
import yb.com.magicplayer.utils.GlobalVariables;
import yb.com.magicplayer.utils.MediaUtils;
import yb.com.magicplayer.weights.ResultListView;

public class MusicListActivity extends BaseActivity implements ResultListView.OnRefreshListener, AdapterView.OnItemClickListener {
    private ResultListView mLv;
    private TextView mTvContent;
    private int state = -1;
    private MsgReceiver msgReceiver;
    private LocalMusicInfoAdpater localMusicInfoAdpater;
    private Intent mServiceIntent;
    private MusicPlayService.MyBinder mServiceBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceBinder = (MusicPlayService.MyBinder) service;
            if (localMusicInfoAdpater != null) {
                localMusicInfoAdpater.playingMusicChanged(mServiceBinder.getPlayingLocalMusic());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_music_list;
    }

    @Override
    protected void initView() {
        initCommonView();
        mLv = (ResultListView) findViewById(R.id.id_music_list_listview);
        mTvContent = (TextView) findViewById(R.id.id_music_list_content);

        mIvTopBarBack.setVisibility(View.VISIBLE);
        mTvTopBarTitle.setText("本地音乐");
    }

    @Override
    protected void initData() {
        state = 0;
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
        mLv.setonRefreshListener(this);
        mLv.setOnItemClickListener(this);
    }

    @Override
    public void onRefresh() {
        state = 1;
        new Thread(runnable).start();
    }

    @Override
    public void onLoadMore() {
        mLv.setFooterView(ResultListView.FOOTER_NOT_SHOW);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0 || localMusicInfoAdpater.getCount() < position) {
            return;
        }
        mServiceIntent.putExtra("playing", "playingbyid");
        mServiceIntent.putExtra("id", ((LocalMusic) localMusicInfoAdpater.getItem(position - 1)).getId());
        startService(mServiceIntent);
        GlobalVariables.playingState = ConfigData.PLAYING_MUSIC_ON_LOCAL;
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
            if (state == 1) {
                localMusicInfoAdpater.setData(GlobalVariables.listLocalMusic);
                mLv.onRefreshComplete();
                Toast.makeText(MusicListActivity.this, "刷新成功",
                        Toast.LENGTH_SHORT).show();
                state = -1;
            } else if (state == 0) {
                localMusicInfoAdpater = new LocalMusicInfoAdpater(
                        GlobalVariables.listLocalMusic, MusicListActivity.this);
                /**
                 * 绑定服务
                 */
                bindService(mServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                mLv.setAdapter(localMusicInfoAdpater, MusicListActivity.this, MusicListActivity.class);
                state = -1;
            }
        }
    };

    class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("RefreshInfor".equals(intent.getAction())) {
                localMusicInfoAdpater.playingMusicChanged(mServiceBinder.getPlayingLocalMusic());
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
