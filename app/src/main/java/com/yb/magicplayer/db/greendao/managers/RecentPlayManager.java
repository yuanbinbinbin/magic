package com.yb.magicplayer.db.greendao.managers;

import com.yb.magicplayer.db.greendao.GreenDaoHelper;
import com.yb.magicplayer.db.greendao.base.AbstractManager;
import com.yb.magicplayer.entity.LikeMusic;
import com.yb.magicplayer.entity.RecentPlay;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by yb on 2017/5/2.
 */
public class RecentPlayManager extends AbstractManager<RecentPlay, Long> {

    @Override
    protected AbstractDao<RecentPlay, Long> getAbstractDao() {
        return GreenDaoHelper.getDaoSession() == null ? null : GreenDaoHelper.getDaoSession().getRecentPlayDao();
    }
}
