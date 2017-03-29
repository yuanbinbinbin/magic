package com.yb.magicplayer.view.visualizer.drawer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;


public class LineDrawer extends DrawerBase {
    private Paint mPaint;
    private Paint mFlashPaint;
    private boolean mCycleColor;
    private float amplitude = 0;

    public LineDrawer(Paint paint, Paint flashPaint) {
        this(paint, flashPaint, false);
    }


    public LineDrawer(Paint paint, Paint flashPaint, boolean cycleColor) {
        super();
        mPaint = paint;
        mFlashPaint = flashPaint;
        mCycleColor = cycleColor;
    }

    @Override
    public void onDraw(Canvas canvas, WaveBean data, Rect rect) {
        if (mCycleColor) {
            cycleColor();
        }

        // Calculate points for line
        for (int i = 0; i < data.getBytes().length - 1; i++) {
            mPoints[i * 4] = rect.width() * i / (data.getBytes().length - 1);
            mPoints[i * 4 + 1] = rect.height() / 2
                    + ((byte) (data.getBytes()[i] + 128)) * (rect.height() / 2) / 128;
            mPoints[i * 4 + 2] = rect.width() * (i + 1) / (data.getBytes().length - 1);
            mPoints[i * 4 + 3] = rect.height() / 2
                    + ((byte) (data.getBytes()[i + 1] + 128)) * (rect.height() / 2) / 128;
        }

        // Calc amplitude for this waveform
        float accumulator = 0;
        for (int i = 0; i < data.getBytes().length - 1; i++) {
            accumulator += Math.abs(data.getBytes()[i]);
        }

        float amp = accumulator / (128 * data.getBytes().length);
        if (amp > amplitude) {
            // Amplitude is bigger than normal, make a prominent line
            amplitude = amp;
            canvas.drawLines(mPoints, mFlashPaint);
        } else {
            // Amplitude is nothing special, reduce the amplitude
           // amplitude *= 0.99;
            canvas.drawLines(mPoints, mPaint);
        }
    }

    @Override
    public void onDraw(Canvas canvas, FFTBean data, Rect rect) {
        // Do nothing, we only display audio data
    }

    private float colorCounter = 0;

    private void cycleColor() {
        int r = (int) Math.floor(128 * (Math.sin(colorCounter) + 3));
        int g = (int) Math.floor(128 * (Math.sin(colorCounter + 1) + 1));
        int b = (int) Math.floor(128 * (Math.sin(colorCounter + 7) + 1));
        mPaint.setColor(Color.argb(128, r, g, b));
        colorCounter += 0.03;
    }
}
