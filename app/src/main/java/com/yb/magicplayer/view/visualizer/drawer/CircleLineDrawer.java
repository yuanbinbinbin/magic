package com.yb.magicplayer.view.visualizer.drawer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;

import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;
import com.yb.magicplayer.view.visualizer.drawer.base.ColorDrawerBase;

public class CircleLineDrawer extends ColorDrawerBase {

    public CircleLineDrawer(Paint paint) {
        this(paint, false);
    }


    public CircleLineDrawer(Paint paint, boolean cycleColor) {
        this(paint, cycleColor, 0.7f, 0.4f);
    }

    public CircleLineDrawer(Paint paint, boolean cycleColor, float modulation, float aggresive) {
        super();
        mPaint = paint;
        mCycleColor = cycleColor;
        reSetColor();
        this.modulation = modulation;
        this.aggresive = aggresive;
    }

    @Override
    public void onDraw(Canvas canvas, WaveBean data, Rect rect) {
        if (mCycleColor) {
            cycleColor();
        }

        for (int i = 0; i < data.getBytes().length - 1; i++) {
            float[] cartPoint = {
                    (float) i / (data.getBytes().length - 1),
                    rect.height() / 2 + ((byte) (data.getBytes()[i] + 128)) * (rect.height() / 2) / 128
            };

            float[] polarPoint = toPolar(cartPoint, rect);
            mPoints[i * 4] = polarPoint[0];
            mPoints[i * 4 + 1] = polarPoint[1];

            float[] cartPoint2 = {
                    (float) (i + 1) / (data.getBytes().length - 1),
                    rect.height() / 2 + ((byte) (data.getBytes()[i + 1] + 128)) * (rect.height() / 2) / 128
            };

            float[] polarPoint2 = toPolar(cartPoint2, rect);
            mPoints[i * 4 + 2] = polarPoint2[0];
            mPoints[i * 4 + 3] = polarPoint2[1];
        }
        canvas.drawLines(mPoints, mPaint);
    }

    @Override
    public void onDraw(Canvas canvas, FFTBean data, Rect rect) {
    }

    float modulation = 0.7f;//缩放大小
    float aggresive = 0.4f;//刺的突出程度

    private float[] toPolar(float[] cartesian, Rect rect) {
        double cX = rect.width() / 2;
        double cY = rect.height() / 2;
        double angle = (cartesian[0]) * 2 * Math.PI;//Math.sin() 参数是弧度制角度，一个完整的圆的弧度是2π，所以：2π rad = 360°，1 π rad = 180°，1°=π/180° rad ，1 rad = (180/π)°≈57.30°=57°18ˊ[2]  [1]
        //double radius = ((rect.width() / 2) * (1 - aggresive) + aggresive * cartesian[1] / 2) * (1.2 + Math.sin(modulation)) / 2.2;
        double radius = ((rect.width() / 2) * (1 - aggresive) + aggresive * cartesian[1] / 2) * modulation;
        float[] out = {
                (float) (cX + radius * Math.sin(angle)),
                (float) (cY + radius * Math.cos(angle))
        };
        return out;
    }

    /**
     * 获取缩放大小
     *
     * @return
     */
    public float getModulation() {
        return modulation;
    }

    /**
     * 设置缩放大小
     *
     * @return
     */
    public void setModulation(float modulation) {
        this.modulation = modulation;
    }

    /**
     * 获取刺的突出程度
     *
     * @return
     */
    public float getAggresive() {
        return aggresive;
    }

    /**
     * 设置刺的突出程度
     *
     * @return
     */
    public void setAggresive(float aggresive) {
        this.aggresive = aggresive;
    }
}
