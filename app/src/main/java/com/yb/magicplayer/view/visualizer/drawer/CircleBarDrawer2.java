/**
 * Copyright 2011, Felix Palmer
 * <p>
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package com.yb.magicplayer.view.visualizer.drawer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.base.ColorDrawerBase;

public class CircleBarDrawer2 extends ColorDrawerBase {
    private int mDivisions;
    float modulation = 0.7f;//缩放大小
    float aggresive = 0.4f;//刺的突出程度

    public CircleBarDrawer2(Paint paint, int divisions) {
        this(paint, divisions, false);
    }


    public CircleBarDrawer2(Paint paint, int divisions, boolean cycleColor) {
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
        int length = data.getBytes().length;
        for (int i = 0; i < length / mDivisions; i++) {
            byte rfk = data.getBytes()[mDivisions * i];
            byte ifk = data.getBytes()[mDivisions * i + 1];
            if (rfk == 0) {
                rfk = 1;
            }
            if (ifk == 0) {
                ifk = 1;
            }
            // float magnitude = (rfk * rfk + ifk * ifk);
            //byte范围为-128~127 故rfk、ifk平方再相加然后开方值的范围在0-181
            float dbValue = (int) Math.hypot(rfk, ifk);//相对高度
            float l = dbValue * rect.height() / 5 / 181;
            float[] cartPoint = {
                    (float) (i * mDivisions) / (data.getBytes().length - 1),
                    rect.height() / 3
            };

            float[] polarPoint = toPolar(cartPoint, rect);
            mFFTPoints[i * 4] = polarPoint[0];
            mFFTPoints[i * 4 + 1] = polarPoint[1];

            float[] cartPoint2 = {
                    (float) (i * mDivisions) / (data.getBytes().length - 1),
                    rect.height() / 3 + dbValue
            };

            float[] polarPoint2 = toPolar(cartPoint2, rect);
            mFFTPoints[i * 4 + 2] = polarPoint2[0];
            mFFTPoints[i * 4 + 3] = polarPoint2[1];
        }
        canvas.drawLines(mFFTPoints, mPaint);
    }

    private float[] toPolar(float[] cartesian, Rect rect) {
        double cX = rect.width() / 2;
        double cY = rect.height() / 2;
        double angle = (cartesian[0]) * 2 * Math.PI;
        double radius = cartesian[1];
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
