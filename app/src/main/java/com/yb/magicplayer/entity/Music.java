package com.yb.magicplayer.entity;

/**
 * 通用Music接口，用于最近播放、播放列表、喜欢等。
 * Created by yb on 2017/3/16.
 */
public class Music {
    public static final int TYPE_LOCAL_MUSIC = 1;//本地音乐
    public static final int TYPE_ONLINE_MUSIC = 2;//在线音乐
    private int type;
    private LocalMusic localMusic;
    private OnlineMusic onlineMusic;

    public Music() {
        type = TYPE_LOCAL_MUSIC;
    }

    public Music(int type, LocalMusic localMusic, OnlineMusic onlineMusic) {
        this.type = type;
        this.localMusic = localMusic;
        this.onlineMusic = onlineMusic;
    }

    public int getAlbum_id() {
        if (type == TYPE_LOCAL_MUSIC) {
            return localMusic == null ? -1 : localMusic.getAlbum_id();
        } else {
            return onlineMusic == null ? -1 : onlineMusic.getAlbum_id();
        }
    }

    public void setAlbum_id(int album_id) {
        if (type == TYPE_LOCAL_MUSIC) {
            if (localMusic != null) {
                localMusic.setAlbum_id(album_id);
            }
        } else {
            if (onlineMusic != null) {
                onlineMusic.setAlbum_id(album_id);
            }
        }
    }

    public int getId() {
        if (type == TYPE_LOCAL_MUSIC) {
            return localMusic == null ? -1 : localMusic.getId();
        } else {
            return onlineMusic == null ? -1 : onlineMusic.getId();
        }
    }

    public void setId(int id) {
        if (type == TYPE_LOCAL_MUSIC) {
            if (localMusic != null) {
                localMusic.setId(id);
            }
        } else {
            if (onlineMusic != null) {
                onlineMusic.setId(id);
            }
        }
    }

    public int getAllTime() {
        if (type == TYPE_LOCAL_MUSIC) {
            return localMusic == null ? -1 : localMusic.getAllTime();
        } else {
            return onlineMusic == null ? -1 : onlineMusic.getAllTime();
        }
    }

    public void setAllTime(int allTime) {
        if (type == TYPE_LOCAL_MUSIC) {
            if (localMusic != null) {
                localMusic.setAllTime(allTime);
            }
        } else {
            if (onlineMusic != null) {
                onlineMusic.setAllTime(allTime);
            }
        }
    }

    public int getSize() {
        if (type == TYPE_LOCAL_MUSIC) {
            return localMusic == null ? -1 : localMusic.getSize();
        } else {
            return onlineMusic == null ? -1 : onlineMusic.getSize();
        }
    }

    public void setSize(int size) {
        if (type == TYPE_LOCAL_MUSIC) {
            if (localMusic != null) {
                localMusic.setSize(size);
            }
        } else {
            if (onlineMusic != null) {
                onlineMusic.setSize(size);
            }
        }
    }

    public String getName() {
        if (type == TYPE_LOCAL_MUSIC) {
            return localMusic == null ? "" : localMusic.getName();
        } else {
            return onlineMusic == null ? "" : onlineMusic.getName();
        }
    }

    public void setName(String name) {
        if (type == TYPE_LOCAL_MUSIC) {
            if (localMusic != null) {
                localMusic.setName(name);
            }
        } else {
            if (onlineMusic != null) {
                onlineMusic.setName(name);
            }
        }
    }

    public String getAuthor() {
        if (type == TYPE_LOCAL_MUSIC) {
            return localMusic == null ? "" : localMusic.getAuthor();
        } else {
            return onlineMusic == null ? "" : onlineMusic.getAuthor();
        }
    }

    public void setAuthor(String author) {
        if (type == TYPE_LOCAL_MUSIC) {
            if (localMusic != null) {
                localMusic.setAuthor(author);
            }
        } else {
            if (onlineMusic != null) {
                onlineMusic.setAuthor(author);
            }
        }
    }

    public String getImage() {
        if (type == TYPE_LOCAL_MUSIC) {
            return localMusic == null ? "" : localMusic.getImage();
        } else {
            return onlineMusic == null ? "" : onlineMusic.getImage();
        }
    }

    public void setImage(String image) {
        if (type == TYPE_LOCAL_MUSIC) {
            if (localMusic != null) {
                localMusic.setImage(image);
            }
        } else {
            if (onlineMusic != null) {
                onlineMusic.setImage(image);
            }
        }
    }

    public String getAlbum() {
        if (type == TYPE_LOCAL_MUSIC) {
            return localMusic == null ? "" : localMusic.getAlbum();
        } else {
            return onlineMusic == null ? "" : onlineMusic.getAlbum();
        }
    }

    public void setAlbum(String album) {
        if (type == TYPE_LOCAL_MUSIC) {
            if (localMusic != null) {
                localMusic.setAlbum(album);
            }
        } else {
            if (onlineMusic != null) {
                onlineMusic.setAlbum(album);
            }
        }
    }

    public String getAddr() {
        if (type == TYPE_LOCAL_MUSIC) {
            return localMusic == null ? "" : localMusic.getAddr();
        } else {
            return onlineMusic == null ? "" : onlineMusic.getAddr();
        }
    }

    public void setAddr(String addr) {
        if (type == TYPE_LOCAL_MUSIC) {
            if (localMusic != null) {
                localMusic.setAddr(addr);
            }
        } else {
            if (onlineMusic != null) {
                onlineMusic.setAddr(addr);
            }
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalMusic getLocalMusic() {
        return localMusic;
    }

    public void setLocalMusic(LocalMusic localMusic) {
        this.localMusic = localMusic;
    }

    public OnlineMusic getOnlineMusic() {
        return onlineMusic;
    }

    public void setOnlineMusic(OnlineMusic onlineMusic) {
        this.onlineMusic = onlineMusic;
    }

    @Override
    public String toString() {
        return "Music{" +
                "type=" + type +
                ", localMusic=" + localMusic +
                ", onlineMusic=" + onlineMusic +
                '}';
    }
}
