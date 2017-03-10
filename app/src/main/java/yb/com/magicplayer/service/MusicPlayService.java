package yb.com.magicplayer.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import yb.com.magicplayer.R;
import yb.com.magicplayer.activity.MainActivity;
import yb.com.magicplayer.entity.LocalMusic;
import yb.com.magicplayer.utils.GlobalVariables;
import yb.com.magicplayer.utils.MediaUtils;

public class MusicPlayService extends Service {

    private LocalMusic playingmusic;

    private static int SEND_NOTIFICATION_PLAY = 0;
    private static int SEND_NOTIFICATION_PAUSE = 1;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private RemoteViews mRemoteViews;

    private PendingIntent mPendingIntent;
    private int playingId;

    private Intent mIntent;
    private int playingMode;

    private MyThread mThread;

    private Intent broadcastIntent;
    private Intent refreshTimeIntent;
    private int playingposition;
    private MyBinder myBinder = new MyBinder();
    private ImageView mIvMusicImg;

    @Override
    public void onCreate() {
        playingmusic = new LocalMusic();
        getMusicInfoFromSharedPreference();

        mThread = new MyThread();
        mThread.start();
        // 获取上次关闭时所播放歌曲的信息必须在mThread和mListLocalMusics定义之后

        playingId = -1;
        mIntent = new Intent(this, MusicPlayService.class);

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

        playingMode = GlobalVariables.playingMode;

        broadcastIntent = new Intent("RefreshInfor");
        refreshTimeIntent = new Intent("RefreshTime");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String playing = intent.getStringExtra("playing");
            if ("close".equals(playing)) {
                Intent intent2 = new Intent("finish");
                sendBroadcast(intent2);
                mNotificationManager.cancel(666);
                stopSelf();
            } else if ("start".equals(playing)) {
                reStartActivity();
            } else {
                mThread.refreshPlayer(intent);
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
        mThread.stopThread();
        System.out.println("MusicPlayService--------------------onDestroy()");
        SharedPreferences mPreferences = getSharedPreferences("iplayer",
                Context.MODE_PRIVATE);
        Editor mEditor = mPreferences.edit();
        System.out.println("playinglocalmusicid" + playingmusic.getName());
        mEditor.putInt("playinglocalmusicid", playingmusic.getId());
        mEditor.commit();
        super.onDestroy();
        System.exit(0);
    }

    private void getMusicInfoFromSharedPreference() {
        SharedPreferences mPreferences = getSharedPreferences("iplayer",
                Context.MODE_PRIVATE);
        int playingLocalMusicId = mPreferences
                .getInt("playinglocalmusicid", -1);
        playingposition = MediaUtils.getPositionInLocalMusicList(
                GlobalVariables.listLocalMusic, playingLocalMusicId);

        if (playingposition != -1) {
            playingmusic = GlobalVariables.listLocalMusic.get(playingposition);
        } else {
            if (GlobalVariables.listLocalMusic == null || GlobalVariables.listLocalMusic.size() <= 0)
                playingmusic = null;
            else
                playingmusic = GlobalVariables.listLocalMusic.get(0);
        }
    }

    class MyThread extends Thread implements OnCompletionListener {
        private MediaPlayer mp;
        private boolean flag;
        private int positon;

        public MyThread() {
            mp = new MediaPlayer();
            flag = true;
            positon = -1;
            mp.setOnCompletionListener(this);
            if (playingposition != -1) {
                mp.reset();
                try {
                    mp.setDataSource(playingmusic.getAddr());
                    mp.prepare();
                    positon = playingposition;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void run() {
            while (flag) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void refreshPlayer(Intent intent) {
            String playing = intent.getStringExtra("playing");
            if ("playingbyid".equals(playing)) {
                int id = intent.getIntExtra("id", -1);
                if (mp.isPlaying()) {
                    if (playingmusic.getId() == id) {
                        mp.pause();
                        sendNotification(SEND_NOTIFICATION_PAUSE);
                    } else {
                        playingmusic = GlobalVariables.listLocalMusic.get(MediaUtils
                                .getPositionInLocalMusicList(GlobalVariables.listLocalMusic,
                                        id));
                        positon = MediaUtils.getPositionInLocalMusicList(
                                GlobalVariables.listLocalMusic, playingmusic.getId());
                        playMusic();
                    }
                } else {
                    if (playingmusic.getId() == id) {
                        mp.start();
                        sendNotification(SEND_NOTIFICATION_PLAY);
                    } else {
                        playingmusic = GlobalVariables.listLocalMusic.get(MediaUtils
                                .getPositionInLocalMusicList(GlobalVariables.listLocalMusic,
                                        id));
                        positon = MediaUtils.getPositionInLocalMusicList(
                                GlobalVariables.listLocalMusic, playingmusic.getId());
                        playMusic();
                    }
                }
                return;
            }
            positon = MediaUtils.getPositionInLocalMusicList(GlobalVariables.listLocalMusic,
                    playingmusic.getId());
            if ("next".equals(playing)) {
                positon++;
                positon = positon % GlobalVariables.listLocalMusic.size();
                playingmusic = GlobalVariables.listLocalMusic.get(positon);
                playMusic();
                sendNotification(SEND_NOTIFICATION_PLAY);
                return;
            }
            if ("pause".equals(playing)) {
                mp.pause();
                sendNotification(SEND_NOTIFICATION_PAUSE);
                return;
            }
            if ("play".equals(playing)) {
                mp.start();
                sendNotification(SEND_NOTIFICATION_PLAY);
                return;
            }
            if ("last".equals(playing)) {
                positon--;
                if (positon == -1)
                    positon = GlobalVariables.listLocalMusic.size() - 1;
                playingmusic = GlobalVariables.listLocalMusic.get(positon);
                playMusic();
                sendNotification(SEND_NOTIFICATION_PLAY);
                return;
            }
            if ("changetime".equals(playing)) {
                int time = intent.getIntExtra("time", 0);
                mp.seekTo(time * 1000);
                sendBroadcast(refreshTimeIntent);
            }
        }

        private void playMusic() {
            mp.reset();
            try {
                mp.setDataSource(playingmusic.getAddr());
                mp.prepare();
                mp.start();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            sendNotification(SEND_NOTIFICATION_PLAY);
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            switch (playingMode) {
                case 0:// 单曲循环
                    playMusic();
                    break;
                case 1:// 顺序循环
                    positon++;
                    positon = positon % GlobalVariables.listLocalMusic.size();
                    playingmusic = GlobalVariables.listLocalMusic.get(positon);
                    playMusic();
                    break;
                case 2://随机播放
                    Random random = new Random();
                    positon = random.nextInt(GlobalVariables.listLocalMusic.size());
                    playingmusic = GlobalVariables.listLocalMusic.get(positon);
                    playMusic();
                    break;
            }

        }

        public void stopThread() {
            mp.release();
            flag = false;
        }

        public boolean isPlaying() {
            return mp.isPlaying();
        }

        public int getPlayingPosition() {
            System.out.println("getposition------------------");
            return mp.getCurrentPosition() / 1000;
        }
    }

    public class MyBinder extends Binder {
        public LocalMusic getPlayingLocalMusic() {
            return playingmusic;
        }

        public boolean isPlayingLocalMusic() {
            return mThread.isPlaying();
        }

        public int getPlayingPosition() {
            return mThread.getPlayingPosition();
        }
    }
}
