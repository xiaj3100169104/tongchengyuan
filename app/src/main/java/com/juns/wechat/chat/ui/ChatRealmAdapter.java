package com.juns.wechat.chat.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juns.wechat.R;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.bean.MessageObject;
import com.juns.wechat.config.MsgType;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmModel;

/**
 * xiajun
 */
public class ChatRealmAdapter extends BaseRecyclerViewAdapter<MessageObject> {

    private static final int MSG_TYPE_TEXT_LEFT = 0;
    private static final int MSG_TYPE_TEXT_RIGHT = 1;
    private static final int MSG_TYPE_PICTURE_LEFT = 2;
    private static final int MSG_TYPE_PICTURE_RIGHT = 3;
    private static final int MSG_TYPE_VOICE_LEFT = 4;
    private static final int MSG_TYPE_VOICE_RIGHT = 5;
    private static final int MSG_TYPE_OFFLINE_VIDEO_LEFT = 6;
    private static final int MSG_TYPE_OFFLINE_VIDEO_RIGHT = 7;
    private static final int MSG_TYPE_LOCATION_LEFT = 8;
    private static final int MSG_TYPE_LOCATION_RIGHT = 9;

    public ChatRealmAdapter(Context context){
        super(context, new ArrayList<MessageObject>());
    }

    public ChatRealmAdapter(Context context, List<MessageObject> dataList) {
        super(context, dataList);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = -1;
        MessageObject messageObject = list.get(position);
        int direction = messageObject.getDirection();
        switch (messageObject.getType()) {
            case MsgType.MSG_TYPE_TEXT:
                if (direction == MessageBean.Direction.INCOMING.value)
                    viewType = MSG_TYPE_TEXT_LEFT;
                else
                    viewType = MSG_TYPE_TEXT_RIGHT;
                break;
            case MsgType.MSG_TYPE_PICTURE:
                if (direction == MessageBean.Direction.INCOMING.value)
                    viewType = MSG_TYPE_PICTURE_LEFT;
                else
                    viewType = MSG_TYPE_PICTURE_RIGHT;
                break;
            case MsgType.MSG_TYPE_VOICE:
                if (direction == MessageBean.Direction.INCOMING.value)
                    viewType = MSG_TYPE_VOICE_LEFT;
                else
                    viewType = MSG_TYPE_VOICE_RIGHT;
                break;
            case MsgType.MSG_TYPE_OFFLINE_VIDEO:
                if (direction == MessageBean.Direction.INCOMING.value)
                    viewType = MSG_TYPE_OFFLINE_VIDEO_LEFT;
                else
                    viewType = MSG_TYPE_OFFLINE_VIDEO_RIGHT;
                break;
            case MsgType.MSG_TYPE_LOCATION:
                if (direction == MessageBean.Direction.INCOMING.value)
                    viewType = MSG_TYPE_LOCATION_LEFT;
                else
                    viewType = MSG_TYPE_LOCATION_RIGHT;
                break;
        }
        return viewType;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public BaseRealMsgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseRealMsgViewHolder holder = null;
        switch (viewType) {
            case MSG_TYPE_TEXT_LEFT:
                holder = new ViewHolderRealmTextLeft(getView(R.layout.chat_item_received_message, parent));
                break;
            case MSG_TYPE_TEXT_RIGHT:
                holder = new ViewHolderRealmTextRight(getView(R.layout.chat_item_sent_message, parent));
                break;
            default:
                holder = new ViewHolderRealmTextRight(getView(R.layout.chat_item_sent_message, parent));
        }

        holder.setContext(mContext, parent);
        return holder;
    }

    private View getView(int resId, ViewGroup parent) {
        return mInflater.inflate(resId, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        MessageObject messageObject = list.get(position);
        BaseRealMsgViewHolder baseMsgViewHolder = (BaseRealMsgViewHolder) viewHolder;
        baseMsgViewHolder.setData(messageObject, list, position);
        switch (getItemViewType(position)) {
            case MSG_TYPE_TEXT_LEFT:
                ViewHolderRealmTextLeft viewHolderTextLeft = (ViewHolderRealmTextLeft) viewHolder;
                viewHolderTextLeft.updateView();
                break;
            case MSG_TYPE_TEXT_RIGHT:
                ViewHolderRealmTextRight viewHolderTextRight = (ViewHolderRealmTextRight) viewHolder;
                viewHolderTextRight.updateView();
                break;
     /*       case MSG_TYPE_PICTURE_LEFT:
                ViewHolderPictureLeft viewHolderPictureLeft = (ViewHolderPictureLeft) viewHolder;
                viewHolderPictureLeft.updateView();
                break;
            case MSG_TYPE_PICTURE_RIGHT:
                ViewHolderPictureRight viewHolderPictureRight = (ViewHolderPictureRight) viewHolder;
                viewHolderPictureRight.updateView();
                break;
            case MSG_TYPE_VOICE_LEFT:
                ViewHolderVoiceLeft viewHolderVoiceLeft = (ViewHolderVoiceLeft) viewHolder;
                viewHolderVoiceLeft.updateView();
                break;
            case MSG_TYPE_VOICE_RIGHT:
                ViewHolderVoiceRight viewHolderVoiceRight = (ViewHolderVoiceRight) viewHolder;
                viewHolderVoiceRight.updateView();
                break;
            case MSG_TYPE_OFFLINE_VIDEO_LEFT:
                ViewHolderOfflineVideoLeft viewHolderVideoLeft = (ViewHolderOfflineVideoLeft) viewHolder;
                viewHolderVideoLeft.updateView();
                break;
            case MSG_TYPE_OFFLINE_VIDEO_RIGHT:
                ViewHolderOfflineVideoRight viewHolderVideoRight = (ViewHolderOfflineVideoRight) viewHolder;
                viewHolderVideoRight.updateView();
                break;
            case MSG_TYPE_LOCATION_LEFT:
                ViewHolderLocationLeft viewHolderLocationLeft = (ViewHolderLocationLeft) viewHolder;
                viewHolderLocationLeft.updateView();
                break;
            case MSG_TYPE_LOCATION_RIGHT:
                ViewHolderLocationRight viewHolderLocationRight = (ViewHolderLocationRight) viewHolder;
                viewHolderLocationRight.updateView();
                break;*/
        }
    }
}
