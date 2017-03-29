package com.yb.magicplayer.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

import com.yb.magicplayer.R;
import com.yb.magicplayer.activity.base.EventBusBaseActivity;
import com.yb.magicplayer.events.RefreshRecentPlayListEvent;
import com.yb.magicplayer.view.visualizer.VisualizerView;
import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.BarGraphDrawer;
import com.yb.magicplayer.view.visualizer.drawer.CircleBarDrawer;
import com.yb.magicplayer.view.visualizer.drawer.CircleDrawer;
import com.yb.magicplayer.view.visualizer.drawer.LineDrawer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PlayDetailActivity extends EventBusBaseActivity {
    private VisualizerView vv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_detail;
    }

    @Override
    protected void initView() {
        vv = (VisualizerView) findViewById(R.id.id_vv);
    }

    @Override
    protected void initData() {
        type = 0;
        change(vv);
    }

    @Override
    protected void initListener() {

    }

    int type = 1;

    public void change(View view) {
        type++;
        if (type == 1) {
            Paint linePaint = new Paint();
            linePaint.setStrokeWidth(1f);
            linePaint.setAntiAlias(true);
            linePaint.setColor(Color.argb(88, 0, 128, 255));

            Paint lineFlashPaint = new Paint();
            lineFlashPaint.setStrokeWidth(5f);
            lineFlashPaint.setAntiAlias(true);
            lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
            vv.setDrawer(new LineDrawer(linePaint, lineFlashPaint, true));
        } else if (type == 2) {
            Paint paint = new Paint();
            paint.setStrokeWidth(3f);
            paint.setAntiAlias(true);
            paint.setColor(Color.argb(255, 222, 92, 143));
            vv.setDrawer(new CircleDrawer(paint, true));
        } else if (type == 3) {
            Paint paint = new Paint();
            paint.setStrokeWidth(8f);
            paint.setAntiAlias(true);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
            paint.setColor(Color.argb(255, 222, 92, 143));
            vv.setDrawer(new CircleBarDrawer(paint, 32, true));
        } else if (type == 4) {
            Paint paint = new Paint();
            paint.setStrokeWidth(50f);
            paint.setAntiAlias(true);
            paint.setColor(Color.argb(200, 56, 138, 252));
            vv.setDrawer(new BarGraphDrawer(16, paint, true));
        } else if (type == 5) {
            Paint paint2 = new Paint();
            paint2.setStrokeWidth(12f);
            paint2.setAntiAlias(true);
            paint2.setColor(Color.argb(200, 181, 111, 233));
            vv.setDrawer(new BarGraphDrawer(4, paint2, true));
            type = 0;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshVisualizer(FFTBean event){
        vv.updateVisualizerFFT(event.getBytes());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshVisualizer(WaveBean event){
        vv.updateVisualizer(event.getBytes());
    }
}
