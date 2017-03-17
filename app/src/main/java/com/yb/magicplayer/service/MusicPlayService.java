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
import com.yb.magicplayer.entity.LocalMusic;
import com.yb.magicplayer.utils.ConfigData;
import com.yb.magicplayer.utils.GlobalVariables;
import com.yb.magicplayer.utils.MediaUtils;
import com.yb.magicplayer.utils.PreferencesUtils;

public class MusicPlayService extends Service implements OnCompletionListener, Visualizer.OnDataCaptureListener, MediaPlayer.OnPreparedListener {

    private LocalMusic playingmusic;

    private static int SEND_NOTIFICATION_PLAY = 0;
    private static int SEND_NOTIFICATION_PAUSE = 1;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews mRemoteViews;

    private PendingIntent mPendingIntent;
    private int playingId;

    private Intent mIntent;
    private Intent broadcastIntent;
    private Intent refreshTimeIntent;
    private int playingposition;
    private MyBinder myBinder = new MyBinder();
    private ImageView mIvMusicImg;
    private MediaPlayer mp;  //用于音乐波荡
    private int positon;   //音乐播放的位置
    private Visualizer mVisualizer;//用于获取音乐频率

    @Override
    public void onCreate() {
        //getMusicInfoFromSharedPreference();
        initData();
        initMediaPlayer();//初始化播放器
        initIntent();
        initNotification();
    }

    private void initData() {
    }

    private void initIntent() {
        mIntent = new Intent(this, MusicPlayService.class);
        broadcastIntent = new Intent("RefreshInfor");
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
            } else if ("start".equals(playing)) {
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
        if (playingId == playingmusic.getId()) {
            if (mode == SEND_NOTIFICATION_PLAY) {
                mRemoteViews.setImageViewResource(R.id.id_notification_pause,
                        R.drawable.activity_notification_pause);
                mPendingIntent = PendingIntent.getService(this, 2,
                        mIntent.putExtra("playing", "pause"),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setTicker(playingmusic.getName());

            } else if (mode == SEND_NOTIFICATION_PAUSE) {
                mRemoteViews.setImageViewResource(R.id.id_notification_pause,
                        R.drawable.activity_notification_play);
                mPendingIntent = PendingIntent.getService(this, 2,
                        mIntent.putExtra("playing", "play"),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setTicker("");
            }
            mRemoteViews.setOnClickPendingIntent(R.id.id_notification_pause,
                    mPendingIntent);
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
            playingId = playingmusic.getId();

        }
        mBuilder.setContent(mRemoteViews);
        // mNotificationManager.notify(666, mBuilder.build());
        startForeground(666, mBuilder.build());
        sendBroadcast(broadcastIntent);
        return;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        PreferencesUtils.savePrefInt(this, "playinglocalmusicid", playingmusic.getId());
        super.onDestroy();
        System.exit(0);
    }

    private void getMusicInfoFromSharedPreference() {

        playingId = PreferencesUtils.loadPrefInt(this, "playinglocalmusicid", -1);
        playingposition = MediaUtils.getPositionInLocalMusicList(
                GlobalVariables.listLocalMusic, playingId);

        if (playingposition != -1) {
            playingmusic = GlobalVariables.listLocalMusic.get(playingposition);
        } else {
            if (GlobalVariables.listLocalMusic == null || GlobalVariables.listLocalMusic.size() <= 0)
                playingmusic = new LocalMusic();
            else
                playingmusic = GlobalVariables.listLocalMusic.get(0);
        }
    }


    private void initMediaPlayer() {
        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);
        mp.setOnPreparedListener(this);
        mVisualizer = new Visualizer(mp.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, true, true);
        mVisualizer.setEnabled(false);
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
                    positon = MediaUtils.getPositionInLocalMusicList(GlobalVariables.listLocalMusic, id);
                    playingmusic = GlobalVariables.listLocalMusic.get(positon);
                    playMusic();
                    sendNotification(SEND_NOTIFICATION_PLAY);
                }
            } else {
                if (playingmusic != null && playingmusic.getId() == id) {
                    mp.start();
                    sendNotification(SEND_NOTIFICATION_PLAY);
                } else {
                    positon = MediaUtils.getPositionInLocalMusicList(GlobalVariables.listLocalMusic, id);
                    playingmusic = GlobalVariables.listLocalMusic.get(positon);
                    playMusic();
                    sendNotification(SEND_NOTIFICATION_PLAY);
                }
            }
        } else if ("next".equals(playing)) {
            positon++;
            positon = positon % GlobalVariables.listLocalMusic.size();
            playingmusic = GlobalVariables.listLocalMusic.get(positon);
            playMusic();
            sendNotification(SEND_NOTIFICATION_PLAY);
            return;
        } else if ("pause".equals(playing)) {
            mp.pause();
            sendNotification(SEND_NOTIFICATION_PAUSE);
            return;
        } else if ("play".equals(playing)) {
            mp.start();
            sendNotification(SEND_NOTIFICATION_PLAY);
            return;
        } else if ("last".equals(playing)) {
            positon--;
            if (positon == -1)
                positon = GlobalVariables.listLocalMusic.size() - 1;
            playingmusic = GlobalVariables.listLocalMusic.get(positon);
            playMusic();
            sendNotification(SEND_NOTIFICATION_PLAY);
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
                positon = positon % GlobalVariables.listLocalMusic.size();
                playingmusic = GlobalVariables.listLocalMusic.get(positon);
                playMusic();
                break;
            case ConfigData.PLAYING_MUSIC_MODE_RANDOM://随机播放
                Random random = new Random();
                positon = random.nextInt(GlobalVariables.listLocalMusic.size());
                playingmusic = GlobalVariables.listLocalMusic.get(positon);
                playMusic();
                break;
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        playingId = playingmusic.getId();
        this.mp.start();
        mVisualizer.setEnabled(true);
        sendNotification(SEND_NOTIFICATION_PLAY);
    }

    public void releasePlayer() {
        mp.release();
        mVisualizer.setEnabled(false);
        mp = null;
        mVisualizer = null;
    }

    public boolean isPlaying() {
        return mp.isPlaying();
    }

    public int getPlayingPosition() {
        return mp.getCurrentPosition() / 1000;
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

    public class MyBinder extends Binder {
        public LocalMusic getPlayingLocalMusic() {
            return playingmusic;
        }

        public boolean isPlayingLocalMusic() {
            return isPlaying();
        }

        public int getPlayingPosition() {
            return getPlayingPosition();
        }
    }

}
