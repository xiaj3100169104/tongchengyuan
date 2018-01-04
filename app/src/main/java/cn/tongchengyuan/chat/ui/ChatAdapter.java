package cn.tongchengyuan.chat.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import com.same.city.love.R;

import cn.tongchengyuan.chat.bean.MessageBean;

import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * xiajun
 */
public class ChatAdapter extends BaseRecyclerViewAdapter<MessageBean> {
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

    public ChatAdapter(Context context, List<MessageBean> list) {
        super(context, list);
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = -1;
        MessageBean msg = list.get(position);
        int direction = msg.getDirection();
        switch (msg.getType()) {
            case MessageBean.Type.TEXT:
                if (direction == MessageBean.Direction.INCOMING)
                    viewType = MSG_TYPE_TEXT_LEFT;
                else
                    viewType = MSG_TYPE_TEXT_RIGHT;
                break;
            case MessageBean.Type.PICTURE:
                if (direction == MessageBean.Direction.INCOMING)
                    viewType = MSG_TYPE_PICTURE_LEFT;
                else
                    viewType = MSG_TYPE_PICTURE_RIGHT;
                break;
            case MessageBean.Type.VOICE:
                if (direction == MessageBean.Direction.INCOMING)
                    viewType = MSG_TYPE_VOICE_LEFT;
                else
                    viewType = MSG_TYPE_VOICE_RIGHT;
                break;
            case MessageBean.Type.OFFLINE_VIDEO:
                if (direction == MessageBean.Direction.INCOMING)
                    viewType = MSG_TYPE_OFFLINE_VIDEO_LEFT;
                else
                    viewType = MSG_TYPE_OFFLINE_VIDEO_RIGHT;
                break;
            case MessageBean.Type.LOCATION:
                if (direction == MessageBean.Direction.INCOMING)
                    viewType = MSG_TYPE_LOCATION_LEFT;
                else
                    viewType = MSG_TYPE_LOCATION_RIGHT;
                break;
        }
        return viewType;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseMsgViewHolder holder = null;
        switch (viewType) {
            case MSG_TYPE_TEXT_LEFT:
                holder = new ViewHolderTextLeft(getView(R.layout.chat_item_received_message, parent));
                break;
            case MSG_TYPE_TEXT_RIGHT:
                holder = new ViewHolderTextRight(getView(R.layout.chat_item_sent_message, parent));
                break;
            case MSG_TYPE_PICTURE_LEFT:
                holder = new ViewHolderPictureLeft(getView(R.layout.chat_item_received_picture, parent));
                break;
            case MSG_TYPE_PICTURE_RIGHT:
                holder = new ViewHolderPictureRight(getView(R.layout.chat_item_sent_picture, parent));
                break;
            case MSG_TYPE_VOICE_LEFT:
                holder = new ViewHolderVoiceLeft(getView(R.layout.chat_item_received_voice, parent));
                break;
            case MSG_TYPE_VOICE_RIGHT:
                holder = new ViewHolderVoiceRight(getView(R.layout.chat_item_sent_voice, parent));
                break;
            case MSG_TYPE_OFFLINE_VIDEO_LEFT:
                holder = new ViewHolderOfflineVideoLeft(getView(R.layout.chat_item_received_video, parent));
                break;
            case MSG_TYPE_OFFLINE_VIDEO_RIGHT:
                holder = new ViewHolderOfflineVideoRight(getView(R.layout.chat_item_sent_video, parent));
                break;
            case MSG_TYPE_LOCATION_LEFT:
                holder = new ViewHolderLocationLeft(getView(R.layout.chat_item_received_location, parent));
                break;
            case MSG_TYPE_LOCATION_RIGHT:
                holder = new ViewHolderLocationRight(getView(R.layout.chat_item_sent_location, parent));
                break;
        }
        holder.setContext(mContext, parent);
        return holder;
    }

    private View getView(int resId, ViewGroup parent) {
        return mInflater.inflate(resId, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        MessageBean messageBean = getData(position);
        BaseMsgViewHolder baseMsgViewHolder = (BaseMsgViewHolder) viewHolder;
        baseMsgViewHolder.setData(messageBean, list, position);
        switch (getItemViewType(position)) {
            case MSG_TYPE_TEXT_LEFT:
                ViewHolderTextLeft viewHolderTextLeft = (ViewHolderTextLeft) viewHolder;
                viewHolderTextLeft.updateView();
                break;
            case MSG_TYPE_TEXT_RIGHT:
                ViewHolderTextRight viewHolderTextRight = (ViewHolderTextRight) viewHolder;
                viewHolderTextRight.updateView();
                break;
            case MSG_TYPE_PICTURE_LEFT:
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
                break;
        }
    }
}
