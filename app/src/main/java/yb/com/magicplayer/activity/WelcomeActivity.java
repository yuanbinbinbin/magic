package yb.com.magicplayer.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import yb.com.magicplayer.R;
import yb.com.magicplayer.utils.ActivityUtil;
import yb.com.magicplayer.utils.ConfigData;
import yb.com.magicplayer.utils.DateUtil;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
        GlobalVariables.playingMode = PreferencesUtils.loadPrefInt(this, GlobalVariables.KEY_PLAYING_MODEL, ConfigData.PLAYING_MUSIC_MODE_ALL);
        GlobalVariables.listLocalMusic = MediaUtils.getLocalMusics(this);
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
