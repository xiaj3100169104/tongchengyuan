package cn.tongchengyuan.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import cn.tongchengyuan.app.App;


/**
 * Created by liuzhe on 2014/6/24.
 */
public class NetWorkUtil {

    private static final String TAG = NetWorkUtil.class.getSimpleName();

    public static boolean isNetworkAvailable() {
        Context context = App.getInstance();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null){
            return false;
        }else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0){
                for (int i = 0; i < networkInfo.length; i++){
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public final static String NETWORK_CONN_MOBILE = "connect_mobile";
    public final static String NETWORK_CONN_WIFI = "connect_wifi";

    public static String getNetworkConnType(Context context){
        ConnectivityManager connectMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectMgr){
            return null;
        }

        NetworkInfo info = connectMgr.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return null;
        }
        if (info.isRoaming()) {
            // here is the roaming option you can change it if you want to
            // disable internet while roaming, just return false
            return null;
        }

        NetworkInfo mobileNetworkInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (null == mobileNetworkInfo){
            return null;
        }
        NetworkInfo.State mobile = mobileNetworkInfo.getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            return NETWORK_CONN_MOBILE;
        }

        NetworkInfo wifiNetworkInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null == wifiNetworkInfo){
            return null;
        }
        NetworkInfo.State wifi = wifiNetworkInfo.getState();
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return NETWORK_CONN_WIFI;
        }
        return null;
    }

}
