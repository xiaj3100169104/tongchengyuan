package com.style.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringUtil {

    /**
     * 字符串转数组
     * @param str
     * @param tag 分隔符
     * @return
     */
    public static String[] String2Array(String str, String tag) {
        if (TextUtils.isEmpty(str)) {
            return null;
        } else {
            String[] s = str.split(tag);
            return s;
        }
    }

    /**
     * 字符串转list
     * @param str
     * @param tag 字符串分隔符
     * @return
     */
    public static List<String> String2List(String str, String tag) {
        if (TextUtils.isEmpty(str)) {
            return null;
        } else {
            List<String> list = new ArrayList<String>();
            String[] s = str.split(tag);
            for (int i = 0; i < s.length; i++) {
                list.add(s[i]);
            }
            return list;
        }
    }

    /**
     * list转字符串
     * @param list
     * @param tag
     * @return
     */
    public static String List2String(List<String> list, String tag) {
        StringBuilder b = new StringBuilder("");
        for (String s : list) {
            b.append(s).append(tag);
        }
        if (b.length() > 0)
            b.deleteCharAt(b.length() - 1);
        String value = b.toString();
        return value;
    }
    public static String[] List2StringArray(List<String> list) {
        String[] s = null;
        if (list != null) {
            int size = list.size();
            s = new String[size];
            for (int i = 0; i < size; i++) {
                s[i] = list.get(i);
            }
        }
        return s;
    }
    public static List<String> Array2List(String[] s) {
        List<String> list = new ArrayList<>();
        if (s != null) {
            int size = s.length;
            for (int i = 0; i < size; i++) {
                list.add(s[i]);
            }
        }
        return list;
    }
    /**
     * 检测String不能为空格
     *
     * @param name
     * @return
     */

    public static boolean isHasSpace(String name) {

        boolean res = true;
        if (name.contains(" ")) {
            res = false;

        }
        return res;

    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */

    public static boolean isOnlyChinese(String name) {

        boolean res = true;
        char[] cTemp = name.toCharArray();

        for (int i = 0; i < name.length(); i++) {

            if (!isChinese(cTemp[i])) {

                res = false;
                break;

            }

        }

        return res;

    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

            return true;

        }

        return false;

    }

    public static Object[] List2Array(List<Object> oList) {
        Object[] oArray = oList.toArray(new Object[] {});
        // TODO 需要在用到的时候另外写方法，不支持泛型的Array.
        return oArray;
    }

    public static Object[] Set2Array(Set<Object> oSet) {
        Object[] oArray = oSet.toArray(new Object[] {});
        // TODO 需要在用到的时候另外写方法，不支持泛型的Array.
        return oArray;
    }

    public static <T extends Object> List<T> Set2List(Set<T> oSet) {
        List<T> tList = new ArrayList<T>(oSet);
        // TODO 需要在用到的时候另外写构造，根据需要生成List的对应子类。
        return tList;
    }

    public static <T extends Object> List<T> Array2List(T[] tArray) {
        List<T> tList = Arrays.asList(tArray);
        // TODO 单纯的asList()返回的tList无法add(),remove(),clear()等一些影响集合个数的操作，
        // 因为Arrays$ArrayList和java.util.ArrayList一样，都是继承AbstractList，
        // 但是Arrays$ArrayList没有override这些方法，而java.util.ArrayList实现了。
        // TODO 建议使用List的子类做返回，而不是Arrays$ArrayList。根据需要吧。如下行注释:
        // List<T> tList = new ArrayList<T>(Arrays.asList(tArray));
        return tList;
    }

    public static <T extends Object> Set<T> List2Set(List<T> tList) {
        Set<T> tSet = new HashSet<T>(tList);
        //TODO 具体实现看需求转换成不同的Set的子类。
        return tSet;
    }

    public static <T extends Object> Set<T> Array2Set(T[] tArray) {
        Set<T> tSet = new HashSet<T>(Arrays.asList(tArray));
        // TODO 没有一步到位的方法，根据具体的作用，选择合适的Set的子类来转换。
        return tSet;
    }
}
