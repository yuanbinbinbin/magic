package com.yb.magicplayer.db.greendao.managers;

import com.yb.magicplayer.db.greendao.GreenDaoHelper;
import com.yb.magicplayer.db.greendao.base.AbstractManager;
import com.yb.magicplayer.entity.LikeMusic;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by yb on 2017/5/2.
 */
public class LikeMusicManager extends AbstractManager<LikeMusic, Long> {

    @Override
    protected AbstractDao<LikeMusic, Long> getAbstractDao() {
        return GreenDaoHelper.getDaoSession() == null ? null : GreenDaoHelper.getDaoSession().getLikeMusicDao();
    }
}
