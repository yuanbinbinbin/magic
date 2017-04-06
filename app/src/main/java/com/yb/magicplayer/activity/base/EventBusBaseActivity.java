package com.yb.magicplayer.activity.base;

import android.os.Bundle;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeActivity(CloseEvent event) {
        finish();
    }
}
