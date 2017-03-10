package yb.com.magicplayer.activity;


import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

import java.io.File;

import yb.com.magicplayer.R;
import yb.com.magicplayer.service.MusicPlayService;
import yb.com.magicplayer.utils.ActivityUtil;
import yb.com.magicplayer.utils.FileUtil;
import yb.com.magicplayer.utils.GlobalVariables;
import yb.com.magicplayer.view.SvgView;
import yb.com.magicplayer.view.Utils.SvgUtils;
import yb.com.text2svg.entity.ResultData;
import yb.com.text2svg.util.Text2SvgUtils;

public class MainActivity extends BaseActivity {
    private SvgView sv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        sv = (SvgView) findViewById(R.id.svg);
        ResultData data = null;
        try {
            data = Text2SvgUtils.toConvert(FileUtil.getOutDirPrivate(this, GlobalVariables.FONT_PATH).getAbsolutePath() + "/" + GlobalVariables.FONT_NAME, "B");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SvgUtils utils = new SvgUtils(new Paint());
            utils.load(this, data.getCharGlyphList().get(0).getD());
            sv.setSvgPaths(utils.getPathsForViewport(512, 512));
            sv.getPathAnimator().duration(5000).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
