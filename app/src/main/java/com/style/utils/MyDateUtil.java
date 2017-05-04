package com.style.utils;

import android.annotation.SuppressLint;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class MyDateUtil {
    public final static String FORMAT_yyyy = "yyyy";
    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_MONTH_DAY = "MM月dd日";
    public final static String FORMAT_MM_dd = "MM-dd";
    public final static String FORMAT_HH_mm = "HH:mm";
    public final static String FORMAT_HH_mm_ss = "HH:mm:ss";
    public final static String FORMAT_MM_dd_HH_mm_CHINA = "MM月dd日  HH:mm";
    public final static String FORMAT_MM_dd_HH_mm = "MM-dd HH:mm";
    public final static String FORMAT_DATE1_TIME = "yyyy/MM/dd HH:mm";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy/MM/dd HH:mm:ss";
    public final static String FORMAT_DATE_TIME_SECOND_NUMBER = "yyyyMMddHHmmss";
    private static SimpleDateFormat sdf = new SimpleDateFormat();

    //单位：秒 返回：00:00
    public static String formatMiss(long miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        if ((miss * 1000) < DateUtils.HOUR_IN_MILLIS)
            return mm + ":" + ss;
        else
            return hh + ":" + mm;
    }

    //单位：秒 返回：00:00:00
    public static String formatHHMMSS(long miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    /*
     * 判断strTime是否是指定格式formatType的日期字符串
     */
    public static boolean strTimeIsTheCorrectFormatType(String strTime, String formatType) {
        try {
            DateFormat formatter = new SimpleDateFormat(strTime);
            Date date = (Date) formatter.parse(strTime);
            return strTime.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    public static String getTimeConversationString(long millions) {
        String format = null;
        if (isBelongToThisYear(millions)) {
            if (isTheDayBefore(millions, 0)) {
                format = "HH:mm";
            } else if (isTheDayBefore(millions, -1)) {
                format = "昨天 HH:mm";
            } else if (isTheDayBefore(millions, -2)) {
                format = "前天 HH:mm";
            } else {
                format = FORMAT_MM_dd;
            }
        } else
            format = FORMAT_DATE;
        return new SimpleDateFormat(format, Locale.CHINA).format(new Date(millions));
    }

    /*
     * @param 返回描述性时间，"今天 HH:mm"，"昨天 HH:mm"，"前天 HH:mm",yyyy-MM-dd HH:mm
     */
    public static String getTimeDynamicString(String strTime, String formatType) {
        if (!strTimeIsTheCorrectFormatType(strTime, formatType)) {
            return null;
        }
        Date date = stringToDate(strTime, formatType);
        if (null == date) {
            return null;
        }
        long speMillis = stringToLong(strTime, formatType);
        String format = null;
        if (isBelongToThisYear(speMillis)) {
            if (isTheDayBefore(speMillis, 0)) {
                format = "今天 HH:mm";
            } else if (isTheDayBefore(speMillis, -1)) {
                format = "昨天 HH:mm";
            } else if (isTheDayBefore(speMillis, -2)) {
                format = "前天 HH:mm";
            } else {
                format = FORMAT_MM_dd_HH_mm_CHINA;
            }
        } else {
            format = FORMAT_yyyy_MM_dd_HH_mm;
        }
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }

    /**
     * @param strTime 时间字符串
     * @param formatType 格式
     * @return  返回描述性时间:多久前
     */
    public static String getTimeFromNow(String strTime, String formatType) {
        if (!strTimeIsTheCorrectFormatType(strTime, formatType)) {
            return null;
        }
        Date date = stringToDate(strTime, formatType);
        if (null == date) {
            return null;
        }
        long speMillis = stringToLong(strTime, formatType);
        long currentMillis = System.currentTimeMillis();
        long value = currentMillis - speMillis;
        String discribleTime = null;
        if (value > DateUtils.DAY_IN_MILLIS) {
            int num = (int) (value / DateUtils.DAY_IN_MILLIS);
            discribleTime = num + "天前";
        } else if (value > DateUtils.HOUR_IN_MILLIS) {
            int num = (int) (value / DateUtils.HOUR_IN_MILLIS);
            discribleTime = num + "小时前";
        } else if (value > DateUtils.MINUTE_IN_MILLIS) {
            int num = (int) (value / DateUtils.MINUTE_IN_MILLIS);
            discribleTime = num + "分钟前";
        } else if (value > DateUtils.SECOND_IN_MILLIS) {
            int num = (int) (value / DateUtils.SECOND_IN_MILLIS);
            discribleTime = num + "秒前";
        }
        return discribleTime;
    }

    /**
     * 判断是否属于今年
     *
     * @param inputTime 指定时间
     * @return true 属于，false不属于
     */
    private static boolean isBelongToThisYear(long inputTime) {
        String year = longToString(inputTime, FORMAT_yyyy);
        String curYear = longToString(System.currentTimeMillis(), FORMAT_yyyy);
        if (year.equals(curYear))
            return true;
        return false;
    }

    /**
     * 判断是否属于近几天中那一天的时间
     *
     * @param inputTime 指定时间
     * @param dayBefore 0代表今天，-1代表昨天，-2代表前天，，，，
     * @return true 属于指定那天，false不属于指定那天
     */
    private static boolean isTheDayBefore(long inputTime, int dayBefore) {
        TimeInfo ti = getDayStartAndEndTime(dayBefore);
        if (inputTime > ti.getStartTime() && inputTime < ti.getEndTime())
            return true;
        return false;
    }

    /**
     * 近几天的开始和结束时间信息，24小时制
     *
     * @param dayBefore 0代表今天，-1代表昨天，-2代表前天，，，，
     * @return 近几天的开始和结束时间信息，24小时制
     */
    public static TimeInfo getDayStartAndEndTime(int dayBefore) {
        Calendar calendar1 = Calendar.getInstance();
        if (dayBefore != 0) {
            calendar1.add(Calendar.DATE, dayBefore);
        }
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();

        Calendar calendar2 = Calendar.getInstance();
        if (dayBefore != 0) {
            calendar2.add(Calendar.DATE, -1);
        }
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();

        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    public static Date getCurrentTimeForDate(String df) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(df);// 设置日期格式
        System.out.println(sdf.format(date));// new Date()为获取当前系统时间
        return date;
    }

    public static String getCurrentTimeForString(String df) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(df);// 设置日期格式
        String d = sdf.format(date);
        return d;
    }

    /**
     * 获取当前日期的指定格式的字符串
     *
     * @param format 指定的日期时间格式，若为null或""则使用指定的格式"yyyy-MM-dd HH:MM"
     * @return
     */
    public static String getCurrentTime(String format) {
        if (format == null || format.trim().equals("")) {
            sdf.applyPattern(FORMAT_yyyy_MM_dd_HH_mm);
        } else {
            sdf.applyPattern(format);
        }
        return sdf.format(new Date());
    }

    // date类型转换为String类型
    // formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    /*
     * string类型转换为date类型 strTime要转换的string类型的时间， formatType要转换的格式 HH时mm分ss秒，
     * strTime的时间格式必须要与formatType的时间格式相同
     */
    public static Date stringToDate(String strTime, String formatType) {
        if (!strTimeIsTheCorrectFormatType(strTime, formatType)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime(); // date类型转成long类型
            return currentTime;
        }
    }

    // 时间戳：long类型转换为String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    public static String longToString(long time, String formatType) {
        SimpleDateFormat format = new SimpleDateFormat(formatType);
        return format.format(new Date(time));
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek;
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param millis 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static String longToWeek(long millis) {
        String format = longToString(millis, FORMAT_DATE);
        int index = 0;
        try {
            index = dayForWeek(format);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String week[] = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日",};
        return week[index];
    }


    public static String getAge(String birthday) {
        String age = null;
        if (null != birthday && birthday.length() > 4) {
            int year1 = Calendar.getInstance().get(Calendar.YEAR);
            int year2 = Integer.valueOf(new StringBuffer(birthday).substring(0, 4));
            age = String.valueOf(year1 - year2);
        }
        return age;
    }

    public static class TimeInfo {
        private long startTime;
        private long endTime;

        public TimeInfo() {
        }

        public long getStartTime() {
            return this.startTime;
        }

        public void setStartTime(long var1) {
            this.startTime = var1;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public void setEndTime(long var1) {
            this.endTime = var1;
        }
    }

}