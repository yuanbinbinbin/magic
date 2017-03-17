package com.yb.magicplayer.activity;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import com.yb.magicplayer.R;
import com.yb.magicplayer.entity.LocalMusic;
import com.yb.magicplayer.service.MusicPlayService;
import com.yb.magicplayer.utils.GlobalVariables;
import com.yb.magicplayer.utils.ImageUtil;
import com.yb.magicplayer.utils.MediaUtils;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar mToolBar;
    ImageView[] headerViews = new ImageView[10];

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
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
        initHeaderViews();
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
                    ImageUtil.loadImage(this, MediaUtils.getMusicImgUrl(localMusic.getId(), localMusic.getAlbum_id()), headerViews[i]);
                } else {
                    headerViews[i].setImageResource(R.drawable.ic_default);
                }
            }
        }

    }

    @Override
    protected void initData() {
        Intent intent = new Intent(this, MusicPlayService.class);
        startService(intent);
    }

    @Override
    protected void initListener() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void startLocalMusic(View view) {
        // ActivityUtil.startActivity(this, MusicListActivity.class);
        drawerLayout.openDrawer(GravityCompat.START);
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
}
