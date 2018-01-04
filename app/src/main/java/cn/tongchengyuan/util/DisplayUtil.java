package cn.tongchengyuan.util;

import android.content.Context;
import android.graphics.Point;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by 王宗文 on 2015/4/17.
 */
public class DisplayUtil {
    private static final String TAG = "DisplayUtil";
    private static float highDensity = 320;  //以小米3的参数为准
    private static double highSclaedDensity = 2.0; //以小米3的参数为准

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        int dip = (int)(dipValue * scale + 0.5f);
        return dip;
    }

    private static float calcHighDp(float pxValue){
        return (float)(pxValue / highSclaedDensity + 0.5f);
    }

    public static int tranferPx(Context context, float pxValue){
        float scale = context.getResources().getDisplayMetrics().density;
        float scaleDensity = context.getResources().getDisplayMetrics().scaledDensity;
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        float dp = (float)(pxValue*scale / highSclaedDensity + 0.5f*scale);
        return (int)dp;
    }


    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }


    public static int px2sp(Context context, float pxValue) {
        int px = tranferPx(context, pxValue);
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变

     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 获取屏幕宽度和高度，单位为px
     */
    public static Point getScreenMetrics(Context context){
        DisplayMetrics dm =context.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }

    /**
     * 获取屏幕长宽比
     */
    public static float getScreenRate(Context context){
        Point P = getScreenMetrics(context);
        float H = P.y;
        float W = P.x;
        return (H/W);
    }


    // 计算出该TextView中文字的长度(像素)
    public static float getTextViewLength(TextView textView,String text){
        TextPaint paint = textView.getPaint();
        // 得到使用该paint写上text的时候,像素为多少
        return paint.measureText(text);
    }
    /**

     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value
     *            指定的字符串
     * @return 字符串的长度
     */
    public static int getStringLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
