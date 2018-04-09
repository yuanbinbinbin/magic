package com.yb.magicplayer.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.yb.magicplayer.db.greendao.base.DaoSession;
import com.yb.magicplayer.db.greendao.base.MusicDao;
import com.yb.magicplayer.db.greendao.base.PlayingQueneDao;

/**
 * 播放列表
 * Created by yb on 2017/5/3.
 */
@Entity
public class PlayingQuene {
    @Id(autoincrement = true)
    private long id;
    @ToMany(referencedJoinProperty = "id")
    private List<Music> playQuene;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 242865648)
    private transient PlayingQueneDao myDao;
    @Generated(hash = 813557269)
    public PlayingQuene(long id) {
        this.id = id;
    }
    @Generated(hash = 838086570)
    public PlayingQuene() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1718357884)
    public List<Music> getPlayQuene() {
        if (playQuene == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MusicDao targetDao = daoSession.getMusicDao();
            List<Music> playQueneNew = targetDao._queryPlayingQuene_PlayQuene(id);
            synchronized (this) {
                if (playQuene == null) {
                    playQuene = playQueneNew;
                }
            }
        }
        return playQuene;
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 828795683)
    public synchronized void resetPlayQuene() {
        playQuene = null;
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1003800931)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlayingQueneDao() : null;
    }

    public void setPlayQuene(List<Music> playQuene) {
        this.playQuene = playQuene;
    }
}
