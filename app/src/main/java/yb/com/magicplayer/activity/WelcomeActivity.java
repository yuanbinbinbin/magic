package yb.com.magicplayer.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import yb.com.magicplayer.R;
import yb.com.magicplayer.entity.LocalMusic;
import yb.com.magicplayer.entity.Music;
import yb.com.magicplayer.utils.ActivityUtil;
import yb.com.magicplayer.utils.ConfigData;
import yb.com.magicplayer.utils.DateUtil;
import yb.com.magicplayer.utils.FastJsonUtil;
import yb.com.magicplayer.utils.GlobalVariables;
import yb.com.magicplayer.utils.MediaUtils;
import yb.com.magicplayer.utils.PreferencesUtils;
import yb.com.magicplayer.view.shimmer.Shimmer;
import yb.com.magicplayer.view.shimmer.ShimmerTextView;

public class WelcomeActivity extends BaseActivity implements Animator.AnimatorListener {
    private ShimmerTextView mSTv;
    private Shimmer shimmer;
    private boolean isPrepared;
    private TextView mTvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        mSTv = (ShimmerTextView) findViewById(R.id.id_welcome_content);
        mTvData = (TextView) findViewById(R.id.id_welcome_data);
    }

    @Override
    protected void initData() {
        initViewContent();
        startAnim();
        isPrepared = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareThings();
                isPrepared = true;
            }
        }).start();
    }

    /**
     * 初始化TextView内容
     */
    private void initViewContent() {
        String content = PreferencesUtils.loadPrefString(this, GlobalVariables.KEY_WELCOME_CONTENT, GlobalVariables.DEFAULT_WELCOME_CONTENT);
        mSTv.setText(content);
        String data = PreferencesUtils.loadPrefString(this, GlobalVariables.KEY_WELCOME_DATA, DateUtil.DateFormat1(DateUtil.getNow()));
        mTvData.setText(data);
    }

    private void startAnim() {
        shimmer = new Shimmer();
        shimmer.setAnimatorListener(this);
        shimmer.setRepeatCount(1);
        shimmer.start(mSTv);
    }

    private void prepareThings() {
        MediaUtils.initMusic(WelcomeActivity.this);//更新音乐库
        Log.e("test", "musicCount: " + GlobalVariables.listLocalMusic.size());
        Log.e("test", "listPlayCount: " + GlobalVariables.listPlayList.size());
        Log.e("test", "playQueneCount: " + GlobalVariables.playQuene.size());
        Log.e("test", "recentCount: " + GlobalVariables.listRecentPlayMusic.size());
        Log.e("test", "likedCount: " + GlobalVariables.listLikedMusic.size());
        Log.e("test", "albumCount: " + GlobalVariables.listAlbum.size());
        Log.e("test", "artistCount: " + GlobalVariables.listArtist.size());
        Log.e("test", "folderCount: " + GlobalVariables.listFolder.size());
        chackFile();
    }

    private void chackFile() {
        //检查font文件是否写入SD卡中
//        File file = FileUtil.getOutDirPrivate(getContext(), GlobalVariables.FONT_PATH);
//        if (file != null) {
//            file = new File(file.getAbsolutePath() + "/" + GlobalVariables.FONT_NAME);
//            if (file != null && !file.exists()) {
//                try {
//                    FileUtil.writeFile(WelcomeActivity.this.getAssets().open("slim.ttf"), file.getAbsolutePath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (isPrepared) {
            shimmer.cancel();
            ActivityUtil.startActivity(getContext(), MainActivity.class);
            finish();
        } else {
            startAnim();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
