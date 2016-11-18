package com.style.utils;

import android.text.TextUtils;
import android.util.Base64;

public class Base64Util {

    public static String string2Base64string(String string) {
        String value = "";
        if (!TextUtils.isEmpty(string))
            value = Base64.encodeToString(string.getBytes(), Base64.NO_WRAP);
        return value;
    }

    public static String base64string2string(String base64string) {
        String value = "";
        if (!TextUtils.isEmpty(base64string)) {
            byte[] bytes = Base64.decode(base64string, Base64.DEFAULT);
            value = new String(bytes);
        }
        return value;
    }
   /* CRLF 这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
    DEFAULT 这个参数是默认，使用默认的方法来加密
    NO_PADDING 这个参数是略去加密字符串最后的”=”
    NO_WRAP 这个参数意思是略去所有的换行符（设置后CRLF就没用了）
    URL_SAFE 这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/*/
}
