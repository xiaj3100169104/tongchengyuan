package cn.tongchengyuan.util;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import cn.tongchengyuan.app.App;

/**
 * Created by 王宗文 on 2016/6/13.
 */
public class ToastUtil {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable(){
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast (String text, int duration) {
        Context context = App.getInstance();
        mHandler.removeCallbacks(r);
        if (null != mToast) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(context, text, duration);
        }
        mHandler.postDelayed(r, 5000);
        mToast.show();
    }

    public static void showToast (int strId, int duration) {
        Context context = App.getInstance();
        showToast (context.getString(strId), duration);
    }

}
