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
            //第i个点的在总纵轴上的坐标。他在画线上以总纵轴的1/2为基准线（mRect.height() / 2），所有的点或正或负以此线为基础标记。
            //((data.getBytes()[i] & 0xFF) - 128) onWaveFormDataCapture返回的是8位  无符号  的PCM单声道样本，因此返回的数值在0-255范围，
            //而java中的byte是有符号的所表示的范围是-128 - 127，因此我们需要将byte数据还原到0-255范围 ，byte & 0xFF 即可转换为源数据
            //然后以中间为0点故需要减去128 ，((byte) (mBytes[i] + 128)) 与 ((data.getBytes()[i] & 0xFF) - 128) 效果相同
            //(mRect.height() / 2) / 128就是将二分之一的总长度换算成128个刻度，因为我们的数据是byte类型，所以刻画成128个刻度正好
            mPoints[i * 4 + 1] = rect.height() / 2 + ((byte) (data.getBytes()[i] + 128)) * (rect.height() / 7) / 128;
            mPoints[i * 4 + 2] = rect.width() * (i + 1) / (data.getBytes().length - 1);
            mPoints[i * 4 + 3] = rect.height() / 2 + ((byte) (data.getBytes()[i + 1] + 128)) * (rect.height() / 7) / 128;
        }

        canvas.drawLines(mPoints, mPaint);
    }

    @Override
    public void onDraw(Canvas canvas, FFTBean data, Rect rect) {
    }

}
