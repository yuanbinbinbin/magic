package yb.com.magicplayer.entity;

/**
 * Created by yb on 2017/3/8.
 */
public class LocalMusic extends Music {
    private String addr;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public LocalMusic() {
        super();
        this.addr = "";
    }

    public LocalMusic(int id, String name, String author, String image, int allTime,
                      int size, String addr, int album_id) {
        super(id, name, author, image, allTime, size, album_id);
        this.addr = addr;
    }
}
