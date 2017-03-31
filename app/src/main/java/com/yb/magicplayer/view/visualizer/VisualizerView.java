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
import android.util.AttributeSet;
import android.view.View;

import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.base.DrawerBase;


public class VisualizerView extends View {
    private static final String TAG = "VisualizerView";

    private byte[] mBytes;
    private byte[] mFFTBytes;
    private Rect mRect = new Rect();

    private Paint mFlashPaint = new Paint();
    private Paint mFadePaint = new Paint();
    private DrawerBase drawer;

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
        mFlashPaint.setColor(Color.argb(122, 255, 255, 255));
        mFadePaint.setColor(Color.argb(238, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
        mFadePaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
    }

    public void setDrawer(DrawerBase drawer) {
        this.drawer = drawer;
    }

    /**
     * Pass data to the visualizer. Typically this will be obtained from the
     * Android Visualizer.OnDataCaptureListener call back. See
     * {@link Visualizer.OnDataCaptureListener#onWaveFormDataCapture }
     *
     * @param bytes
     */
    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    /**
     * Pass FFT data to the visualizer. Typically this will be obtained from the
     * Android Visualizer.OnDataCaptureListener call back. See
     * {@link Visualizer.OnDataCaptureListener#onFftDataCapture }
     *
     * @param bytes
     */
    public void updateVisualizerFFT(byte[] bytes) {
        mFFTBytes = bytes;
        invalidate();
    }

    boolean mFlash = false;
    Bitmap mCanvasBitmap;
    Canvas mCanvas;


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
        if (mFlash) {
            mFlash = false;
            mCanvas.drawPaint(mFlashPaint);
        }
        canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);
    }
}