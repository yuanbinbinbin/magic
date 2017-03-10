package yb.com.magicplayer.utils;

import android.provider.MediaStore;




public class ConfigData {
	public static final int PLAYING_MUSIC_ON_ONLINE = 0 ;
	public static final int PLAYING_MUSIC_ON_LOCAL = 1;
	
	public static final int PLAYING_MUSIC_MODE_DANQU = 0;
	public static final int PLAYING_MUSIC_MODE_ALL = 1;
	public static final int PLAYING_MUSIC_MODE_SUIJI = 2;

	public static int MUSIC_SIZE_MIN = 1024*1024;
	
	public static String[] media_music_info = new String[] {
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE,
			MediaStore.Audio.Media.DATA ,MediaStore.Audio.Media.ALBUM_ID};
}
