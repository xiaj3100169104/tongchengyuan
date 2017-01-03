package com.juns.wechat.chat.im;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;


import com.juns.wechat.adpter.ListDataAdapter;
import com.juns.wechat.bean.chat.viewmodel.MsgViewModel;
import com.juns.wechat.config.MsgType;

/**
 * ****************************************************
 * Created by 王者 on 2015/6/18
 * *****************************************************
 */
public class ChatAdapter extends ListDataAdapter<MsgViewModel> {

    public ChatAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        setPrevMsgTime(i);
        return listData.get(i).fillView(i, view, viewGroup);
    }

    /**
     * 设置上一条消息时间，用来判断该条消息是否显示时间
     * @param index
     */
    private void setPrevMsgTime(int index){
        if (0 == index){
            listData.get(index).setPrevMsgTime(0);
        }else{
            long preMicroSec = listData.get(index-1).getMsgTime();
            listData.get(index).setPrevMsgTime(preMicroSec);
        }
    }

    @Override
    public int getItemViewType(int position){
        MsgViewModel msgView = listData.get(position);
        return transferViewType(msgView.getType(), msgView.isShowMyself());
    }

    @Override
    public int getViewTypeCount(){
        return 10;
    }

    private int transferViewType(int originalType, boolean isShowMySelf){
        int viewType;
        switch (originalType){
            case MsgType.MSG_TYPE_TEXT:
                viewType = 0;
                break;
            case MsgType.MSG_TYPE_VOICE:
                viewType = 2;
                break;
            case MsgType.MSG_TYPE_PICTURE:
                viewType = 4;
                break;
            case MsgType.MSG_TYPE_SEND_INVITE:
                viewType = 6;
                break;
            default:
                viewType = 8;  //未知type类型，通常出现在老版本收到新版本发过来的消息
                break;
        }
        if (isShowMySelf){
            return viewType +1;
        }
        return viewType;
    }
}
