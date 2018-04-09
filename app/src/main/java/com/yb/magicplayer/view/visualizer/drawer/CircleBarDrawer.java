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


    @Override
    public void onDraw(Canvas canvas, FFTBean data, Rect rect) {

        if (mCycleColor) {
            cycleColor();
        }
        byte[] bytes = data.getBytes();
        int length = bytes.length;
        int totalFor = length / mDivisions;
        int width2 = rect.width() / 2;
        int height2 = rect.height() / 2;
        for (int i = 0; i < totalFor; i++) {
            int location = i * mDivisions;
            byte rfk = bytes[location / 2];
            byte ifk = bytes[location / 2 + 1];
            if (rfk == 0) {
                rfk = 1;
            }
            if (ifk == 0) {
                ifk = 1;
            }
            float magnitude = (rfk * rfk + ifk * ifk);
            float dbValue = 150 * (float) Math.log10(magnitude);//相对高度
            float percent = (float) location / (length - 1);
            //计算起点坐标
            float[] cartPoint = {
                    percent,
                    height2
            };
            float[] polarPoint = toPolar(cartPoint, width2, height2);
            mFFTPoints[i * 4] = polarPoint[0];
            mFFTPoints[i * 4 + 1] = polarPoint[1];

            //计算终点坐标
            float[] cartPoint2 = {
                    percent,
                    height2 + dbValue
            };
            float[] polarPoint2 = toPolar(cartPoint2, width2, height2);
            mFFTPoints[i * 4 + 2] = polarPoint2[0];
            mFFTPoints[i * 4 + 3] = polarPoint2[1];
        }
        canvas.drawLines(mFFTPoints, mPaint);
    }
    private double totalAngle = 2 * Math.PI;
    private float[] toPolar(float[] cartesian, float width2, float height2) {
        double cX = width2;
        double cY = height2;
        double angle = (cartesian[0]) * totalAngle;
        double radius = (width2 * (1 - aggresive) + aggresive * cartesian[1] / 2) * modulation;
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
