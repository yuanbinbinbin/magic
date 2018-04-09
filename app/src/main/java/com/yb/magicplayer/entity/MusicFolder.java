package com.yb.magicplayer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存音乐的文件夹
 * Created by yb on 2017/3/16.
 */
public class MusicFolder {
    private String name;
    private String path;
    private List<Music> musics;

    public MusicFolder() {
        name = "";
        path = "";
        musics = new ArrayList<Music>();
    }

    public MusicFolder(String name, String path, List<Music> musics) {
        this.name = name;
        this.path = path;
        this.musics = musics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public void setMusics(List<Music> musics) {
        this.musics = musics;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "MusicFolder{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", musics=" + musics +
                '}';
    }
}
