package com.style.view.progressbar;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by bruce on 14-11-6.
 */
public class Utils {

    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
