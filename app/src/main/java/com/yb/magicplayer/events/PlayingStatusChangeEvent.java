package com.yb.magicplayer.events;

import com.yb.magicplayer.entity.Music;

/**
 * 播放状态更改的Event
 * Created by yb on 2017/3/23.
 */
public class PlayingStatusChangeEvent {

    private int status;

    public PlayingStatusChangeEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
