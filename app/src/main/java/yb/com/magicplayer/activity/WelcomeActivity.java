package yb.com.magicplayer.activity;

import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import yb.com.magicplayer.R;
import yb.com.magicplayer.utils.ActivityUtil;
import yb.com.magicplayer.utils.FileUtil;
import yb.com.magicplayer.utils.GlobalVariables;
import yb.com.magicplayer.utils.MediaUtils;
import yb.com.text2svg.util.Text2SvgUtils;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareThings();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ActivityUtil.startActivity(getContext(), MainActivity.class);
                        finish();
                    }
                });
            }
        }).start();
    }

    private void prepareThings() {
        GlobalVariables.listLocalMusic = MediaUtils.getLocalMusics(this);
        chackFile();
    }

    private void chackFile() {
        //检查font文件是否写入SD卡中
        File file = FileUtil.getOutDirPrivate(getContext(), GlobalVariables.FONT_PATH);
        if (file != null) {
            file = new File(file.getAbsolutePath() + "/" + GlobalVariables.FONT_NAME);
            if (file != null && !file.exists()) {
                try {
                    FileUtil.writeFile(WelcomeActivity.this.getAssets().open("slim.ttf"), file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void initListener() {

    }

}
