package com.yb.magicplayer.utils;

import android.provider.MediaStore;


public class ConfigData {
    public static final int PLAYING_MUSIC_ON_ONLINE = 0;
    public static final int PLAYING_MUSIC_ON_LOCAL = 1;

    public static final int PLAYING_MUSIC_MODE_ONLY = 0;
    public static final int PLAYING_MUSIC_MODE_ALL = 1;
    public static final int PLAYING_MUSIC_MODE_RANDOM = 2;

    public static int MUSIC_SIZE_MIN = 1024 * 1024;

    public static String[] media_music_info = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE,
            MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ALBUM};

    public static String IMAGE_CACHE_FILE_NAME = "image_cache";//本地缓存图片的文件夹名
    public static int IMAGE_CACHE_DISK_FILE_SIZE = 52428800;//本地缓存图片的大小 50*1024*1024 = 52428800
    public static int IMAGE_CACHE_MEMORY_SIZE = 2097152;//内存缓存图片的大小 2*1024*1024 = 2097152
}
