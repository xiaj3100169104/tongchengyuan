package cn.tongchengyuan.chat.xmpp.prompt;

import android.content.Context;
import android.media.MediaPlayer;

import cn.tongchengyuan.app.App;
import cn.tongchengyuan.util.SharedPreferencesUtil;


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
public class SoundPrompt extends Prompt implements ISwitch {
    protected Context mContext;
    private int mRingResId;
    private static long mPlayMills =0;


    public final static String SCLIENTNOTIFY= "ringdone";

    public SoundPrompt(Context context){
        super(context);
        mContext = context;
    }

    @Override
    public final void switchOn(){
        SharedPreferencesUtil.putBooleanValue(App.getInstance(), SCLIENTNOTIFY, true);
    }

    @Override
    public final void switchOff(){
        SharedPreferencesUtil.putBooleanValue(App.getInstance(), SCLIENTNOTIFY, false);
    }

    @Override
    public final boolean isSwitchOn(){
        return SharedPreferencesUtil.getBooleanValue(App.getInstance(), SCLIENTNOTIFY, true);
    }

    public void ringDone(){
        if (!isSwitchOn()){
            return;
        }

        if (isOnChatActivity()){
            return;
        }

        if (!needPlay()){
            return;
        }

        MediaPlayer player = MediaPlayer.create(mContext, mRingResId);
        player.start();
    }

    public void setRingRes(int resId) {
        mRingResId = resId;
    }

    /**
     * 判断提示音播放是否超过1.5s
     * @return 是否需要播放提示音
     */
    private boolean needPlay(){
        long mills = System.currentTimeMillis();
        if (mills - mPlayMills < 1500){
            return false;
        }
        mPlayMills = mills;
        return true;
    }
}
