package com.yb.magicplayer.view.visualizer.drawer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;

import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.base.ColorDrawerBase;


public class LineDrawer extends ColorDrawerBase {

    public LineDrawer(Paint paint) {
        this(paint, false);
    }


    public LineDrawer(Paint paint, boolean cycleColor) {
        super();
        mPaint = paint;
        mCycleColor = cycleColor;
        reSetColor();
    }

    public LineDrawer(Paint paint, boolean cycleColor, int r, int g, int b) {
        super();
        mPaint = paint;
        mCycleColor = cycleColor;
        setColor(r, g, b);
    }

    @Override
    public void onDraw(Canvas canvas, WaveBean data, Rect rect) {
        if (mCycleColor) {
            cycleColor();
        }
        for (int i = 0; i < data.getBytes().length - 1; i++) {
            mPoints[i * 4] = rect.width() * i / (data.getBytes().length - 1);
            mPoints[i * 4 + 1] = rect.height() / 2
                    + ((byte) (data.getBytes()[i] + 128)) * (rect.height() / 5) / 128;
            mPoints[i * 4 + 2] = rect.width() * (i + 1) / (data.getBytes().length - 1);
            mPoints[i * 4 + 3] = rect.height() / 2
                    + ((byte) (data.getBytes()[i + 1] + 128)) * (rect.height() / 5) / 128;
        }
        canvas.drawLines(mPoints, mPaint);
    }

    @Override
    public void onDraw(Canvas canvas, FFTBean data, Rect rect) {
    }

}
