package com.yb.magicplayer.activity.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yb.magicplayer.R;
import com.yb.magicplayer.service.MusicPlayService;
import com.yb.magicplayer.utils.LogUtil;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    // 通用顶部操作栏中的一些View
    protected ImageView mIvTopBarBack;
    protected TextView mTvTopBarTitle;
    protected ImageView mIvTopBarRight1;
    protected ImageView mIvTopBarRight2;
    protected TextView mTvTopBarRightText;

    protected ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicPlayBinder = (MusicPlayService.MusicPlayBinder) service;
            bindServiceSuccess();
            LogUtil.i("BindService", "bind service success");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    protected MusicPlayService.MusicPlayBinder mMusicPlayBinder;

    protected void bindMusicPlaySerivce() {
        Intent intent = new Intent(getContext(), MusicPlayService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
        initListener();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    protected void initCommonView() {
        mIvTopBarBack = (ImageView) findViewById(R.id.id_common_title_bar_back);
        mTvTopBarTitle = (TextView) findViewById(R.id.id_common_title_bar_title);
        mIvTopBarRight1 = (ImageView) findViewById(R.id.id_common_title_bar_right1);
        mIvTopBarRight2 = (ImageView) findViewById(R.id.id_common_title_bar_right2);
        mTvTopBarRightText = (TextView) findViewById(R.id.id_common_title_bar_right_text);
    }

    @Override
    public void onClick(View v) {

    }

    protected Context getContext() {
        return this;
    }

    protected void bindServiceSuccess() {
    }
}
