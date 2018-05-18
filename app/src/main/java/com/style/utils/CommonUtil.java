package com.style.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


import cn.tongchengyuan.app.App;

import com.same.city.love.R;
import com.style.constant.Skip;
import com.style.manager.ToastManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class CommonUtil {
    private static final String TAG = "CommonUtil";

    public static String getUUID() {
        String UUID = java.util.UUID.randomUUID().toString();
        return new String(UUID.getBytes(), Charset.forName("UTF-8"));
    }

    public static String md5(final String c) {
        MessageDigest md = null;
        if (md == null) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        if (md != null) {
            if (!TextUtils.isEmpty(c)) {
                md.update(c.getBytes());
                return byte2hex(md.digest());
            }
        }
        return "";
    }

    public static String byte2hex(byte b[]) {
        String hs = "";
        String stmp;
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = (new StringBuffer(String.valueOf(hs))).toString();
        }

        return hs.toLowerCase();
    }

    /**
     * 检测网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    /**
     * 检测wifi是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        context = context == null ? App.getInstance() : context;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiConnectInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiConnectInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /*
     * 打开软键盘
     */
    public static boolean showSoftInput(Activity activity, EditText editText) {
        boolean isFocus = editText.requestFocus();
        //Log.e(TAG, "isFocus==" + isFocus);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isShow = imm.showSoftInput(editText, 0);
        //Log.e(TAG, "isShow==" + isShow);
        if (!isShow)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        return isShow;
    }

    /*
     * 隐藏软键盘
     */
    public static void hiddenSoftInput(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            boolean isHide = imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            //Log.e(TAG, "isHide==" + isHide);
        }
    }

    /*
     * 有输入框时隐藏软键盘
     */
    public static void hiddenSoftInput(Activity activity, EditText editText) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isHide = imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        //Log.e(TAG, "isHide==" + isHide);
    }

    /*
     * 切换软键盘状态
     */
    public static void toggleSoftInput(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            return;
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        } else {
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
        }
    }

    public static CharSequence getNotNullText(CharSequence str) {
        return str == null ? "" : str;
    }

    public static int[] getViewLocationOnScreen(View view) {
        int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        int x = locations[0];// 获取组件当前位置的横坐标
        int y = locations[1];// 获取组件当前位置的纵坐标
        return locations;
    }

    public static int dip2px(Context context, float dpValue) {
        final float pxValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
        return (int) (pxValue + 0.5f);//0.5f是为了四舍五入
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int spToPx(int sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static void setInputTypeNumber(EditText text) {
        // 限制键盘只能输入字母和数字
        text.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
                return InputType.TYPE_CLASS_NUMBER;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] mychar = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
                return mychar;
            }
        });
    }

    public static void setInputTypeEmail(EditText text) {
        // 限制键盘只能输入字母和数字
        text.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
                return InputType.TYPE_CLASS_NUMBER;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] mychar = {'@', '.', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                        'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return mychar;
            }
        });
    }

    public static void setEdittextInputTypePassword(EditText text) {
        // 限制键盘只能输入字母和数字
        text.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                // 0无键盘 1英文键盘 2模拟键盘 3数字键盘
                return InputType.TYPE_CLASS_TEXT;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] mychar = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                        'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
                return mychar;
            }
        });
    }

    public static void setEditTextTheLeastLengthListener(EditText et, final Button bt, final int lessLength) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int index, int start, int end) {
            }

            @Override
            public void beforeTextChanged(CharSequence cs, int index, int start, int end) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.length() >= lessLength) {
                    bt.setEnabled(true);
                } else {
                    bt.setEnabled(false);
                }
            }
        });
    }

    public static void setEditTextPhoneListener(EditText et, final Button bt) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int index, int start, int end) {
            }

            @Override
            public void beforeTextChanged(CharSequence cs, int index, int start, int end) {
            }

            @Override
            public void afterTextChanged(Editable phone) {
                if (FormatUtil.isMobileNum(String.valueOf(phone))) {
                    bt.setEnabled(true);
                } else {
                    bt.setEnabled(false);
                }
            }
        });
    }

    public static void setPasswordVisible(EditText et_password, View view) {
        // TODO Auto-generated method stub
        boolean isVisible = view.isSelected();
        view.setSelected(!isVisible);
        if (isVisible)
            // 文本正常显示
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        else
            // 文本以密码形式显示
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // 下面两行代码实现: 输入框光标一直在输入文本后面
        Editable etable = et_password.getText();
        Selection.setSelection(etable, etable.length());
    }

    public static boolean isSDcardAvailable() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ToastManager.showToastOnApplication(R.string.sd_card_unavailable);
            return false;
        }
        return true;
    }

    public static void notifyUpdateGallary(Context context, File photoFile) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(photoFile);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static File takePhoto(Activity activity, String path) {
        File f = new File(path);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(f);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intent, Skip.CODE_TAKE_CAMERA);
        return f;
    }

    public static File takePhotoFromFragment(Fragment fragment, String path) {
        File f = new File(path);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(f);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        fragment.startActivityForResult(intent, Skip.CODE_TAKE_CAMERA);
        return f;
    }

    public static void selectPhoto(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        activity.startActivityForResult(intent, Skip.CODE_TAKE_ALBUM);
    }

    public static void selectPhotoFromFragment(Fragment fragment) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        fragment.startActivityForResult(intent, Skip.CODE_TAKE_ALBUM);
    }

    public static List<String> getFromAssets(Context context, String fileName, String charSet) {
        ArrayList<String> keyWords = new ArrayList<>();
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getAssets().open(fileName), charSet);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            while ((line = bufReader.readLine()) != null) {
                keyWords.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyWords;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().isEmpty()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiIpAddress==", ex.toString());
        }
        return null;
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int verCode = -1;
        try {
            // 注意："com.example.try_downloadfile_progress"对应AndroidManifest.xml里的package="……"部分
            int pid = android.os.Process.myPid();
            String processAppName = getAppName(context, pid);
            PackageInfo info = context.getPackageManager().getPackageInfo(processAppName, 0);
            verCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verCode;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo("com.meilian.youyuan", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verName;
    }

    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     *
     * @param pID
     * @return
     */
    private static String getAppName(Context context, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    public static int getScreenWidth(Context context) {
        return getMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getMetrics(context).heightPixels;
    }

    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        getDisplay(context).getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        float density = metric.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        return metric;
    }

    public static Display getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

    public static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String id = tm.getDeviceId();
            if (id != null) {
                return tm.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void openEditSms(Context context, String phone) {
        Uri smsToUri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        intent.putExtra("sms_body", "我最近成为了脉连的用户，可以随时查找与别人的连系，让交友变得简单可信。交朋友、找关系好用极了，推荐给你试试（下载链接:www.yimxl.com）。");
        context.startActivity(intent);
    }
}
