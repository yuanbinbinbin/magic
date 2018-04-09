package com.yb.magicplayer.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.yb.magicplayer.db.greendao.base.DaoSession;
import com.yb.magicplayer.db.greendao.base.MusicDao;
import com.yb.magicplayer.db.greendao.base.LikeMusicDao;

/**
 * Created by yb on 2017/5/2.
 */
@Entity
public class LikeMusic {
    @Id(autoincrement = true)
    private long id;
    
    @ToOne
    private Music music;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 552153063)
    private transient LikeMusicDao myDao;

    @Generated(hash = 1971172339)
    public LikeMusic(long id) {
        this.id = id;
    }

    @Generated(hash = 1538876461)
    public LikeMusic() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Generated(hash = 1953415558)
    private transient boolean music__refreshed;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 413482766)
    public Music getMusic() {
        if (music != null || !music__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MusicDao targetDao = daoSession.getMusicDao();
            targetDao.refresh(music);
            music__refreshed = true;
        }
        return music;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 60076822)
    public Music peakMusic() {
        return music;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 837808635)
    public void setMusic(Music music) {
        synchronized (this) {
            this.music = music;
            music__refreshed = true;
        }
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
    @Generated(hash = 770293107)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLikeMusicDao() : null;
    }
  
}
