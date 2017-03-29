package com.yb.magicplayer.view.visualizer.bean;

/**
 * 频率数据
 */
public class FFTBean {
    public FFTBean(byte[] bytes) {
        this.bytes = bytes;
    }

    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
