package com.yb.magicplayer.view.visualizer.drawer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;

import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.base.ColorDrawerBase;
import com.yb.magicplayer.view.visualizer.drawer.base.DrawerBase;

public class LineBarDrawer extends ColorDrawerBase {
    private int mDivisions;
    private int mLocation;
    public static final int LOCATION_TOP = 1;
    public static final int LOCATION_BOTTOM = 2;
    public static final int LOCATION_CENTER = 3;

    public LineBarDrawer(int divisions, Paint paint, int location, boolean cycleColor) {
        super();
        mDivisions = divisions;
        mPaint = paint;
        mLocation = location;
        mCycleColor = cycleColor;
        reSetColor();
    }

    @Override
    public void onDraw(Canvas canvas, WaveBean data, Rect rect) {
    }

    @Override
    public void onDraw(Canvas canvas, FFTBean data, Rect rect) {
        if (mCycleColor) {
            cycleColor();
        }
        byte[] bytes = data.getBytes();
        int length = bytes.length;
        int totalFor = length / mDivisions;
        int height = rect.height() - 10;
        int centerY = canvas.getHeight() / 2;
        int temp = 3 * mDivisions;
        for (int i = 0; i < totalFor; i++) {
            mFFTPoints[i * 4] = (i + 1) * temp;
            mFFTPoints[i * 4 + 2] = (i + 1) * temp;
            byte rfk = bytes[mDivisions * i];
            byte ifk = bytes[mDivisions * i + 1];
            float magnitude = (rfk * rfk + ifk * ifk);
            int dbValue = (int) (50 * Math.log10(magnitude));
            if (dbValue < 5) {
                dbValue = 6;
            }
            if (LOCATION_TOP == mLocation) {
                mFFTPoints[i * 4 + 1] = 0;
                mFFTPoints[i * 4 + 3] = (dbValue * 2 - 10);
            } else if (LOCATION_BOTTOM == mLocation) {
                mFFTPoints[i * 4 + 1] = height;
                mFFTPoints[i * 4 + 3] = height - (dbValue * 2 - 10);
            } else {
                mFFTPoints[i * 4 + 1] = centerY;
                mFFTPoints[i * 4 + 3] = centerY - (dbValue * 2 - 10);
            }
            if (mFFTPoints[i * 4 + 2] > rect.width()) {
                mFFTPoints[i * 4 + 3] = 0;
                break;
            }
        }
        canvas.drawLines(mFFTPoints, mPaint);
    }
}
