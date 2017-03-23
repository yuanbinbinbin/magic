package com.yb.magicplayer.events;

import com.yb.magicplayer.entity.Music;

/**
 * 播放歌曲更改的Event
 * Created by yb on 2017/3/23.
 */
public class PlayingMusicChangeEvent {
    private Music playingMusic;

    public PlayingMusicChangeEvent(Music playingMusic) {
        this.playingMusic = playingMusic;
    }

    public Music getPlayingMusic() {
        return playingMusic;
    }

    public void setPlayingMusic(Music playingMusic) {
        this.playingMusic = playingMusic;
    }
}
