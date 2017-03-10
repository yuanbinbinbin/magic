package yb.com.magicplayer.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;


import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import yb.com.magicplayer.R;
import yb.com.magicplayer.entity.LocalMusic;

public class MediaUtils {

    public static List<LocalMusic> getLocalMusics(Context mContext) {
        List<LocalMusic> mList = new ArrayList<LocalMusic>();
        Cursor mCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                ConfigData.media_music_info, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (mCursor == null)
            return null;
        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount(); i++) {
            if (mCursor.getInt(4) > ConfigData.MUSIC_SIZE_MIN) {
                LocalMusic mLocalMusic = new LocalMusic(mCursor.getInt(0),
                        mCursor.getString(1), mCursor.getString(2), null,
                        mCursor.getInt(3) / 1000, mCursor.getInt(4),
                        mCursor.getString(5), mCursor.getInt(6));
                mLocalMusic.setImage(getMusicImgUrl(mCursor.getInt(0), mCursor.getInt(6), true));
                mList.add(mLocalMusic);
            }
            mCursor.moveToNext();
        }
        return PaiXuByZMu(mList);
    }

    private static String getMusicImgUrl(long songid, long albumid,
                                         boolean allowdefault) {
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

    public static List<LocalMusic> PaiXuByZMu(List<LocalMusic> list) {
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
        List<LocalMusic> musics = new ArrayList<LocalMusic>();
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

    public static int getPositionInLocalMusicList(List<LocalMusic> list, int id) {
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

}

