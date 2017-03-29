package com.yb.magicplayer.view.visualizer.drawer;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.yb.magicplayer.view.visualizer.bean.FFTBean;
import com.yb.magicplayer.view.visualizer.bean.WaveBean;

abstract public class DrawerBase {
    // 用于存放波形图和频率点
    protected float[] mPoints;
    protected float[] mFFTPoints;

    public DrawerBase() {
    }

    abstract public void onDraw(Canvas canvas, WaveBean data, Rect rect);

    abstract public void onDraw(Canvas canvas, FFTBean data, Rect rect);


    final public void draw(Canvas canvas, WaveBean data, Rect rect) {
        if (data == null) {
            return;
        }
        byte[] bytes = data.getBytes();
        if (bytes == null || bytes.length <= 0) {
            return;
        }
        if (mPoints == null || mPoints.length < bytes.length * 4) {
            mPoints = new float[bytes.length * 4];
        }
        onDraw(canvas, data, rect);
    }

    final public void draw(Canvas canvas, FFTBean data, Rect rect) {
        if (data == null) {
            return;
        }
        byte[] bytes = data.getBytes();
        if (bytes == null || bytes.length <= 0) {
            return;
        }
        if (mFFTPoints == null || mFFTPoints.length < bytes.length * 4) {
            mFFTPoints = new float[bytes.length * 4];
        }
        onDraw(canvas, data, rect);
    }
}
