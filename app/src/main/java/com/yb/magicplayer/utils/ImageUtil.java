package com.yb.magicplayer.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * 加载图片的工具类
 * Created by yb on 2017/3/17.
 */
public class ImageUtil {
    public static void loadImage(Context context, String path, ImageView iv) {
        Glide.with(context).load(path).centerCrop().into(iv);
    }

    public static void loadImage(Context context, String path, ImageView iv, int defaultImg) {
        Glide.with(context).load(path).placeholder(defaultImg).centerCrop().into(iv);
    }

    public static void loadImage(Context context, String path, ImageView iv, int defaultImg, int errorImg) {
        Glide.with(context).load(path).placeholder(defaultImg).error(errorImg).centerCrop().dontAnimate().dontTransform().into(iv);
    }

    public static void loadGif(Context context, String path, ImageView iv) {
        Glide.with(context).load(path).asGif().centerCrop().into(iv);
    }

    public static void loadGif(Context context, String path, ImageView iv, int defaultImg) {
        Glide.with(context).load(path).asGif().placeholder(defaultImg).centerCrop().into(iv);
    }

    public static void loadGif(Context context, String path, ImageView iv, int defaultImg, int errorImg) {
        Glide.with(context).load(path).asGif().placeholder(defaultImg).error(errorImg).centerCrop().into(iv);
    }


}
