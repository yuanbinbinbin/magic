package com.yb.magicplayer.events;

/**
 * 播放状态更改的Event
 * Created by yb on 2017/3/23.
 */
public class RefreshPlayProgressEvent {

    private int progress;
    private int secondProgress;
    private int maxProgress;

    public RefreshPlayProgressEvent(int progress, int secondProgress, int maxProgress) {
        this.progress = progress;
        this.secondProgress = secondProgress;
        this.maxProgress = maxProgress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getSecondProgress() {
        return secondProgress;
    }

    public void setSecondProgress(int secondProgress) {
        this.secondProgress = secondProgress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }
}
