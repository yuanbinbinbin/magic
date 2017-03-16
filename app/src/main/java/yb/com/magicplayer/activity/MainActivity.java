package yb.com.magicplayer.activity;


import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import yb.com.magicplayer.R;
import yb.com.magicplayer.service.MusicPlayService;
import yb.com.magicplayer.utils.ActivityUtil;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar mToolBar;

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
