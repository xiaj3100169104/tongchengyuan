package cn.tongchengyuan.util;

/**
 * 时间日期 处理工具
 * create by 王宗文 on 2015/8/17
 *
 * @version 1.5
 */

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {
    public static String format(long timeInMillis, boolean fullTime) {
        SimpleDateFormat sdf = null;
        if (fullTime) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }

        String result = sdf.format(new Date(timeInMillis));
        return result;
    }

    public static String format(Date date, boolean fullTime) {
        SimpleDateFormat sdf = null;
        if (fullTime) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }

        String result = sdf.format(date);
        return result;
    }

    /**
     * 将字符串类型的日期转换成毫秒
     *
     * @param date
     * @param fullTime
     * @return
     */
    public static Date format(String date, boolean fullTime) {
        SimpleDateFormat sdf = null;
        if (fullTime) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        try {
            Date d = sdf.parse(date);
            return d;
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     *
     * @param date 时间字符串
     * @param dateFormat 将要转换为的格式
     * @return
     */
    public static String format(String date, String dateFormat) {
        try {
            Date d = new Date(date);
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.format(d);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据生日获取年龄 格式必须符合
     *
     * @param s1 生日
     * @param s2 当天的年月日
     * @return
     */
    public static int getDifftime(String s1, String s2) {
        int year = Integer.parseInt(s2.split("-")[0]) - Integer.parseInt(s1.split("-")[0]);
        int month = Integer.parseInt(s2.split("-")[1]) - Integer.parseInt(s1.split("-")[1]);
        if (month < 0) {
            month += 12;
            year += -1;
        }
        return year;
    }

    /**
     * 获取格式化时间 HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    public static String getFreeCallTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getRecentTime(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date nowDate = new Date();
        int firstDayOfThisWeek = getFirstDayOfWeek(nowDate);
        int day = date.getDate();
        if(day >= firstDayOfThisWeek){
            if(nowDate.getDate() == day){
                try{
                    return format.format(date);
                }catch (Exception e){
                    return null;
                }

            }else if(nowDate.getDate() - day == 1){
                return "昨天";
            }else if(nowDate.getDate() - day == 2){
                return "前天";
            }else {
                String strWeek;
                try {
                    strWeek = "星期" + "日一二三四五六".substring(date.getDay(), date.getDay() + 1);
                }catch (Exception e){
                    LogUtil.e("day: " + date.getDay());
                    strWeek = "";
                }

                return strWeek;
            }
        }else {
            format = new SimpleDateFormat("yyyy-MM-dd");
            return format.format(date);
        }
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();
        long t = format(timeStr,true).getTime();
       // long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time / 1000);//秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前



        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    /**
     * 取得指定日期所在周的第一天的日期号
     *
     * @param date
     * @return
     */
    public static int getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setTime(date);

        int dayWeek = c.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.get(Calendar.DAY_OF_MONTH);
    }

}
