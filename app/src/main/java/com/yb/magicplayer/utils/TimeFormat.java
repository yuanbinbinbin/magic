package com.yb.magicplayer.utils;

/**
 * 时间格式化
 * Created by yb on 2017/4/6.
 */
public class TimeFormat {
    public static String millis2mmss(long millis) {
        if (millis < 0) {
            return "";
        }
        StringBuffer result = new StringBuffer("");
        long s = millis / 1000;
        if (s / 60 >= 10) {
            result.append(s / 60);
        } else {
            result.append("0");
            result.append(s / 60);
        }
        result.append(":");
        if (s % 60 >= 10) {
            result.append(s % 60);
        } else {
            result.append("0");
            result.append(s % 60);
        }
        return result.toString();
    }
}
