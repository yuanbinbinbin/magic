package com.yb.magicplayer.activity.base;

import com.yb.magicplayer.events.CloseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 注册EventBusActivity 的基类
 * Created by yb on 2017/3/23.
 */
public abstract class EventBusBaseActivity extends BaseActivity {
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeActivity(CloseEvent event) {
        finish();
    }
}
