package com.yb.magicplayer.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.audiofx.Visualizer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.util.List;
import java.util.Random;

import com.yb.magicplayer.R;
import com.yb.magicplayer.activity.MainActivity;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.events.CloseEvent;
import com.yb.magicplayer.events.PlayingMusicChangeEvent;
import com.yb.magicplayer.events.PlayingStatusChangeEvent;
import com.yb.magicplayer.events.RefreshPlayProgressEvent;
import com.yb.magicplayer.events.RefreshRecentPlayListEvent;
import com.yb.magicplayer.utils.ConfigData;
import com.yb.magicplayer.utils.GlobalVariables;
import com.yb.magicplayer.utils.LogUtil;
import com.yb.magicplayer.utils.MediaUtils;

import org.greenrobot.eventbus.EventBus;

public class MusicPlayService extends Service implements OnCompletionListener, Visualizer.OnDataCaptureListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnInfoListener {

    private Music playingmusic;

    private static int SEND_NOTIFICATION_PLAY = 0;
    private static int SEND_NOTIFICATION_PAUSE = 1;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews mRemoteViews;

    private PendingIntent mPendingIntent;
    private int playingId;

    private Intent mIntent;
    private Intent refreshTimeIntent;
    private ImageView mIvMusicImg;
    private MediaPlayer mp;  //用于音乐波荡
    private int positon;   //音乐播放的位置
    private Visualizer mVisualizer;//用于获取音乐频率
    private boolean isPrepared = false;

    @Override
    public void onCreate() {
        //getMusicInfoFromSharedPreference();
        initData();
        initMediaPlayer();//初始化播放器
        initIntent();
        initNotification();
    }

    private void initData() {
        positon = GlobalVariables.playingPosition;
        playingmusic = GlobalVariables.playQuene.get(positon);
        playingId = playingmusic.getId();
    }

