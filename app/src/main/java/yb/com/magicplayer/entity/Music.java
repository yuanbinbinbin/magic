package yb.com.magicplayer.entity;

public class Music {
    private String name;
    private String author;
    private String image;
    private int allTime;
    private int size;
    private int id;
    private int album_id;

    public Music() {
        this.name = "";
        this.author = "";
        this.image = null;
        this.allTime = 0;
        this.size = 0;
        this.id = 0;
        this.album_id = 0;
    }

    public Music(int id, String name, String author, String image, int allTime, int size, int album_id) {
        this.name = name;
        this.author = author;
        this.image = image;
        this.allTime = allTime;
        this.size = size;
        this.id = id;
        this.album_id = album_id;
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

}
