package com.style.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {
    /**
     * 产生随机数，首位不为0
     *
     * @param count 位数
     * @return count 位随机数
     */
    public static String makeRandom(int count) {
        int k = (int) (Math.random() * Math.pow(10, count - 1));
        if (k < Math.pow(10, count - 1)) {
            k += Math.pow(10, count);
        }
        String s = String.valueOf(k);
        return s;
    }

    public static boolean isMobileNum(CharSequence mobiles) {
        Pattern p = Pattern.compile("^(1[3,4,5,7,8][0-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmail(CharSequence s) {
        //Pattern p = Pattern.compile("/^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$/");
        Pattern p=Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");;
        Matcher m = p.matcher(s);
        return m.matches();
    }

    public static boolean isMainAccountLength(String mainAccount) {
        if (mainAccount.length() != 7) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean ismainAccount(String mainAccount) {
        Pattern p = Pattern.compile("^[1-9]\\d{3}$");
        Matcher m = p.matcher(mainAccount);
        return m.matches();
    }

    public static boolean isFriendAccount(String mainAccount) {
        Pattern p = Pattern.compile("^(?!)a[0-9]\\d{7}$");
        Matcher m = p.matcher(mainAccount);
        return m.matches();
    }

    public static boolean isWorldAccount(String mainAccount) {
        Pattern p = Pattern.compile("^(?!)m[0-9]\\d{7}$");
        Matcher m = p.matcher(mainAccount);
        return m.matches();
    }

    public static boolean isPassword(CharSequence password) {
        /*
         * Pattern p =
		 * Pattern.compile("(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{6,})$");
		 * Matcher m = p.matcher(password); return m.matches();
		 */
        if (password.length() >= 6 && password.length() <= 15) {
            return true;
        } else {
            return false;
        }
    }

    public static InputFilter getLengthFilter(int length) {
        return new InputFilter.LengthFilter(length);
    }

    public static InputFilter getNoSpaceFilter() {
        return new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (" ".equals(source)) {
                    return "";
                }
                return source;
            }
        };
    }

    public static boolean isNumeric(String str){
        if(str.isEmpty()) return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}
