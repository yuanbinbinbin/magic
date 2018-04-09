package com.yb.magicplayer.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.base.baselibrary.utils.PreferencesUtils;
import com.yb.magicplayer.db.greendao.base.RecentPlayDao;
import com.yb.magicplayer.db.greendao.managers.LikeMusicManager;
import com.yb.magicplayer.db.greendao.managers.MusicManager;
import com.yb.magicplayer.db.greendao.managers.PlayListManager;
import com.yb.magicplayer.db.greendao.managers.PlayingQueneManager;
import com.yb.magicplayer.db.greendao.managers.RecentPlayManager;
import com.yb.magicplayer.entity.Album;
import com.yb.magicplayer.entity.Artist;
import com.yb.magicplayer.entity.LikeMusic;
import com.yb.magicplayer.entity.Music;
import com.yb.magicplayer.entity.MusicFolder;
import com.yb.magicplayer.entity.PlayList;
import com.yb.magicplayer.entity.PlayingQuene;
import com.yb.magicplayer.entity.RecentPlay;

public class MediaUtils {

    public static List<Music> getLocalMusics(Context mContext) {
        List<Music> mList = new ArrayList<Music>();
        Cursor mCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                ConfigData.media_music_info, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (mCursor == null)
            return null;
        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount(); i++) {
            if (mCursor.getInt(4) > ConfigData.MUSIC_SIZE_MIN) {
                Music mLocalMusic = new Music(mCursor.getInt(0),
                        mCursor.getString(1), mCursor.getString(2),
                        mCursor.getInt(3), mCursor.getInt(4),
                        mCursor.getString(5), mCursor.getInt(6),
                        mCursor.getString(7), getMusicImgUrl(mCursor.getInt(0), mCursor.getInt(6)), false, "", 0, false);
                mList.add(mLocalMusic);
            }
            mCursor.moveToNext();
        }
        mCursor.close();
        return PaiXuByZMu(mList);
    }

    public static String getMusicImgUrl(long songid, long albumid) {
        if (albumid < 0 && songid < 0) {
            return "";
        }
        if (albumid < 0) {
            return "content://media/external/audio/media/" + songid + "/albumart";
        } else {
            Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
            return uri.toString();
        }
    }

    public static List<Music> PaiXuByZMu(List<Music> list) {
        int num[] = new int[27];
        for (int i = 0; i < 27; i++)
            num[i] = 0;
        char[] shouZiMu = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            shouZiMu[i] = HanZiToPinYin.getFirstChar(list.get(i).getName());
            switch (shouZiMu[i]) {
                case 'A':
                    num[0]++;
                    break;
                case 'B':
                    num[1]++;

                    break;
                case 'C':
                    num[2]++;

                    break;
                case 'D':
                    num[3]++;

                    break;
                case 'E':
                    num[4]++;

                    break;
                case 'F':
                    num[5]++;

                    break;
                case 'G':
                    num[6]++;

                    break;
                case 'H':
                    num[7]++;

                    break;
                case 'I':
                    num[8]++;

                    break;
                case 'J':
                    num[9]++;

                    break;
                case 'K':
                    num[10]++;

                    break;
                case 'L':
                    num[11]++;

                    break;
                case 'M':
                    num[12]++;

                    break;
                case 'N':
                    num[13]++;

                    break;
                case 'O':
                    num[14]++;

                    break;
                case 'P':
                    num[15]++;

                    break;
                case 'Q':
                    num[16]++;

                    break;
                case 'R':
                    num[17]++;

                    break;
                case 'S':
                    num[18]++;

                    break;
                case 'T':
                    num[19]++;

                    break;
                case 'U':
                    num[20]++;

                    break;
                case 'V':
                    num[21]++;

                    break;
                case 'W':
                    num[22]++;

                    break;
                case 'X':
                    num[23]++;

                    break;
                case 'Y':
                    num[24]++;

                    break;
                case 'Z':
                    num[25]++;

                    break;
                case '#':
                    num[26]++;

                    break;
                default:
                    break;
            }
        }
        int flag = 0;
        for (int i = 0; i < 27; i++) {
            if (num[i] > 0 && flag == 0) {
                num[i]--;
                flag = 1;
            }
            if (i > 0) {
                num[i] += num[i - 1];
            }
        }
        List<Music> musics = new ArrayList<Music>();
        for (int i = 0; i < list.size(); i++)
            musics.add(null);
        for (int i = 0; i < list.size(); i++) {
            switch (shouZiMu[i]) {
                case 'A':
                    if (num[0] >= 0) {
                        musics.set(num[0], list.get(i));
                        num[0]--;
                    }
                    break;
                case 'B':
                    if (num[1] >= 0) {
                        musics.set(num[1], list.get(i));
                        num[1]--;
                    }

                    break;
                case 'C':
                    if (num[2] >= 0) {
                        musics.set(num[2], list.get(i));
                        num[2]--;
                    }

                    break;
                case 'D':
                    if (num[3] >= 0) {
                        musics.set(num[3], list.get(i));
                        num[3]--;
                    }

                    break;
                case 'E':
                    if (num[4] >= 0) {
                        musics.set(num[4], list.get(i));
                        num[4]--;
                    }

                    break;
                case 'F':
                    if (num[5] >= 0) {
                        musics.set(num[5], list.get(i));
                        num[5]--;
                    }

                    break;
                case 'G':
                    if (num[6] >= 0) {
                        musics.set(num[6], list.get(i));
                        num[6]--;
                    }

                    break;
                case 'H':
                    if (num[7] >= 0) {
                        musics.set(num[7], list.get(i));
                        num[7]--;
                    }

                    break;
                case 'I':
                    if (num[8] >= 0) {
                        musics.set(num[8], list.get(i));
                        num[8]--;
                    }

                    break;
                case 'J':
                    if (num[9] >= 0) {
                        musics.set(num[9], list.get(i));
                        num[9]--;
                    }

                    break;
                case 'K':
                    if (num[10] >= 0) {
                        musics.set(num[10], list.get(i));
                        num[10]--;
                    }

                    break;
                case 'L':
                    if (num[11] >= 0) {
                        musics.set(num[11], list.get(i));
                        num[11]--;
                    }

                    break;
                case 'M':
                    if (num[12] >= 0) {
                        musics.set(num[12], list.get(i));
                        num[12]--;
                    }

                    break;
                case 'N':
                    if (num[13] >= 0) {
                        musics.set(num[13], list.get(i));
                        num[13]--;
                    }

                    break;
                case 'O':
                    if (num[14] >= 0) {
                        musics.set(num[14], list.get(i));
                        num[14]--;
                    }

                    break;
                case 'P':
                    if (num[15] >= 0) {
                        musics.set(num[15], list.get(i));
                        num[15]--;
                    }

                    break;
                case 'Q':
                    if (num[16] >= 0) {
                        musics.set(num[16], list.get(i));
                        num[16]--;
                    }

                    break;
                case 'R':
                    if (num[17] >= 0) {
                        musics.set(num[17], list.get(i));
                        num[17]--;
                    }

                    break;
                case 'S':
                    if (num[18] >= 0) {
                        musics.set(num[18], list.get(i));
                        num[18]--;
                    }

                    break;
                case 'T':
                    if (num[19] >= 0) {
                        musics.set(num[19], list.get(i));
                        num[19]--;
                    }

                    break;
                case 'U':
                    if (num[20] >= 0) {
                        musics.set(num[20], list.get(i));
                        num[20]--;
                    }

                    break;
                case 'V':
                    if (num[21] >= 0) {
                        musics.set(num[21], list.get(i));
                        num[21]--;
                    }

                    break;
                case 'W':
                    if (num[22] >= 0) {
                        musics.set(num[22], list.get(i));
                        num[22]--;
                    }

                    break;
                case 'X':
                    if (num[23] >= 0) {
                        musics.set(num[23], list.get(i));
                        num[23]--;
                    }

                    break;
                case 'Y':
                    if (num[24] >= 0) {
                        musics.set(num[24], list.get(i));
                        num[24]--;
                    }

                    break;
                case 'Z':
                    if (num[25] >= 0) {
                        musics.set(num[25], list.get(i));
                        num[25]--;
                    }

                    break;
                case '#':
                    if (num[26] >= 0) {
                        musics.set(num[26], list.get(i));
                        num[26]--;
                    }

                    break;
                default:
                    break;
            }
        }

        return musics;
    }

    public static int getPositionInLocalMusicList(List<Music> list, int id) {
        int position;
        if (list == null || list.size() <= 0) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getId() == id)
                return i;
        return -1;
    }

    public static int getPositionInMusicList(List<Music> list, long id) {
        int position;
        if (list == null || list.size() <= 0) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getId() == id)
                return i;
        return -1;
    }

    public static Bitmap getArtwork(Context context, long song_id, long album_id,
                                    boolean allowdefault) {
        if (album_id < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (song_id >= 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1);
                if (bm != null) {
                    return bm;
                }
            }
            if (allowdefault) {
                return getDefaultArtwork(context);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                Bitmap temp;
                temp = BitmapFactory.decodeStream(in, null, sBitmapOptions);
                if (temp == null) {
                    temp = getArtworkFromFile(context, song_id, album_id);
                    if (temp != null) {
                        if (temp.getConfig() == null) {
                            temp = temp.copy(Bitmap.Config.RGB_565, false);
                            if (temp == null && allowdefault) {
                                return getDefaultArtwork(context);
                            }
                        }
                    } else if (allowdefault) {
                        temp = getDefaultArtwork(context);
                    }
                    return temp;
                } else
                    return temp;
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null && allowdefault) {
                            return getDefaultArtwork(context);
                        }
                    }
                } else if (allowdefault) {
                    bm = getDefaultArtwork(context);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
        if (allowdefault) {
            return getDefaultArtwork(context);
        }
        return null;
    }

    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
        Bitmap bm = null;
        byte[] art = null;
        String path = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);

                }
            }
        } catch (FileNotFoundException ex) {

        }
        if (bm != null) {
            mCachedBit = bm;
        }
        return bm;
    }

    private static Bitmap getDefaultArtwork(Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
//        return BitmapFactory.decodeStream(
//                context.getResources().openRawResource(R.drawable.music_image_default), null, opts);
        return null;
    }

    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private static Bitmap mCachedBit = null;


    public static void initMusic(Context context) {
        long time = System.currentTimeMillis();
        refreshLocalMusic(context);
        long time2 = System.currentTimeMillis();
        Log.e("test", "time:" + (time2 - time));
        time = time2;
        initSavedData(context);
        time2 = System.currentTimeMillis();
        Log.e("test", "time:" + (time2 - time));
        time = time2;
        initOtherData();
        time2 = System.currentTimeMillis();
        Log.e("test", "time:" + (time2 - time));
    }

    /**
     * 更新本地音乐库
     */
    //更新本地音乐库
    public static void refreshLocalMusic(Context context) {
        GlobalVariables.listLocalMusic = getLocalMusics(context);
        if (GlobalVariables.listLocalMusic == null) {
            GlobalVariables.listLocalMusic = new ArrayList<Music>();
        }
    }

    /**
     * 初始话保存在本地的信息
     *
     * @param context
     */
    //初始话保存在本地的信息
    public static void initSavedData(Context context) {
        MusicManager musicManager = new MusicManager();
        List<Music> savedMusic = musicManager.loadAll();
        if (savedMusic == null) {
            savedMusic = new ArrayList<Music>();
        }
        //向自己的数据库中添加本地新加的音乐
        for (Music music : GlobalVariables.listLocalMusic) {
            if (music != null) {
                boolean isExist = false;
                for (Music music2 : savedMusic) {
                    if (music2 != null) {
                        if (music2.getId() == music.getId()) {
                            musicCopy(music, music2);
                            musicManager.update(music2);
                        }
                        isExist = true;
                    }
                }
                if (!isExist) {
                    musicManager.insert(music);
                }
            }
        }
        //向自己的数据库中去除掉已经删除的音乐
        for (Music music : savedMusic) {
            if (music != null) {
                boolean isExist = false;
                for (Music music2 : GlobalVariables.listLocalMusic) {
                    if (music2 != null) {
                        if (music2.getId() == music.getId()) {
                            isExist = true;
                            break;
                        }
                    }
                }
                if (!isExist) {
                    musicManager.delete(music);
                }
            }
        }

        //获取最近播放列表
        RecentPlayManager recentPlayManager = new RecentPlayManager();
        List<RecentPlay> recentPlays = recentPlayManager.getQueryBuilder().orderDesc(RecentPlayDao.Properties.PlayTime).list();
        if (GlobalVariables.listRecentPlayMusic == null) {
            GlobalVariables.listRecentPlayMusic = new ArrayList<Music>();
        }
        if (recentPlays != null) {
            for (RecentPlay recentPlay : recentPlays) {
                if (recentPlay != null && recentPlay.getMusic() != null) {
                    GlobalVariables.listRecentPlayMusic.add(recentPlay.getMusic());
                }
            }
        }

        //获取喜欢列表
        LikeMusicManager likeMusicManager = new LikeMusicManager();
        List<LikeMusic> likeMusics = likeMusicManager.loadAll();
        if (GlobalVariables.listLikedMusic == null) {
            GlobalVariables.listLikedMusic = new ArrayList<Music>();
        }
        if (likeMusics != null) {
            for (LikeMusic likeMusic : likeMusics) {
                if (likeMusic != null && likeMusic.getMusic() != null) {
                    GlobalVariables.listLikedMusic.add(likeMusic.getMusic());
                }
            }
        }

        //获取播放列表
        PlayListManager playListManager = new PlayListManager();
        GlobalVariables.listPlayList = playListManager.loadAll();
        if (GlobalVariables.listPlayList == null) {
            GlobalVariables.listPlayList = new ArrayList<PlayList>();
        }
        //获取正在播放列表
        PlayingQueneManager playingQueneManager = new PlayingQueneManager();
        PlayingQuene playingQuene = playingQueneManager.selectByPrimaryKey((long) 1);
        if (playingQuene != null && playingQuene.getPlayQuene() != null) {
            GlobalVariables.playQuene = playingQuene.getPlayQuene();
        }

        //如果正在播放列表为空，则默认为播放本地列表
        if (GlobalVariables.playQuene == null || GlobalVariables.playQuene.size() <= 0) {
            GlobalVariables.playQuene = new ArrayList<Music>();
            for (Music localMusic : GlobalVariables.listLocalMusic) {
                if (localMusic != null) {
                    GlobalVariables.playQuene.add(localMusic);
                }
            }
            savePlayQuene(GlobalVariables.playQuene);
        }

        //获取正在播放位置
        GlobalVariables.playingPosition = PreferencesUtils.loadPrefInt(context, GlobalVariables.KEY_PLAY_POSITION, 0);
        //获取正在播放音乐的id
        GlobalVariables.playingMusicId = PreferencesUtils.loadPrefInt(context, GlobalVariables.KEY_PLAY_ID, -1);

        //获取播放模式、顺序播放、单曲循环、随机播放
        GlobalVariables.playingMode = PreferencesUtils.loadPrefInt(context, GlobalVariables.KEY_PLAYING_MODEL, ConfigData.PLAYING_MUSIC_MODE_SEQUENCE);
    }

    /**
     * 刷新最近播放列表
     */
    //刷新最近播放列表
    public static void addMusic2RecentPlayList(Music music) {
        if (music == null) {
            return;
        }
        RecentPlayManager recentPlayManager = new RecentPlayManager();
        int position = getPositionInMusicList(GlobalVariables.listRecentPlayMusic, music.getId());
        if (position >= 0) {
            GlobalVariables.listRecentPlayMusic.remove(position);
        }
        GlobalVariables.listRecentPlayMusic.add(0, music);
        RecentPlay recentPlay = recentPlayManager.selectByPrimaryKey(music.getId());
        if (recentPlay == null) {
            recentPlay = new RecentPlay();
            recentPlay.setMusic(music);
            recentPlay.setMusicId(music.getId());
        }
        recentPlay.setPlayTime(new Date());
        recentPlayManager.insertOrReplace(recentPlay);
    }

    /**
     * 保存喜欢列表
     *
     * @param context
     * @param list
     */
    //保存喜欢列表
    public static void addMusic2Like(Music music) {
        if (music == null) {
            return;
        }
        if (GlobalVariables.listLikedMusic == null) {
            GlobalVariables.listLikedMusic = new ArrayList<Music>();
        }
        if (getPositionInMusicList(GlobalVariables.listLikedMusic, music.getId()) > 0) {
            return;
        }
        GlobalVariables.listLikedMusic.add(music);
        LikeMusicManager likeMusicManager = new LikeMusicManager();
        LikeMusic likeMusic = new LikeMusic();
        likeMusic.setMusic(music);
        likeMusicManager.insertOrReplace(likeMusic);
    }


    /**
     * 保存正在播放队列
     *
     * @param list
     */
    //保存正在播放队列
    public static void savePlayQuene(List<Music> list) {
        PlayingQueneManager playingQueneManager = new PlayingQueneManager();
        PlayingQuene playingQuene = playingQueneManager.selectByPrimaryKey(1L);
        if (playingQuene == null) {
            playingQuene = new PlayingQuene();
        }
        playingQuene.setPlayQuene(list);
        playingQueneManager.insertOrReplace(playingQuene);
    }

    /**
     * 向播放队列中添加音乐
     */
    //向播放队列中添加音乐
    public static void addMusic2PlayQuene(Music music) {
        if (music == null) {
            return;
        }
        if (GlobalVariables.playQuene == null) {
            GlobalVariables.playQuene = new ArrayList<Music>();
        }
        if (GlobalVariables.playQuene.size() <= 0) {
            if (GlobalVariables.listLocalMusic != null && GlobalVariables.listLocalMusic.size() > 0) {
                for (Music localMusic : GlobalVariables.listLocalMusic) {
                    GlobalVariables.playQuene.add(localMusic);
                }
            }
        }
        GlobalVariables.playQuene.add(music);
        savePlayQuene(GlobalVariables.playQuene);
    }

    /**
     * 保存正在播放的位置
     *
     * @param context
     * @param position
     */
    //保存正在播放的位置
    public static void savePlayingPosition(Context context, int position) {
        GlobalVariables.playingPosition = position;
        PreferencesUtils.savePrefInt(context, GlobalVariables.KEY_PLAY_POSITION, position);
    }

    /**
     * 保存正在播放的模式
     *
     * @param context
     * @param model
     */
    //保存正在播放的模式
    public static void savePlayModel(Context context, int model) {
        PreferencesUtils.savePrefInt(context, GlobalVariables.KEY_PLAYING_MODEL, model);
    }

    public static void initOtherData() {
        if (GlobalVariables.listAlbum == null) {
            GlobalVariables.listAlbum = new ArrayList<Album>();
        }
        if (GlobalVariables.listArtist == null) {
            GlobalVariables.listArtist = new ArrayList<Artist>();
        }
        if (GlobalVariables.listFolder == null) {
            GlobalVariables.listFolder = new ArrayList<MusicFolder>();
        }
        for (Music localMusic : GlobalVariables.listLocalMusic) {
            if (localMusic != null) {
                Music music = localMusic;
                boolean isAdded = false;
                //将music添加到相应的专辑中
                for (Album album : GlobalVariables.listAlbum) {
                    if (album != null && album.getName() != null && album.getName().equals(localMusic.getAlbum())) {
                        if (isExist(album.getMusicList(), music) < 0) {
                            album.getMusicList().add(music);
                        }
                        isAdded = true;
                    }
                }
                if (!isAdded && !TextUtils.isEmpty(localMusic.getAlbum())) {
                    Album album = new Album();
                    album.setAlbumid(localMusic.getAlbum_id());
                    album.setName(localMusic.getAlbum());
                    album.getMusicList().add(music);
                    GlobalVariables.listAlbum.add(album);
                }
                //将music添加到相应的歌手中
                isAdded = false;
                for (Artist artist : GlobalVariables.listArtist) {
                    if (artist != null) {
                        if (artist.getName().equals(localMusic.getAuthor())) {
                            if (isExist(artist.getMusicList(), music) < 0) {
                                artist.getMusicList().add(music);
                            }
                            isAdded = true;
                        }
                    }
                }
                if (!isAdded && !TextUtils.isEmpty(localMusic.getAuthor())) {
                    Artist artist = new Artist();
                    artist.setName(localMusic.getAuthor());
                    artist.getMusicList().add(music);
                    GlobalVariables.listArtist.add(artist);
                }
                //将music添加到文件夹中
                isAdded = false;
                File file = new File(localMusic.getAddr()).getParentFile();
                for (MusicFolder musicFolder : GlobalVariables.listFolder) {
                    if (musicFolder != null) {
                        if (!TextUtils.isEmpty(musicFolder.getPath()) && musicFolder.getPath().equals(file.getAbsolutePath())) {
                            if (isExist(musicFolder.getMusics(), localMusic) < 0) {
                                musicFolder.getMusics().add(localMusic);
                            }
                            isAdded = true;
                        }
                    }
                }
                if (!isAdded) {
                    MusicFolder musicFolder = new MusicFolder();
                    musicFolder.setName(file.getName());
                    musicFolder.setPath(file.getAbsolutePath());
                    musicFolder.getMusics().add(localMusic);
                    GlobalVariables.listFolder.add(musicFolder);
                }
            }
        }
    }

    public static int isExist(List<Music> list, Music music) {
        if (list == null || list.size() <= 0) {
            return -1;
        } else {
            int position = 0;
            for (Music m : list) {
                if (m.getName() != null && m.getName().equals(music.getName())) {
                    return position;
                }
                position++;
            }
        }
        return -1;
    }

    public static void musicCopy(Music from, Music to) {
        if (from == null || to == null) {
            return;
        }
        to.setSize(from.getSize());
        to.setAddr(from.getAddr());
        to.setAlbum(from.getAlbum());
        to.setAlbum_id(from.getAlbum_id());
        to.setAllTime(from.getAllTime());
        to.setAuthor(from.getAuthor());
        to.setImage(from.getImage());
        to.setName(from.getName());
        to.setType(from.getType());
    }
}

