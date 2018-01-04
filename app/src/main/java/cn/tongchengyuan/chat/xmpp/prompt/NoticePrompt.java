package cn.tongchengyuan.chat.xmpp.prompt;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;


import cn.tongchengyuan.app.App;
import cn.tongchengyuan.util.LogUtil;
import cn.tongchengyuan.util.SharedPreferencesUtil;

import com.same.city.love.R;
import com.style.constant.Skip;

import java.util.HashMap;
import java.util.Map;

/**
 * ****************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * <p/>
 * This file is part of YY Cube project.
 * <p/>
 * It can not be copied and/or distributed without the express
 * permission of Yunyun Network
 * Created by 王宗文 on 2015/7/3
 * *****************************************************
 */
public class NoticePrompt extends Prompt implements ISwitch {
    public final static String NOTICE_PROMPT = "notice_prompt";
    public final static String VIBRATIONNOTIFY = "vibration_list";
    public final static String TICKER = "ticker";

    private String TAG = NoticePrompt.class.getName();
    private static final int MAX_TICKER_MSG_LEN = 50;
    protected Context mContext;
    private Vibrator mVibrator;
    private PowerManager.WakeLock mWakeLock;
    private NotificationManager mNotificationManager;
    private Map<String, Integer> mNotificationCount = new HashMap<>(2);
    private Map<String, Integer> mNotificationId = new HashMap<>(2);
    private Notification mNotification;
    private int mLastNotificationId = 2;

    public NoticePrompt(Context context) {
        super(context);
        mContext = context;
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        String appName = "wechat";
        mWakeLock = ((PowerManager) mContext.getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, appName);
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public final void switchOn() {
        SharedPreferencesUtil.putBooleanValue(App.getInstance(), NOTICE_PROMPT, true);
    }

    @Override
    public final void switchOff() {
        SharedPreferencesUtil.putBooleanValue(App.getInstance(), NOTICE_PROMPT, false);
    }

    @Override
    public final boolean isSwitchOn() {
        return SharedPreferencesUtil.getBooleanValue(App.getInstance(), NOTICE_PROMPT, true);
    }

    private void setLEDNotification() {
        mNotification.ledARGB = Color.GREEN;
        mNotification.ledOnMS = 300;
        mNotification.ledOffMS = 1000;
        mNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
    }


    public final void notifyClient(String fromUserName, String message, Intent noticeIntent) {
        if (!isSwitchOn()) {
            return;
        }
        if (isOnForeGround()) {
            return;
        }

        LogUtil.i("isOnForeGround:" + isOnForeGround());

        mWakeLock.acquire();
        setNotification(fromUserName, message, noticeIntent);
        setLEDNotification();

        int notifyId;
        if (mNotificationId.containsKey(fromUserName)) {
            notifyId = mNotificationId.get(fromUserName);
        } else {
            mLastNotificationId++;
            notifyId = mLastNotificationId;
            mNotificationId.put(fromUserName, Integer.valueOf(notifyId));
        }
        // If vibration is set to true, add the vibration flag to
        // the notification and let the system decide.
        boolean vibraNotify = SharedPreferencesUtil.getBooleanValue(App.getInstance(), VIBRATIONNOTIFY, true);
        if (vibraNotify) {
            mVibrator.vibrate(400);
        }
        mNotificationManager.notify(notifyId, mNotification);

        mWakeLock.release();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setNotification(String fromUserName,
                                 String message, Intent noticeIntent) {

        int mNotificationCounter = 0;
        if (mNotificationCount.containsKey(fromUserName)) {
            mNotificationCounter = mNotificationCount.get(fromUserName);
        }
        mNotificationCounter++;
        mNotificationCount.put(fromUserName, mNotificationCounter);

        String title = fromUserName;
        String ticker;
        boolean isTicker = SharedPreferencesUtil.getBooleanValue(mContext,
                TICKER, true);
        if (isTicker) {
            int newline = message.indexOf('\n');
            int limit = 0;
            String messageSummary = message;
            if (newline >= 0)
                limit = newline;
            if (limit > MAX_TICKER_MSG_LEN
                    || message.length() > MAX_TICKER_MSG_LEN)
                limit = MAX_TICKER_MSG_LEN;
            if (limit > 0)
                messageSummary = message.substring(0, limit) + " [...]";
            ticker = title + ":\n" + messageSummary;
        } else {
            ticker = fromUserName;
        }

        noticeIntent.putExtra(Skip.KEY_USER_NAME, fromUserName);
        noticeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // need to set flag FLAG_UPDATE_CURRENT to get extras transferred
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0,
                noticeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker(ticker);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setDefaults(Notification.DEFAULT_SOUND);

        mNotification = builder.build();
        if (mNotificationCounter > 1)
            mNotification.number = mNotificationCounter;
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
    }

}
