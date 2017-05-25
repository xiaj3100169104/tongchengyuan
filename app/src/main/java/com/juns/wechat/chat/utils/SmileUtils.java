package com.juns.wechat.chat.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.same.city.love.R;

//表情
public class SmileUtils {

    private static List<SmileBean> emoticonsNames2;

    private static final Factory spannableFactory = Spannable.Factory.getInstance();

    private static Map<Pattern, Integer> emoticons = new HashMap<>();

    private static void addPattern(String smile, int resource) {
        emoticons.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    public static List<SmileBean> getSmileData2(Context context) {
        if (emoticonsNames2 == null) {
            emoticonsNames2 = new ArrayList<>();
            emoticons = new HashMap<>();

            for (int x = 1; x <= 43; x++) {
                String name = "e_" + x;
                int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
                String key = "[(A" + x + ")]";
                emoticonsNames2.add(new SmileBean(key, resId));
                addPattern(key, resId);
            }
            for (int x = 0; x <= 62; x++) {
                String name = "f_static_0" + x;
                int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
                String key = "[(B" + x + ")]";
                emoticonsNames2.add(new SmileBean(key, resId));
                addPattern(key, resId);
            }
            for (int x = 44; x <= 64; x++) {
                String name = "e_" + x;
                int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
                String key = "[(A" + x + ")]";
                emoticonsNames2.add(new SmileBean(key, resId));
                addPattern(key, resId);
            }
        }
        return emoticonsNames2;
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(), matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start() && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Drawable drawable = context.getResources().getDrawable(entry.getValue());
                    drawable.setBounds(0, 0, 60, 60);// 这里设置图片的大小
                    ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, String sequence) {
        Spannable spannable = spannableFactory.newSpannable(sequence);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static class SmileBean {
        public String key;
        public int resId;

        public SmileBean(String key, int resId) {
            this.key = key;
            this.resId = resId;
        }
    }
}
