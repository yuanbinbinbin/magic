package yb.com.magicplayer.activity;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import yb.com.magicplayer.R;
import yb.com.magicplayer.service.MusicPlayService;
import yb.com.magicplayer.utils.ActivityUtil;


public class MainActivity extends BaseActivity {
    private TextView tv;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tv = (TextView) findViewById(R.id.id_local);
    }

    @Override
    protected void initData() {
        Intent intent = new Intent(this, MusicPlayService.class);
        startService(intent);
    }

    @Override
    protected void initListener() {

    }

    public void startLocalMusic(View view) {
        ActivityUtil.startActivity(this, MusicListActivity.class);
    }

}
