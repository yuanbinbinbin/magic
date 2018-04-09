package com.yb.magicplayer.db.greendao.managers;

import com.yb.magicplayer.db.greendao.GreenDaoHelper;
import com.yb.magicplayer.db.greendao.base.AbstractManager;
import com.yb.magicplayer.entity.LikeMusic;
import com.yb.magicplayer.entity.PlayingQuene;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by yb on 2017/5/2.
 */
public class PlayingQueneManager extends AbstractManager<PlayingQuene, Long> {

    @Override
    protected AbstractDao<PlayingQuene, Long> getAbstractDao() {
        return GreenDaoHelper.getDaoSession() == null ? null : GreenDaoHelper.getDaoSession().getPlayingQueneDao();
    }
}
