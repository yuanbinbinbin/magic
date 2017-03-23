package com.yb.magicplayer.utils;


import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import com.yb.magicplayer.entity.Album;
import com.yb.magicplayer.entity.Artist;
import com.yb.magicplayer.entity.LocalMusic;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.entity.MusicFolder;
import com.yb.magicplayer.entity.PlayList;

/**
 * 此类定义整个app所用到的全局变量
 *
 * @author Administrator
 */
public class GlobalVariables {
    public static final int PLAY_STATUS_PLAYING = 1;//播放状态
    public static final int PLAY_STATUS_PAUSE = 2;//暂停状态
    public static final int PLAY_STATUS_BUFFER = 3;//缓冲状态

    public static List<LocalMusic> listLocalMusic;//本地音乐列表
    public static List<Music> listRecentPlayMusic;//最近播放音乐列表
    public static List<Music> listLikedMusic;//喜欢音乐列表
    public static List<Album> listAlbum;//专辑列表
    public static List<Artist> listArtist;//歌手列表
    public static List<MusicFolder> listFolder;//保存音乐文件夹列表
    public static List<PlayList> listPlayList;//播放列表集合
    public static List<Music> playQuene = new ArrayList<Music>();//播放队列
    public static int playingPosition = 0;//当前播放音乐的位置
    public static int playStatus = PLAY_STATUS_PAUSE;//当前是否在播放
    public static int playingMode = ConfigData.PLAYING_MUSIC_MODE_ALL;//循环方式
    public static int themeColor = Color.parseColor("#B24242");//主题颜色
    public static final String KEY_WELCOME_CONTENT = "key_welcome_content";
    public static final String DEFAULT_WELCOME_CONTENT = "MAGIC";
    public static final String KEY_WELCOME_DATA = "key_welcome_data";
    public static final String KEY_PLAYING_MODEL = "key_playing_model";
    public static final String KEY_RECENT_PLAY = "key_recent_play";
    public static final String KEY_LIKED_MUSIC = "key_liked_music";
    public static final String KEY_PLAY_LIST = "key_play_list";
    public static final String KEY_PLAY_QUENE = "key_play_quene";
    public static final String KEY_PLAY_POSITION = "key_play_position";
}
