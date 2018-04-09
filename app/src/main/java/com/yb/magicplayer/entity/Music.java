package com.yb.magicplayer.entity;


import com.base.baselibrary.adapter.ItemViewTypes;
import com.base.baselibrary.entity.BaseItem;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 通用Music接口，用于最近播放、播放列表、喜欢等。
 * Created by yb on 2017/3/16.
 */
@Entity
public class Music extends BaseItem{
    public static final int TYPE_LOCAL_MUSIC = 1;//本地音乐
    public static final int TYPE_ONLINE_MUSIC = 2;//在线音乐
    @Id
    private long id;
    private String name;
    private String author;
    private int allTime;
    private int size;
    private String addr;
    private int album_id;
    private String album;
    private String image;
    private boolean isLike;//是否喜欢
    private String lrc;//歌词
    private int type;//0为本地 1为网络
    private boolean isPlaying;//是否正在播放
    @Keep
    public Music(long id, String name, String author, int allTime, int size,
            String addr, int album_id, String album, String image, boolean isLike,
            String lrc, int type, boolean isPlaying) {
        super(ItemViewTypes.ITEM_VIEW_TYPE_MUSIC);
        this.id = id;
        this.name = name;
        this.author = author;
        this.allTime = allTime;
        this.size = size;
        this.addr = addr;
        this.album_id = album_id;
        this.album = album;
        this.image = image;
        this.isLike = isLike;
        this.lrc = lrc;
        this.type = type;
        this.isPlaying = isPlaying;
    }
    @Keep
    public Music() {
        super(ItemViewTypes.ITEM_VIEW_TYPE_MUSIC);
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getAllTime() {
        return this.allTime;
    }
    public void setAllTime(int allTime) {
        this.allTime = allTime;
    }
    public int getSize() {
        return this.size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getAddr() {
        return this.addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public int getAlbum_id() {
        return this.album_id;
    }
    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }
    public String getAlbum() {
        return this.album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public boolean getIsLike() {
        return this.isLike;
    }
    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
    }
    public String getLrc() {
        return this.lrc;
    }
    public void setLrc(String lrc) {
        this.lrc = lrc;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public boolean getIsPlaying() {
        return this.isPlaying;
    }
    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
