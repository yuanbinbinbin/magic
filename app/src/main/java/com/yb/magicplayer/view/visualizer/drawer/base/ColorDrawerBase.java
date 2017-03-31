package com.yb.magicplayer.view.visualizer.drawer.base;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;

abstract public class ColorDrawerBase extends DrawerBase {
    protected float redAdd; //红色渐变增量值
    protected float greenAdd;//绿色渐变增量值
    protected float blueAdd;//蓝色渐变增量值
    protected Paint mPaint;
    protected boolean mCycleColor;//颜色是否渐变

    public ColorDrawerBase() {
    }

    public void reSetColor() {
        setColor(0, 2, 4);
    }

    public void setColor(int r, int g, int b) {
        redAdd = r;
        greenAdd = g;
        blueAdd = b;
    }

    //设置线的宽度
    public void setStrokeWidth(float width) {
        if (mPaint != null) {
            mPaint.setStrokeWidth(width);
        }
    }

    protected float colorCounter = 0;//渐变进度

    protected void cycleColor() {
        int r = (int) Math.floor(128 * (Math.sin(colorCounter + redAdd) + 1));
        int g = (int) Math.floor(128 * (Math.sin(colorCounter + greenAdd) + 1));
        int b = (int) Math.floor(128 * (Math.sin(colorCounter + blueAdd) + 1));
        mPaint.setColor(Color.argb(128, r, g, b));
        colorCounter += 0.03;
    }
}
