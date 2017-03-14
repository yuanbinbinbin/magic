package yb.com.magicplayer.utils;


import java.util.List;

import yb.com.magicplayer.entity.LocalMusic;

/**
 * 此类定义整个app所用到的全局变量
 *
 * @author Administrator
 */
public class GlobalVariables {

    public static LocalMusic playingLocalMusic = new LocalMusic();
    public static List<LocalMusic> listLocalMusic = null;
    public static int playingLocalMusicPosition = -1;
    public static int playingState = -1;//在线还是本地
    public static boolean isPlaying = false;
    public static int playingMode = ConfigData.PLAYING_MUSIC_MODE_ALL;//循环方式
    public static int localMusicHistoryCount = 0;
    public static int localMusicDownloadCount = 0;
    public static final String FONT_PATH = "font";
    public static final String FONT_NAME = "slim.ttf";

    public static final String KEY_WELCOME_CONTENT = "key_welcome_content";
    public static final String DEFAULT_WELCOME_CONTENT = "MAGIC";
    public static final String KEY_WELCOME_DATA = "key_welcome_data";
    public static final String KEY_PLAYING_MODEL = "key_playing_model";
}
