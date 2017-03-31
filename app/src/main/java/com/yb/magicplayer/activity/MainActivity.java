package com.yb.magicplayer.activity;


import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.List;

import com.yb.magicplayer.R;
import com.yb.magicplayer.activity.base.EventBusBaseActivity;
import com.yb.magicplayer.adapter.MainBottomPlayerAdpater;
import com.yb.magicplayer.adapter.MainPlayListAdapter;
import com.yb.magicplayer.adapter.MainRecentPlayAdapter;
import com.yb.magicplayer.entity.LocalMusic;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.events.PlayingMusicChangeEvent;
import com.yb.magicplayer.events.PlayingStatusChangeEvent;
import com.yb.magicplayer.events.RefreshPlayProgressEvent;
import com.yb.magicplayer.events.RefreshRecentPlayListEvent;
import com.yb.magicplayer.listener.OnItemClickListener;
import com.yb.magicplayer.service.MusicPlayService;
import com.yb.magicplayer.utils.ActivityUtil;
import com.yb.magicplayer.utils.GlobalVariables;
import com.yb.magicplayer.utils.ImageUtil;
import com.yb.magicplayer.utils.LogUtil;
import com.yb.magicplayer.utils.MediaUtils;
import com.yb.magicplayer.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends EventBusBaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar mToolBar;
    private ImageView[] headerViews = new ImageView[10];
    private View mViewRecentlyEmpty;
    private View mViewRecentlyShowAll;
    private View mViewPlayListEmpty;
    private View mViewPlayListShowAll;
    private RecyclerView mRvRecently;
    private RecyclerView mRvPlayList;
    private View mViewLocal;
    private View mIvLocalPlay;
    private View mViewLike;
    private View mViewLikePlay;
    private View mViewLocalFolder;
    private View mViewBottomPlayingContainer;
    private SeekBar mSeekBar;

    private ImageView mIvPlayStatus;
    private ViewPager mViewPagerBottomMusics;
    private MainBottomPlayerAdpater mBottomMusicsAdapter;

    private MainRecentPlayAdapter mainRecentPlayAdapter;
    private MainPlayListAdapter mainPlayListAdapter;
    private static int DELAYED_TIME = 1000;
    private Handler updateMusicProgressHandler = new Handler();
    private Runnable updateMusicProgressRunable = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "updateMusicProgressRunable");
            if (mMusicPlayBinder != null) {
                refreshSeekBar(mMusicPlayBinder.getProgress(), mMusicPlayBinder.getSecondProgress(), mMusicPlayBinder.getMaxProgress());
            }
            updateMusicProgressHandler.postDelayed(updateMusicProgressRunable, DELAYED_TIME);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initToolBar();
        initHeaderViews();
        initBodyView();
        initBottomPlaying();
        //mProgressBarPlaying =  (ProgressBar) findViewById(R.id.id_main_playing_progress);
    }

    private void initBottomPlaying() {
        mViewBottomPlayingContainer = findViewById(R.id.id_main_bottom_playing_container);
        mSeekBar = (SeekBar) findViewById(R.id.id_main_playing_seek_bar);
        mViewPagerBottomMusics = (ViewPager) findViewById(R.id.id_main_bottom_musics);
        mIvPlayStatus = (ImageView) findViewById(R.id.id_main_playing_controller);
    }

    private void initBodyView() {
        mViewRecentlyEmpty = findViewById(R.id.id_main_recent_nothing);
        mViewRecentlyShowAll = findViewById(R.id.id_main_recent_all);
        mViewPlayListEmpty = findViewById(R.id.id_main_play_list_nothing);
        mViewPlayListShowAll = findViewById(R.id.id_main_play_list_all);
        mRvRecently = (RecyclerView) findViewById(R.id.id_main_recent_list);
        mRvPlayList = (RecyclerView) findViewById(R.id.id_main_play_list_list);
        mViewLocal = findViewById(R.id.id_main_local_banner);
        mIvLocalPlay = findViewById(R.id.id_main_local_banner_play);
        mViewLike = findViewById(R.id.id_main_favorite_banner);
        mViewLikePlay = findViewById(R.id.id_main_local_banner_play);
        mViewLocalFolder = findViewById(R.id.id_main_folder_banner);
    }

    private void initToolBar() {
        navigationView = (NavigationView) findViewById(R.id.id_main_nav);
        mToolBar = (Toolbar) findViewById(R.id.id_main_toolbar);
        mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        //使toolbar有左上角的按钮 start
        drawerLayout = (DrawerLayout) findViewById(R.id.id_main_drawerlayout);
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        //使toolbar有左上角的按钮 end
    }

    public void initHeaderViews() {
        headerViews[0] = (ImageView) findViewById(R.id.home_header_img_1);
        headerViews[1] = (ImageView) findViewById(R.id.home_header_img_2);
        headerViews[2] = (ImageView) findViewById(R.id.home_header_img_3);
        headerViews[3] = (ImageView) findViewById(R.id.home_header_img_4);
        headerViews[4] = (ImageView) findViewById(R.id.home_header_img_5);
        headerViews[5] = (ImageView) findViewById(R.id.home_header_img_6);
        headerViews[6] = (ImageView) findViewById(R.id.home_header_img_7);
        headerViews[7] = (ImageView) findViewById(R.id.home_header_img_8);
        headerViews[8] = (ImageView) findViewById(R.id.home_header_img_9);
        headerViews[9] = (ImageView) findViewById(R.id.home_header_img_10);
        refreshHeaderViews();
    }

    /**
     * 刷新header图片
     */
    public void refreshHeaderViews() {
        List<LocalMusic> localMusicList = GlobalVariables.listLocalMusic;
        if (localMusicList == null || localMusicList.size() <= 0) {
            for (int i = 0; i < 10; i++) {
                headerViews[i].setImageResource(R.drawable.ic_default);
            }
            return;
        }
        int size = localMusicList.size();
        for (int i = 0; i < 10; i++) {
            if (i >= size) {
                headerViews[i].setImageResource(R.drawable.ic_default);
            } else {
                LocalMusic localMusic = localMusicList.get(i);
                if (localMusic != null) {
                    ImageUtil.loadImage(this, localMusic.getImage(), headerViews[i]);
                } else {
                    headerViews[i].setImageResource(R.drawable.ic_default);
                }
            }
        }

    }

    @Override
    protected void initData() {
//        startService(intent);
        bindMusicPlaySerivce();
        initBottomMusicInfo();
    }

    private void initBottomMusicInfo() {
        mBottomMusicsAdapter = new MainBottomPlayerAdpater(GlobalVariables.playQuene, getContext());
        mBottomMusicsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (object != null && object instanceof Music) {
                    //LogUtil.i(TAG, "onItemClick position: " + position + "info: " + object);
                    ActivityUtil.startActivity(getContext(), PlayDetailActivity.class);
                }
            }
        });
        mViewPagerBottomMusics.setAdapter(mBottomMusicsAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshRecentMusic();
        refreshPlayList();
        refreshBottom();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateMusicProgressHandler.removeCallbacks(updateMusicProgressRunable);
    }

    /**
     * 更新底部播放
     */
    private void refreshBottom() {
        Music music;
        int progress = 0;
        int secondProgress = 0;
        int playStatus;
        if (mMusicPlayBinder == null) {
            if (GlobalVariables.playQuene == null) {
                mViewBottomPlayingContainer.setVisibility(View.GONE);
                return;
            }
            music = GlobalVariables.playQuene.get(GlobalVariables.playingPosition);
            playStatus = GlobalVariables.playStatus;
        } else {
            music = mMusicPlayBinder.getPlayingMusic();
            progress = mMusicPlayBinder.getProgress();
            secondProgress = mMusicPlayBinder.getSecondProgress();
            playStatus = mMusicPlayBinder.getPlayStatus();
        }
        refreshMusicInfo(music);
        refreshSeekBar(progress, secondProgress, music.getAllTime());
        refreshPlayStatus(playStatus);
    }

    private void refreshMusicInfo(Music music) {
        if (music == null) {
            mViewBottomPlayingContainer.setVisibility(View.GONE);
            return;
        }
        mViewPagerBottomMusics.setCurrentItem(MediaUtils.getPositionInMusicList(GlobalVariables.playQuene, music.getId()));
    }

    private void refreshPlayStatus(int status) {
        if (GlobalVariables.PLAY_STATUS_PLAYING == status) {
            mIvPlayStatus.setImageResource(R.drawable.ic_main_pause_white);
            updateMusicProgressHandler.postDelayed(updateMusicProgressRunable, DELAYED_TIME);
        } else if (GlobalVariables.PLAY_STATUS_PAUSE == status) {
            mIvPlayStatus.setImageResource(R.drawable.ic_main_play_arrow_white);
            updateMusicProgressHandler.removeCallbacks(updateMusicProgressRunable);
        } else if (GlobalVariables.PLAY_STATUS_BUFFER == status) {
            updateMusicProgressHandler.removeCallbacks(updateMusicProgressRunable);
        }
    }

    private void refreshSeekBar(int progress, int secondProgress, int maxProgress) {
        LogUtil.i(TAG, "set progress: " + progress + "secondProgress " + secondProgress);
        mSeekBar.setMax(maxProgress);
        mSeekBar.setProgress(progress);
        mSeekBar.setSecondaryProgress(secondProgress);
    }

    /**
     * 刷新最近播放列表
     */
    private void refreshRecentMusic() {
        if (GlobalVariables.listRecentPlayMusic == null || GlobalVariables.listRecentPlayMusic.size() <= 0) {
            mViewRecentlyEmpty.setVisibility(View.VISIBLE);
            mRvRecently.setVisibility(View.INVISIBLE);
        } else {
            mViewRecentlyEmpty.setVisibility(View.INVISIBLE);
            mRvRecently.setVisibility(View.VISIBLE);
            if (mainRecentPlayAdapter == null) {
                mainRecentPlayAdapter = new MainRecentPlayAdapter(getContext(), null);
                mainRecentPlayAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, Object object) {
                        if (object != null && object instanceof Music) {
                            playMusicByMusicId(((Music) object).getId());
                        }
                    }
                });
                mRvRecently.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                mRvRecently.setAdapter(mainRecentPlayAdapter);
            }
            mainRecentPlayAdapter.setData(GlobalVariables.listRecentPlayMusic);
        }
    }

    /**
     * 刷新播放列表
     */
    private void refreshPlayList() {
        if (GlobalVariables.listPlayList == null || GlobalVariables.listPlayList.size() <= 0) {
            mViewPlayListEmpty.setVisibility(View.VISIBLE);
            mRvPlayList.setVisibility(View.INVISIBLE);
        } else {
            mViewPlayListEmpty.setVisibility(View.INVISIBLE);
            mRvPlayList.setVisibility(View.VISIBLE);
            if (mainPlayListAdapter == null) {
                mainPlayListAdapter = new MainPlayListAdapter(getContext(), null);
                mRvPlayList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                mRvPlayList.setAdapter(mainPlayListAdapter);
            }
            mainPlayListAdapter.setData(GlobalVariables.listPlayList);
        }
    }

    @Override
    protected void initListener() {
        navigationView.setNavigationItemSelectedListener(this);
        mViewRecentlyShowAll.setOnClickListener(this);
        mViewPlayListShowAll.setOnClickListener(this);
        mViewLocal.setOnClickListener(this);
        mViewLike.setOnClickListener(this);
        mViewLikePlay.setOnClickListener(this);
        mViewLocalFolder.setOnClickListener(this);
        mIvPlayStatus.setOnClickListener(this);
        mViewPagerBottomMusics.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int playingPosition = -1;
            private boolean isSelfChanged = false;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (playingPosition != position && isSelfChanged && GlobalVariables.playQuene != null && GlobalVariables.playQuene.size() > position) {
                    playingPosition = position;
                    playMusicByMusicId(GlobalVariables.playQuene.get(position).getId());
                }
                isSelfChanged = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                0：什么都没做 1：开始滑动  2：滑动结束
                if (state == 1)
                    isSelfChanged = true;
                else if (state == 0)
                    isSelfChanged = false;
            }
        });
    }

    private void playMusicByMusicId(int id) {
        Intent intent = new Intent(getContext(), MusicPlayService.class);
        intent.putExtra("playing", "playingbyid");
        intent.putExtra("id", id);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    /**
     * 当bind成功时的回调
     */
    @Override
    protected void bindServiceSuccess() {
        refreshBottom();
    }

    public void startLocalMusic() {
        ActivityUtil.startActivity(this, MusicListActivity.class);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.id_main_recent_all://查看全部最近播放
                break;
            case R.id.id_main_play_list_all://查看全部播放列表
                break;
            case R.id.id_main_local_banner://查看全部本地音乐
                startLocalMusic();
                break;
            case R.id.id_main_local_banner_play://播放本地音乐
                break;
            case R.id.id_main_favorite_banner://查看自己喜欢的音乐
                break;
            case R.id.id_main_favorite_banner_play://播放喜欢的音乐
                break;
            case R.id.id_main_folder_banner://本地文件夹
                break;
            case R.id.id_main_playing_controller://更改播放状态
                changePlayStatus();
                break;
        }
    }

    private void changePlayStatus() {
        int status;
        if (mMusicPlayBinder != null) {
            status = mMusicPlayBinder.getPlayStatus();
        } else {
            status = GlobalVariables.playStatus;
        }
        if (status == GlobalVariables.PLAY_STATUS_BUFFER) {
            ToastUtil.showShortTime(getContext(), "缓冲中...");
        } else if (status == GlobalVariables.PLAY_STATUS_PAUSE) {
            Intent intent = new Intent(getContext(), MusicPlayService.class);
            intent.putExtra("playing", "play");
            startService(intent);
        } else if (status == GlobalVariables.PLAY_STATUS_PLAYING) {
            Intent intent = new Intent(getContext(), MusicPlayService.class);
            intent.putExtra("playing", "pause");
            startService(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                break;
            case R.id.nav_local:
                break;
            case R.id.nav_playlists:
                break;
            case R.id.nav_recent:
                break;
            case R.id.nav_fav:
                break;
            case R.id.nav_folder:
                break;
            case R.id.nav_settings:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    /**
     * 播放音乐改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayingMusicChanged(PlayingMusicChangeEvent event) {
        LogUtil.i(TAG, "onPlayingMusicChanged");
        refreshMusicInfo(event.getPlayingMusic());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPlayingStatusChanged(PlayingStatusChangeEvent event) {
        LogUtil.i(TAG, "onPlayingStatusChanged");
        refreshPlayStatus(event.getStatus());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshPlayProgress(RefreshPlayProgressEvent event) {
        LogUtil.i(TAG, "onRefreshPlayProgress");
        refreshSeekBar(event.getProgress(), event.getSecondProgress(), event.getMaxProgress());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshRecentPlayList(RefreshRecentPlayListEvent event) {
        LogUtil.i(TAG, "onRefreshRecentPlayList");
        refreshRecentMusic();
    }
}
