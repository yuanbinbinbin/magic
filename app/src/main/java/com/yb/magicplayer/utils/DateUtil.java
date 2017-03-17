package com.yb.magicplayer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * Created by yb on 2017/3/14.
 */
public class DateUtil {
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 日期格式话
     * 2017.03.14
     *
     * @param date
     * @return
     */
    public static String DateFormat1(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat.format(date);
    }
}
