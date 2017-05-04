package com.style.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lxt on 11/29/15.
 */
public class DateUtil {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat dateFormat11 = new SimpleDateFormat("yyyy年MM月dd");
    public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat dateFormat4 = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat dateFormat5 = new SimpleDateFormat("yyyyMM");
    public static SimpleDateFormat dateFormat6=  new SimpleDateFormat("HH小时mm分ss秒");
    private static boolean LOG_SWITCH = true;

    public static String getDateStringFromTimeStamp(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat.format(calendar.getTime());
    }

    public static String getDateStringAccurateToDay(Long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat1.format(calendar.getTime());
    }

    public static String getDateStringAccurateToDay1(Long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat11.format(calendar.getTime());
    }

    public static String getDateStringAccurateToMinute(Long time) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat2.format(calendar.getTime());
    }

    public static String getDateStringAccurateToDate(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat3.format(calendar.getTime());
    }

    public static String getDateStringAccurateToTime(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat4.format(calendar.getTime());
    }

    public static String getDateStringAccurateToYearMonth(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat5.format(calendar.getTime());
    }

    public static String getDateStringAccurateToYearHour(Long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat6.format(calendar.getTime());
    }
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static long stringToDate(String strTime)
            throws ParseException {
        Date date = null;
        date = dateFormat.parse(strTime);
        return date.getTime();
    }
    //计算倒计时的时间
    public static String endData(Long time) {
        long second = time / 1000;
        if (second < 60) {
            return "0:0:" + second;
        }
        long minute = second / 60;
        if (minute < 60) {
            second = second - 60 * minute;
            return "0:" + minute + ":" + second;
        }
        long hour = minute / 60;
        if (hour < 24) {
            second = second - 60 * minute;
            minute = minute - 60 * hour;
            return hour + ":" + minute + ":" + second;
        }
        long day = hour / 24;
        second = second - 60 * minute;
        minute = minute - 60 * hour;
        hour = hour - 24 * day;
        return day + "天" + " " + hour + ":" + minute + ":" + second;
    }
}
