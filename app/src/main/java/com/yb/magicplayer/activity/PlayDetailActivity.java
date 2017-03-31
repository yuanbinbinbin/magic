package com.yb.magicplayer.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;

import com.yb.magicplayer.R;
import com.yb.magicplayer.activity.base.EventBusBaseActivity;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.service.MusicPlayService;
import com.yb.magicplayer.utils.ImageUtil;
import com.yb.magicplayer.view.visualizer.VisualizerView;
import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.LineBarDrawer;
import com.yb.magicplayer.view.visualizer.drawer.CircleBarDrawer;
import com.yb.magicplayer.view.visualizer.drawer.CircleLineDrawer;
import com.yb.magicplayer.view.visualizer.drawer.LineDrawer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PlayDetailActivity extends EventBusBaseActivity {
    private VisualizerView vv;
    private ImageView mIvBg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_detail;
    }

    @Override
    protected void initView() {
        vv = (VisualizerView) findViewById(R.id.id_vv);
        mIvBg = (ImageView) findViewById(R.id.id_play_detail_bg);
    }

    @Override
    protected void initData() {
        type = 0;
        change(vv);
        bindMusicPlaySerivce();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshMusicInfo();
    }

    /**
     * 更新正在播放音乐的信息
     */
    private void refreshMusicInfo() {
        if (mMusicPlayBinder != null) {
            Music music = mMusicPlayBinder.getPlayingMusic();
            ImageUtil.loadImage(getContext(), music.getImage(), mIvBg);
        }
    }

    int type = 1;

    public void change(View view) {
        type++;
        if (type == 4) {
            Paint linePaint = new Paint();
            linePaint.setStrokeWidth(3f);
            linePaint.setAntiAlias(true);
            linePaint.setColor(Color.argb(88, 0, 128, 255));
            vv.setDrawer(new LineDrawer(linePaint, true));
        } else if (type == 2) {
            Paint paint = new Paint();
            paint.setStrokeWidth(3f);
            paint.setAntiAlias(true);
            paint.setColor(Color.argb(255, 222, 92, 143));
            vv.setDrawer(new CircleLineDrawer(paint, true));
        } else if (type == 1) {
            Paint paint = new Paint();
            paint.setStrokeWidth(8f);
            paint.setAntiAlias(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
            paint.setColor(Color.argb(255, 222, 92, 143));
            vv.setDrawer(new CircleBarDrawer(paint, 8, true));
        } else if (type == 3) {
            Paint paint2 = new Paint();
            paint2.setStrokeWidth(12f);
            paint2.setAntiAlias(true);
            paint2.setColor(Color.argb(200, 181, 111, 233));
            vv.setDrawer(new LineBarDrawer(8, paint2, LineBarDrawer.LOCATION_BOTTOM, true));
            type = 0;
        }
    }

    @Override
    protected void bindServiceSuccess() {
        super.bindServiceSuccess();
        refreshMusicInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshVisualizer(FFTBean event) {
        vv.updateVisualizerFFT(event.getBytes());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshVisualizer(WaveBean event) {
        vv.updateVisualizer(event.getBytes());
    }
}
