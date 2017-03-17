package com.yb.magicplayer.entity;

public class OnlineMusic {
    private int id;
    private String name;
    private String author;
    private int allTime;
    private int size;
    private String addr;
    private int album_id;
    private String album;
    private String image;

    public OnlineMusic() {
        this.name = "";
        this.author = "";
        this.album = "";
        this.image = "";
        this.addr = "";
        this.allTime = 0;
        this.size = 0;
        this.id = 0;
        this.album_id = 0;
    }

    public OnlineMusic(int id, String name, String author, int allTime, int size, String addr, int album_id, String album, String image) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.allTime = allTime;
        this.size = size;
        this.addr = addr;
        this.album_id = album_id;
        this.album = album;
        this.image = image;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAllTime() {
        return allTime;
    }

    public void setAllTime(int allTime) {
        this.allTime = allTime;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
