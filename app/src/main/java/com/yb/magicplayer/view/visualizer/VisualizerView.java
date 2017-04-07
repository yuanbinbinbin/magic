package com.yb.magicplayer.view.visualizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.yb.magicplayer.utils.LogUtil;
import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.base.DrawerBase;


public class VisualizerView extends View {
    private static final String TAG = "VisualizerView";

    private byte[] mBytes;
    private byte[] mFFTBytes;
    private Rect mRect = new Rect();
    private Paint mFadePaint = new Paint();
    private DrawerBase drawer;
    private boolean isRenew = true;//是否恢复到初始状态
    private boolean isCanDraw = true;//是否可以画
    private byte[] updateWaveData;
    private byte[] updateFFTData;
    private Handler handler = new Handler();
    private Runnable runable = new Runnable() {
        @Override
        public void run() {
            if (isRenew) {
                updateVisualizer(updateWaveData);
                updateVisualizerFFT(updateFFTData);
                handler.postDelayed(runable, 100);
            }
        }
    };

    public VisualizerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VisualizerView(Context context) {
        this(context, null);
    }

    private void init() {
        mBytes = null;
        mFFTBytes = null;
        mFadePaint.setColor(Color.argb(238, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
        mFadePaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
        updateWaveData = new byte[1024];
        for (int i = 0; i < 1024; i++) {
            updateWaveData[i] = (byte) 128;
        }
        updateFFTData = new byte[1024];
        handler.postDelayed(runable, 100);
    }

    public void setDrawer(DrawerBase drawer) {
        this.drawer = drawer;
        flash();
    }

    public void setIsCanDraw(boolean isCanDraw) {
        this.isCanDraw = isCanDraw;
    }

    //更新Wave动画
    public void updateVisualizer(byte[] bytes) {
        if(isCanDraw){
            mBytes = bytes;
            invalidate();
        }
    }

    //更新bar状态动画
    public void updateVisualizerFFT(byte[] bytes) {
        if(isCanDraw){
            mFFTBytes = bytes;
            invalidate();
        }
    }

    Bitmap mCanvasBitmap;
    Canvas mCanvas;

    boolean mFlash = false;//是否更新界面

    //更新界面
    public void flash() {
        mFlash = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create canvas once we're ready to draw
        mRect.set(0, 0, getWidth(), getHeight());
        if (mCanvasBitmap == null) {
            mCanvasBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
        }
        if (mCanvas == null) {
            mCanvas = new Canvas(mCanvasBitmap);
        }
        if (mFlash) {
            Paint paint = new Paint();
            paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            mCanvas.drawPaint(paint);
            mFlash = false;
        }
        if (mBytes != null) {
            // Render all audio renderers
            WaveBean audioData = new WaveBean(mBytes);
            drawer.draw(mCanvas, audioData, mRect);
        }
        if (mFFTBytes != null) {
            // Render all FFT renderers
            FFTBean fftData = new FFTBean(mFFTBytes);
            drawer.draw(mCanvas, fftData, mRect);
        }
        // Fade out old contents
        mCanvas.drawPaint(mFadePaint);
        canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);
    }

    // 设置是否恢复到默认状态
    public void setIsRenew(boolean isRenew) {
        this.isRenew = isRenew;
        if (isRenew) {
            handler.postDelayed(runable, 100);
        } else {
            handler.removeCallbacks(runable);
        }
    }
}