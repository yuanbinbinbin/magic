package yb.com.magicplayer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 歌手列表
 * Created by yb on 2017/3/16.
 */
public class Artist {
    private List<Music> musicList;
    private String name;

    public Artist(List<Music> musicList, String name) {
        this.musicList = musicList;
        this.name = name;
    }

    public Artist() {
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

    public int getCount() {
        return musicList == null ? 0 : musicList.size();
    }

    @Override
    public String toString() {
        return "musicList{" +
                "musicList=" + musicList +
                ", name='" + name + '\'' +
                '}';
    }
}
