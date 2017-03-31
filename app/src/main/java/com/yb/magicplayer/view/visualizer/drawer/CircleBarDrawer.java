/**
 * Copyright 2011, Felix Palmer
 * <p/>
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package com.yb.magicplayer.view.visualizer.drawer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;

import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.base.ColorDrawerBase;

public class CircleBarDrawer extends ColorDrawerBase {
    private int mDivisions;
    float modulation = 0.7f;//缩放大小
    float aggresive = 0.4f;//刺的突出程度

    public CircleBarDrawer(Paint paint, int divisions) {
        this(paint, divisions, false);
    }


    public CircleBarDrawer(Paint paint, int divisions, boolean cycleColor) {
        super();
        mPaint = paint;
        mDivisions = divisions;
        mCycleColor = cycleColor;
        reSetColor();
    }

    @Override
    public void onDraw(Canvas canvas, WaveBean data, Rect rect) {
    }
    boolean isDrawing = false;
    @Override
    public void onDraw(Canvas canvas, FFTBean data, Rect rect) {
        if(isDrawing){
            return;
        }
        isDrawing = true;
        if (mCycleColor) {
            cycleColor();
        }

        for (int i = 0; i < data.getBytes().length / mDivisions; i++) {
            byte rfk = data.getBytes()[mDivisions * i];
            byte ifk = data.getBytes()[mDivisions * i + 1];
            float magnitude = (rfk * rfk + ifk * ifk);
            float dbValue = 150 * (float) Math.log10(magnitude);//相对高度

            float[] cartPoint = {
                    (float) (i * mDivisions) / (data.getBytes().length - 1),
                    rect.height() / 2 - dbValue / 4
            };

            float[] polarPoint = toPolar(cartPoint, rect);
            mFFTPoints[i * 4] = polarPoint[0];
            mFFTPoints[i * 4 + 1] = polarPoint[1];

            float[] cartPoint2 = {
                    (float) (i * mDivisions) / (data.getBytes().length - 1),
                    rect.height() / 2 + dbValue
            };

            float[] polarPoint2 = toPolar(cartPoint2, rect);
            mFFTPoints[i * 4 + 2] = polarPoint2[0];
            mFFTPoints[i * 4 + 3] = polarPoint2[1];
        }
        canvas.drawLines(mFFTPoints, mPaint);
        isDrawing =false;
    }

    private float[] toPolar(float[] cartesian, Rect rect) {
        double cX = rect.width() / 2;
        double cY = rect.height() / 2;
        double angle = (cartesian[0]) * 2 * Math.PI;
        //  double radius = ((rect.width() / 2) * (1 - aggresive) + aggresive * cartesian[1] / 2) * ((1 - modulationStrength) + modulationStrength * (1 + Math.sin(modulation)) / 2);
        double radius = ((rect.width() / 2) * (1 - aggresive) + aggresive * cartesian[1] / 2) * modulation;
        float[] out = {
                (float) (cX + radius * Math.sin(angle)),
                (float) (cY + radius * Math.cos(angle))
        };
        return out;
    }

    public float getModulation() {
        return modulation;
    }

    public void setModulation(float modulation) {
        this.modulation = modulation;
    }

    public float getAggresive() {
        return aggresive;
    }

    public void setAggresive(float aggresive) {
        this.aggresive = aggresive;
    }
}
