package com.yb.magicplayer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 播放列表
 * Created by yb on 2017/3/16.
 */
public class PlayList {
    private List<Music> playList;
    private String name;

    public PlayList(List<Music> playList, String name) {
        this.playList = playList;
        this.name = name;
    }

    public PlayList() {
        playList = new ArrayList<Music>();
        name = "";
    }

    public List<Music> getPlayList() {
        return playList;
    }

    public void setPlayList(List<Music> playList) {
        this.playList = playList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return playList == null ? 0 : playList.size();
    }

    @Override
    public String toString() {
        return "PlayList{" +
                "playList=" + playList +
                ", name='" + name + '\'' +
                '}';
    }
}