    private void initMediaPlayer() {
        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);
        mp.setOnPreparedListener(this);
        mp.setOnBufferingUpdateListener(this);
        mp.setOnInfoListener(this);
        mVisualizer = new Visualizer(mp.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, true, true);
        mVisualizer.setEnabled(false);
        isPrepared = false;
    }

    private void initIntent() {
        mIntent = new Intent(this, MusicPlayService.class);
        refreshTimeIntent = new Intent("RefreshTime");
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mRemoteViews = new RemoteViews(getPackageName(),
                R.layout.notification_playing_status);
        mPendingIntent = PendingIntent.getService(this, 4,
                mIntent.putExtra("playing", "close"),
                PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.id_notification_close,
                mPendingIntent);
        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.activity_notification_play)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)//锁屏显示全部内容
                .setOngoing(true)
                .setContentIntent(
                        PendingIntent.getService(this, 0, new Intent(this,
                                MusicPlayService.class).putExtra("playing",
                                "start"), PendingIntent.FLAG_UPDATE_CURRENT))
                .setContent(mRemoteViews);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String playing = intent.getStringExtra("playing");
            if ("close".equals(playing)) {
                mNotificationManager.cancel(666);
                stopSelf();
                EventBus.getDefault().post(new CloseEvent());
            } else if ("start".equals(playing)) {//启动app
                reStartActivity();
            } else {
                refreshPlayer(intent);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void reStartActivity() {
        Intent intent = new Intent(getApplicationContext(),
                MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = manager
                .getRunningTasks(Integer.MAX_VALUE);
        if (tasks == null)
            startActivity(intent);
        else
            for (RunningTaskInfo task : tasks) {
                if (task.baseActivity.getPackageName().equals(getPackageName())) {
                    try {
                        startActivity(new Intent(getApplicationContext(), Class.forName(task.topActivity.getClassName()))
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        startActivity(intent);
    }

    private void sendNotification(int mode) {
        if (playingId == playingmusic.getId() && isPrepared) {
            if (mode == SEND_NOTIFICATION_PLAY) {
                mRemoteViews.setImageViewResource(R.id.id_notification_pause,
                        R.drawable.activity_notification_pause);
                mPendingIntent = PendingIntent.getService(this, 2,
                        mIntent.putExtra("playing", "pause"),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                GlobalVariables.playStatus = GlobalVariables.PLAY_STATUS_PLAYING;
                mBuilder.setTicker(playingmusic.getName());
                EventBus.getDefault().post(new PlayingStatusChangeEvent(GlobalVariables.PLAY_STATUS_PLAYING));
            } else if (mode == SEND_NOTIFICATION_PAUSE) {
                mRemoteViews.setImageViewResource(R.id.id_notification_pause,
                        R.drawable.activity_notification_play);
                mPendingIntent = PendingIntent.getService(this, 2,
                        mIntent.putExtra("playing", "play"),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setTicker("");
                GlobalVariables.playStatus = GlobalVariables.PLAY_STATUS_PAUSE;
                EventBus.getDefault().post(new PlayingStatusChangeEvent(GlobalVariables.PLAY_STATUS_PAUSE));
            }
            mRemoteViews.setOnClickPendingIntent(R.id.id_notification_pause, mPendingIntent);
        } else {
            mRemoteViews.setTextViewText(R.id.id_notification_name,
                    playingmusic.getName());
            mRemoteViews.setTextViewText(R.id.id_notification_author,
                    playingmusic.getAuthor());
            mRemoteViews.setImageViewResource(R.id.id_notification_pause,
                    R.drawable.activity_notification_pause);
            mRemoteViews.setImageViewResource(R.id.id_notification_img, R.drawable.music_default_img);
//            if (TextUtils.isEmpty(playingmusic.getImage())) {
//            }else{
//                //mRemoteViews.
//                mRemoteViews.setImageViewBitmap(R.id.id_notification_img, );
//            }
            mPendingIntent = PendingIntent.getService(this, 1,
                    mIntent.putExtra("playing", "last"),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mRemoteViews.setOnClickPendingIntent(R.id.id_notification_last,
                    mPendingIntent);

            mPendingIntent = PendingIntent.getService(this, 2,
                    mIntent.putExtra("playing", "pause"),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mRemoteViews.setOnClickPendingIntent(R.id.id_notification_pause,
                    mPendingIntent);

            mPendingIntent = PendingIntent.getService(this, 3,
                    mIntent.putExtra("playing", "next"),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mRemoteViews.setOnClickPendingIntent(R.id.id_notification_next,
                    mPendingIntent);
            mBuilder.setTicker(playingmusic.getName());
            GlobalVariables.playStatus = GlobalVariables.PLAY_STATUS_PLAYING;
            EventBus.getDefault().post(new PlayingMusicChangeEvent(playingmusic));
            EventBus.getDefault().post(new PlayingStatusChangeEvent(GlobalVariables.PLAY_STATUS_PLAYING));
        }
        mBuilder.setContent(mRemoteViews);
        // mNotificationManager.notify(666, mBuilder.build());
        startForeground(666, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MusicPlayBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        super.onDestroy();
        System.exit(0);
    }

    public void refreshPlayer(Intent intent) {
        String playing = intent.getStringExtra("playing");
        if ("playingbyid".equals(playing)) {
            int id = intent.getIntExtra("id", -1);
            if (playingmusic != null && mp.isPlaying()) {
                if (playingmusic.getId() == id) {
                    mp.pause();
                    sendNotification(SEND_NOTIFICATION_PAUSE);
                } else {
                    positon = MediaUtils.getPositionInMusicList(GlobalVariables.playQuene, id);
                    playingmusic = GlobalVariables.playQuene.get(positon);
                    sendNotification(SEND_NOTIFICATION_PLAY);
                    playMusic();
                }
            } else {
                if (playingmusic != null && playingmusic.getId() == id && isPrepared) {
                    mp.start();
                    sendNotification(SEND_NOTIFICATION_PLAY);
                } else {
                    positon = MediaUtils.getPositionInMusicList(GlobalVariables.playQuene, id);
                    playingmusic = GlobalVariables.playQuene.get(positon);
                    sendNotification(SEND_NOTIFICATION_PLAY);
                    playMusic();
                }
            }
        } else if ("next".equals(playing)) {
            positon++;
            positon = positon % GlobalVariables.playQuene.size();
            playingmusic = GlobalVariables.playQuene.get(positon);
            sendNotification(SEND_NOTIFICATION_PLAY);
            playMusic();
            return;
        } else if ("pause".equals(playing)) {
            mp.pause();
            sendNotification(SEND_NOTIFICATION_PAUSE);
            return;
        } else if ("play".equals(playing)) {
            sendNotification(SEND_NOTIFICATION_PLAY);
            if(isPrepared){
                mp.start();
            }else{
                playMusic();
            }
            return;
        } else if ("last".equals(playing)) {
            positon--;
            if (positon == -1)
                positon = GlobalVariables.playQuene.size() - 1;
            playingmusic = GlobalVariables.playQuene.get(positon);
            sendNotification(SEND_NOTIFICATION_PLAY);
            playMusic();
            return;
        } else if ("changetime".equals(playing)) {
            int time = intent.getIntExtra("time", -1);
            if (time >= 0) {
                mp.seekTo(time * 1000);
                sendBroadcast(refreshTimeIntent);
            }
        }
    }

    private void playMusic() {
        try {
            isPrepared = false;
            mp.reset();
            mp.setDataSource(playingmusic.getAddr());
            mp.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mVisualizer != null) {
            mVisualizer.setEnabled(false);
        }
        switch (GlobalVariables.playingMode) {
            case ConfigData.PLAYING_MUSIC_MODE_ONLY:// 单曲循环
                playMusic();
                break;
            case ConfigData.PLAYING_MUSIC_MODE_ALL:// 顺序循环
                positon++;
                positon = positon % GlobalVariables.playQuene.size();
                playingmusic = GlobalVariables.playQuene.get(positon);
                sendNotification(SEND_NOTIFICATION_PLAY);
                playMusic();
                break;
            case ConfigData.PLAYING_MUSIC_MODE_RANDOM://随机播放
                Random random = new Random();
                positon = random.nextInt(GlobalVariables.playQuene.size());
                playingmusic = GlobalVariables.playQuene.get(positon);
                sendNotification(SEND_NOTIFICATION_PLAY);
                playMusic();
                break;
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        this.mp.start();
        mVisualizer.setEnabled(true);
        sendNotification(SEND_NOTIFICATION_PLAY);
        playingId = playingmusic.getId();
        MediaUtils.savePlayingPosition(this, positon);
        MediaUtils.refreshRecentPlayList(playingmusic);
        EventBus.getDefault().post(new RefreshRecentPlayListEvent());
    }

    public void releasePlayer() {
        mp.release();
        mVisualizer.setEnabled(false);
        isPrepared = false;
        mp = null;
        mVisualizer = null;
    }

    public boolean musicIsPlaying() {
        return mp.isPlaying();
    }

    public int getPlayingMusicPosition() {
        int currentPosition = 0;
        if (isPrepared) {
            currentPosition = mp.getCurrentPosition();
        }
        LogUtil.i("getPlayingMusicPosition", "position : " + currentPosition);
        return currentPosition;
    }

    public int getPlayingMusicTotalTime() {
        int totalTime = playingmusic.getAllTime();
        if (isPrepared) {
            totalTime = mp.getDuration();
        }
        return totalTime < 0 ? playingmusic.getAllTime() : totalTime;
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        if (fft != null && fft.length > 0) {
            byte[] model = new byte[fft.length / 2 + 1];
            model[0] = (byte) Math.abs(fft[1]);
            int j = 1;
            for (int i = 2; i < fft.length - 1; ) {
                model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
                i += 2;
                j++;
            }
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        LogUtil.i("onBufferingUpdate", "percent: " + percent);
        EventBus.getDefault().post(new RefreshPlayProgressEvent(getPlayingMusicPosition(), percent, getPlayingMusicTotalTime()));
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                LogUtil.i("onInfo", "MEDIA_INFO_BUFFERING_START");
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                LogUtil.i("onInfo", "MEDIA_INFO_BUFFERING_END");
                break;
        }
        return false;
    }

    public class MusicPlayBinder extends Binder {
        public Music getPlayingMusic() {
            return playingmusic;
        }

        public int getPlayStatus() {
            return GlobalVariables.playStatus;
        }

        public int getProgress() {
            return getPlayingMusicPosition();
        }

        public int getSecondProgress() {
            return 0;
        }

        public int getMaxProgress() {
            return getPlayingMusicTotalTime();
        }
    }

}
