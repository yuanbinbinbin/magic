package yb.com.magicplayer.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * FastJson转化工具，有利于方便转成其他转化工具如Gson
 * Created by yb on 2017/3/16.
 */
public class FastJsonUtil {
    public static String toJsonString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
        }
        return "";
    }

    public static <T> List<T> parseArray(String s, Class<T> clazz) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        try {
            return JSON.parseArray(s, clazz);
        } catch (Exception e) {

        }
        return null;
    }

    public static <T> T parseObject(String s, Class<T> clazz) {
        if (TextUtils.isEmpty(s)) {
            return null;
        }
        try {
            return JSON.parseObject(s, clazz);
        } catch (Exception e) {

        }
        return null;
    }
}
