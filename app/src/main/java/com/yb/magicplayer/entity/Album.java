package com.yb.magicplayer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 唱片列表
 * Created by yb on 2017/3/16.
 */
public class Album {
    private int albumid;
    private List<Music> musicList;
    private String name;

    public Album(int albumid, List<Music> musicList, String name) {
        this.albumid = albumid;
        this.musicList = musicList;
        this.name = name;
    }

    public Album() {
        musicList = new ArrayList<Music>();
        name = "";
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAlbumid() {
        return albumid;
    }

    public void setAlbumid(int albumid) {
        this.albumid = albumid;
    }

    public int getCount() {
        return musicList == null ? 0 : musicList.size();
    }

    @Override
    public String toString() {
        return "Album{" +
                "albumid=" + albumid +
                ", musicList=" + musicList +
                ", name='" + name + '\'' +
                '}';
    }
}
