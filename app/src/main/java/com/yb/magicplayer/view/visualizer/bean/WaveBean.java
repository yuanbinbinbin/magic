package com.yb.magicplayer.view.visualizer.bean;

/**
 * 波形数据
 */
public class WaveBean
{
  public WaveBean(byte[] bytes)
  {
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
