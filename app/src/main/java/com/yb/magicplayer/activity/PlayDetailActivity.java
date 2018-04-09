package com.yb.magicplayer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yb.magicplayer.R;
import com.yb.magicplayer.activity.base.EventBusBaseActivity;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.events.PlayingMusicChangeEvent;
import com.yb.magicplayer.events.PlayingStatusChangeEvent;
import com.yb.magicplayer.events.RefreshPlayProgressEvent;
import com.yb.magicplayer.service.MusicPlayService;
import com.yb.magicplayer.utils.ConfigData;
import com.yb.magicplayer.utils.GlobalVariables;
import com.yb.magicplayer.utils.ImageUtil;
import com.yb.magicplayer.utils.LogUtil;
import com.yb.magicplayer.utils.SafeConvertUtil;
import com.yb.magicplayer.utils.TimeFormat;
import com.yb.magicplayer.utils.ToastUtil;
import com.yb.magicplayer.view.visualizer.VisualizerView;
import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.LineBarDrawer;
import com.yb.magicplayer.view.visualizer.drawer.CircleBarDrawer;
import com.yb.magicplayer.view.visualizer.drawer.CircleLineDrawer;
import com.yb.magicplayer.view.visualizer.drawer.LineDrawer;
import com.yb.magicplayer.view.visualizer.drawer.base.DrawerBase;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PlayDetailActivity extends EventBusBaseActivity {
    private static final String TAG = "PlayDetailActivity";

    private VisualizerView mVisualizerView;
    private ImageView mIvBg;
    private TextView mTvPlayingTime;//播放时间
    private TextView mTvTotalTime;//歌曲总时间
    private SeekBar mSeekBar;//播放条
    private ImageView mIvPlayModel;//播放模式
    private View mViewPlayLast;//播放上一首按钮
    private View mViewPlayNext;//播放下一首按钮
    private ImageView mIvPlayStatus;//播放或暂停按钮
    private View mViewPlayList;//播放列表
    private Intent serviceIntent;//用于更改播放状态的Intent
    private boolean isCanUpdatePlayProgress;//是否可以更新播放进度，当用于拉进度条时应取消更新进度
    private int drawerType;//动画类型
    private boolean isOnStop;//是否回调了onStop方法
    private static int DELAYED_TIME = 1000;
    private Handler updateMusicProgressHandler = new Handler();
    private Runnable updateMusicProgressRunable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "updateMusicProgressRunable");
            if (mMusicPlayBinder != null) {
                refreshSeekBar(mMusicPlayBinder.getProgress(), mMusicPlayBinder.getSecondProgress(), mMusicPlayBinder.getMaxProgress());
            }
            updateMusicProgressHandler.postDelayed(updateMusicProgressRunable, DELAYED_TIME);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_detail;
    }

    @Override
    protected void initView() {
        initCommonView();
        mVisualizerView = (VisualizerView) findViewById(R.id.id_play_detail_visualizer);
        mIvBg = (ImageView) findViewById(R.id.id_play_detail_bg);
        mTvPlayingTime = (TextView) findViewById(R.id.id_play_detail_playing_time);
        mTvTotalTime = (TextView) findViewById(R.id.id_play_detail_playing_totaltime);
        mSeekBar = (SeekBar) findViewById(R.id.id_play_detail_seek_bar);
        mIvPlayModel = (ImageView) findViewById(R.id.id_play_detail_mode);
        mViewPlayLast = findViewById(R.id.id_play_detail_last);
        mViewPlayNext = findViewById(R.id.id_play_detail_next);
        mIvPlayStatus = (ImageView) findViewById(R.id.id_play_detail_play);
        mViewPlayList = findViewById(R.id.id_play_detail_list);

        mIvTopBarBack.setVisibility(View.VISIBLE);
        mTvTopBarTitle.setVisibility(View.VISIBLE);
        mIvTopBarRight1.setVisibility(View.VISIBLE);
        mIvTopBarRight1.setImageResource(R.drawable.like_no);
        mIvTopBarRight2.setVisibility(View.GONE);
        mTvTopBarRightText.setVisibility(View.GONE);

        mTvTopBarTitle.setText("");
        refreshPlayModel();
    }

    private void refreshPlayModel() {
        switch (GlobalVariables.playingMode) {
            case ConfigData.PLAYING_MUSIC_MODE_SEQUENCE:
                mIvPlayModel.setImageResource(R.drawable.play_detail_sequence);
                break;
            case ConfigData.PLAYING_MUSIC_MODE_SINGLE:
                mIvPlayModel.setImageResource(R.drawable.play_detail_single);
                break;
            case ConfigData.PLAYING_MUSIC_MODE_RANDOM:
                mIvPlayModel.setImageResource(R.drawable.play_detail_random);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {
        bindMusicPlaySerivce();
        drawerType = DRAWER_CIRCLE_BAR;
        changeDrawer();
        isCanUpdatePlayProgress = false;
    }

    @Override
    protected void initListener() {
        mIvTopBarBack.setOnClickListener(this);
        mIvPlayModel.setOnClickListener(this);
        mViewPlayLast.setOnClickListener(this);
        mViewPlayList.setOnClickListener(this);
        mViewPlayNext.setOnClickListener(this);
        mIvPlayStatus.setOnClickListener(this);
        mVisualizerView.setOnClickListener(this);
        mIvTopBarRight1.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                refreshPlayingTime(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopUpdatePlayProgress();//关闭更新进度条
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (serviceIntent == null) {
                    serviceIntent = new Intent(getContext(), MusicPlayService.class);
                }
                serviceIntent.putExtra("playing", "changetime");
                serviceIntent.putExtra("time", seekBar.getProgress());
                startService(serviceIntent);
                startUpdatePlayProgress();//开启更新进度条
            }
        });
    }

    private void refreshPlayingTime(int millis) {
        mTvPlayingTime.setText(TimeFormat.millis2mmss(millis));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_play_detail_visualizer:
                drawerType++;
                changeDrawer();
                break;
            case R.id.id_common_title_bar_back:
                finish();
                break;
            case R.id.id_play_detail_mode:
                changeModel();//更改播放模式
                break;
            case R.id.id_play_detail_last:
                playLast();//播放上一首
                break;
            case R.id.id_play_detail_play:
                changePlayStatus();//更改播放状态
                break;
            case R.id.id_play_detail_next:
                playNext();//播发下一首
                break;
            case R.id.id_play_detail_list:
                showPlayList();//显示播放列表
                break;
            case R.id.id_common_title_bar_right1:
                changeLikeStatus();//修改喜欢音乐状态
                break;
        }
    }
    //修改喜欢音乐的状态
    private void changeLikeStatus() {
        
    }

    private void showPlayList() {

    }

    private void playNext() {
        if (serviceIntent == null) {
            serviceIntent = new Intent(getContext(), MusicPlayService.class);
        }
        serviceIntent.putExtra("playing", "next");
        startService(serviceIntent);
    }

    private void changePlayStatus() {
        int status;
        if (mMusicPlayBinder != null) {
            status = mMusicPlayBinder.getPlayStatus();
        } else {
            status = GlobalVariables.playStatus;
        }
        if (status == GlobalVariables.PLAY_STATUS_BUFFER) {
            ToastUtil.showShortTime(getContext(), "缓冲中...");
        } else if (status == GlobalVariables.PLAY_STATUS_PAUSE) {
            Intent intent = new Intent(getContext(), MusicPlayService.class);
            intent.putExtra("playing", "play");
            startService(intent);
        } else if (status == GlobalVariables.PLAY_STATUS_PLAYING) {
            Intent intent = new Intent(getContext(), MusicPlayService.class);
            intent.putExtra("playing", "pause");
            startService(intent);
        }
    }

    private void playLast() {
        if (serviceIntent == null) {
            serviceIntent = new Intent(getContext(), MusicPlayService.class);
        }
        serviceIntent.putExtra("playing", "last");
        startService(serviceIntent);
    }

    //更改播放模式
    private void changeModel() {
        GlobalVariables.changePlayingMode(getContext());
        refreshPlayModel();
    }


    @Override
    protected void onStart() {
        super.onStart();
        isOnStop = false;
        mVisualizerView.setIsCanDraw(true);//可以更新界面
        startUpdatePlayProgress();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOnStop = true;
        mVisualizerView.setIsCanDraw(false);//不可以更新界面
        stopUpdatePlayProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    //不可以更新进度条
    private void stopUpdatePlayProgress() {
        isCanUpdatePlayProgress = false;
        updateMusicProgressHandler.removeCallbacks(updateMusicProgressRunable);
    }

    //可以更新进度条
    private void startUpdatePlayProgress() {
        //Activity处于后台状态不可更新、不可多次启动、music未播放不可启动(后两个判断条件主要是因为onStop状态->onStart状态)
        if (!isOnStop && !isCanUpdatePlayProgress && mMusicPlayBinder != null && mMusicPlayBinder.getPlayStatus() == GlobalVariables.PLAY_STATUS_PLAYING) {
            isCanUpdatePlayProgress = true;
            updateMusicProgressHandler.postDelayed(updateMusicProgressRunable, DELAYED_TIME);
        }
    }

    //更新正在播放音乐的信息
    private void refreshMusicInfo() {
        Music music;
        int progress = 0;
        int secondProgress = 0;
        int playStatus;
        if (mMusicPlayBinder == null) {
            if (GlobalVariables.playQuene == null) {
                return;
            }
            music = GlobalVariables.playQuene.get(GlobalVariables.playingPosition);
            playStatus = GlobalVariables.playStatus;
        } else {
            music = mMusicPlayBinder.getPlayingMusic();
            progress = mMusicPlayBinder.getProgress();
            secondProgress = mMusicPlayBinder.getSecondProgress();
            playStatus = mMusicPlayBinder.getPlayStatus();
        }
        refreshMusicInfo(music);
        refreshSeekBar(progress, secondProgress, music.getAllTime());
        refreshPlayStatus(playStatus);
    }

    //更新正在播放音乐的信息
    private void refreshMusicInfo(Music music) {
        if (music != null) {
            ImageUtil.loadImage(getContext(), music.getImage(), mIvBg);
            mTvTopBarTitle.setText(SafeConvertUtil.convertToString(music.getName(), ""));
            mTvTotalTime.setText(TimeFormat.millis2mmss(music.getAllTime()));
            if (music.getIsLike()) {
                mIvTopBarRight1.setImageResource(R.drawable.like_yes);
            } else {
                mIvTopBarRight1.setImageResource(R.drawable.like_no);
            }
        }
    }

    //更新播放状态
    private void refreshPlayStatus(int status) {
        if (GlobalVariables.PLAY_STATUS_PLAYING == status) {
            mIvPlayStatus.setImageResource(R.drawable.ic_play_detail_pause);
            mVisualizerView.setIsRenew(false);
            startUpdatePlayProgress();
        } else if (GlobalVariables.PLAY_STATUS_PAUSE == status) {
            mIvPlayStatus.setImageResource(R.drawable.ic_play_detail_play);
            mVisualizerView.setIsRenew(true);
            stopUpdatePlayProgress();
        } else if (GlobalVariables.PLAY_STATUS_BUFFER == status) {
            mVisualizerView.setIsRenew(true);
        }
    }

    //更新播放进度条
    private void refreshSeekBar(int progress, int secondProgress, int maxProgress) {
        if (!isCanUpdatePlayProgress) {
            return;
        }
        LogUtil.i(TAG, "set progress: " + progress + " secondProgress " + secondProgress + " maxProgress: " + maxProgress);
        mSeekBar.setMax(maxProgress);
        mSeekBar.setProgress(progress);
        mSeekBar.setSecondaryProgress(secondProgress);
    }

    private static final int DRAWER_CIRCLE_BAR = 1;
    private static final int DRAWER_CIRCLE_LINE = 2;
    private static final int DRAWER_LINE_BAR = 3;
    private static final int DRAWER_LINE = 4;
    private DrawerBase circleBarDrwder;
    private DrawerBase circleLineDrwder;
    private DrawerBase lineBarDrwder;
    private DrawerBase lineDrwder;

    public void changeDrawer() {
        if (drawerType > DRAWER_LINE) {
            drawerType = DRAWER_CIRCLE_BAR;
        }
        if (drawerType == DRAWER_CIRCLE_BAR) {
            if (circleBarDrwder == null) {
                Paint paint = new Paint();
                paint.setStrokeWidth(8f);
                paint.setAntiAlias(true);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
                paint.setColor(Color.argb(255, 222, 92, 143));
                circleBarDrwder = new CircleBarDrawer(paint, 8, true);
            }
            mVisualizerView.setDrawer(circleBarDrwder);
        } else if (drawerType == DRAWER_CIRCLE_LINE) {
            if (circleLineDrwder == null) {
                Paint paint = new Paint();
                paint.setStrokeWidth(3f);
                paint.setAntiAlias(true);
                paint.setColor(Color.argb(255, 222, 92, 143));
                circleLineDrwder = new CircleLineDrawer(paint, true);
            }
            mVisualizerView.setDrawer(circleLineDrwder);
        } else if (drawerType == DRAWER_LINE_BAR) {
            if (lineBarDrwder == null) {
                Paint paint2 = new Paint();
                paint2.setStrokeWidth(12f);
                paint2.setAntiAlias(true);
                paint2.setColor(Color.argb(200, 181, 111, 233));
                lineBarDrwder = new LineBarDrawer(8, paint2, LineBarDrawer.LOCATION_BOTTOM, true);
            }
            mVisualizerView.setDrawer(lineBarDrwder);
        } else if (drawerType == DRAWER_LINE) {
            if (lineDrwder == null) {
                Paint linePaint = new Paint();
                linePaint.setStrokeWidth(3f);
                linePaint.setAntiAlias(true);
                linePaint.setColor(Color.argb(88, 0, 128, 255));
                lineDrwder = new LineDrawer(linePaint, true);
            }
            mVisualizerView.setDrawer(lineDrwder);
        }
    }

    @Override
    protected void bindServiceSuccess() {
        super.bindServiceSuccess();
        refreshMusicInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshVisualizer(FFTBean event) {
        if (!isOnStop) {//Activity处于后台不更新
            mVisualizerView.updateVisualizerFFT(event.getBytes());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshVisualizer(WaveBean event) {
        if (!isOnStop) {//Activity处于后台不更新
            mVisualizerView.updateVisualizer(event.getBytes());
        }
    }

    /**
     * 播放音乐改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayingMusicChanged(PlayingMusicChangeEvent event) {
        LogUtil.i(TAG, "onPlayingMusicChanged");
        refreshMusicInfo(event.getPlayingMusic());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayingStatusChanged(PlayingStatusChangeEvent event) {
        LogUtil.i(TAG, "onPlayingStatusChanged");
        refreshPlayStatus(event.getStatus());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshPlayProgress(RefreshPlayProgressEvent event) {
        LogUtil.i(TAG, "onRefreshPlayProgress");
        refreshSeekBar(event.getProgress(), event.getSecondProgress(), event.getMaxProgress());
    }
}
