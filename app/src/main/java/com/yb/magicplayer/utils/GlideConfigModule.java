package com.yb.magicplayer.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpGlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;

/**
 * Glide图片加载框架，配置
 * Created by yb on 2017/3/17.
 */
public class GlideConfigModule extends OkHttpGlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //设置磁盘缓存的位置和大小
        //InternalCacheDiskCacheFactory  缓存在内部存储，其他应用无法访问，地址为:/android/data/包名/cache/缓存文件名/
        //ExternalCacheDiskCacheFactory  缓存在外部存储，公共部分，其他应用也可访问，地址为:/缓存文件夹名/
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, ConfigData.IMAGE_CACHE_FILE_NAME, ConfigData.IMAGE_CACHE_DISK_FILE_SIZE));
        //设置内存缓存的大小
        //LRU算法保留最近使用的Bitmap
        //默认大小是由MemorySizeCalculator类。MemorySizeCalculator类通过考虑设备给定的可用内存和屏幕大小想出合理的默认大小。
        builder.setMemoryCache(new LruResourceCache(ConfigData.IMAGE_CACHE_MEMORY_SIZE));
        //bitmap pool用于各种不同的大小的Bitmap重复使用以其显著减少以及避免垃圾回收Bitmap分配而造成的图像被销毁。
        builder.setBitmapPool(new LruBitmapPool(ConfigData.IMAGE_CACHE_MEMORY_SIZE));
        //设置图片解码格式
        builder.setDecodeFormat(DecodeFormat.ALWAYS_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
