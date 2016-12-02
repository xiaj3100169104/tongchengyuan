package com.juns.wechat.helper;

import com.juns.wechat.activity.ChatActivity;
import com.juns.wechat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.bean.chat.viewmodel.MsgViewModel;
import com.juns.wechat.bean.chat.viewmodel.PictureMsgViewModel;
import com.juns.wechat.bean.chat.viewmodel.TextMsgViewModel;
import com.juns.wechat.bean.chat.viewmodel.VoiceMsgViewModel;
import com.juns.wechat.config.MsgType;
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.dao.MessageDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ThreadPoolUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by 王者 on 2016/8/8.
 */
public class ChatActivityHelper {
    private ChatActivity chatActivity;

    private static final int SIZE = 10;  //一次最多查询10条数据

    private String myselfName;
    private MessageDao messageDao;
    private String otherName;
    private int mQueryIndex; //从该位置开始查询

    private UserBean account = AccountManager.getInstance().getUser();


    public ChatActivityHelper(ChatActivity chatActivity){
        this.chatActivity = chatActivity;
        this.myselfName = account.getUserName();
        this.otherName = chatActivity.getContactName();
        messageDao = MessageDao.getInstance();
    }

    public void onCreate(){
        initQueryIndex(myselfName, otherName);
        markAsRead(myselfName, otherName);
    }

    private void initQueryIndex(String myselfName, String otherName){
        int size = messageDao.getMessageCount(myselfName, otherName);
        mQueryIndex = size > SIZE ? size - SIZE : 0;
    }

    private List<MessageBean> getMessagesByIndexAndSize(){
        return messageDao.getMessagesByIndexAndSize(myselfName, otherName, mQueryIndex, SIZE);
    }

    public void loadMessagesFromDb(){
        List<MessageBean> messageBeen = getMessagesByIndexAndSize();
        if(messageBeen == null || messageBeen.isEmpty()) {
            notifyActivityDataSetChanged(false);
            return;
        }

        mQueryIndex = mQueryIndex - messageBeen.size();

        List<MsgViewModel> msgViewModels = chatActivity.getMsgViewModels();
        addEntityToViewModel(msgViewModels, messageBeen);

        notifyActivityDataSetChanged(true);
    }

    private void notifyActivityDataSetChanged(boolean hasNewData){
        chatActivity.loadDataComplete(hasNewData);
    }

    /**
     * 由于消息是从上向下展示，下拉刷新查询出消息应该放在数据链表头部
     * @param messageEntities
     */
    private void addEntityToViewModel(List<MsgViewModel> msgViewModels, List<MessageBean> messageEntities){
        int size = messageEntities.size();
        for(int i = size - 1; i >= 0 ; i--){
            MessageBean entity = messageEntities.get(i);
            addEntityToViewModel(msgViewModels, entity);
        }
        Collections.sort(msgViewModels);  //要将消息重新排一次序
    }

    private void addEntityToViewModel(List<MsgViewModel> msgViewModels,MessageBean entity){
        if(entity == null) return;
        int type = entity.getType();
        MsgViewModel viewModel = null;
        UserBean contactUser = chatActivity.getContactUser();
        switch (type){
            case MsgType.MSG_TYPE_TEXT:
                viewModel = new TextMsgViewModel(chatActivity, entity);
                break;
            case MsgType.MSG_TYPE_PICTURE:
                viewModel = new PictureMsgViewModel(chatActivity, entity);
                break;
            case MsgType.MSG_TYPE_VOICE:
                viewModel = new VoiceMsgViewModel(chatActivity, entity);
                break;
            default:
                break;
        }
        if(viewModel != null){
            viewModel.setInfo(account.getUserName(), account.getHeadUrl(), contactUser.getUserName(), contactUser.getHeadUrl());
            viewModel.markAsRead();
            msgViewModels.add(viewModel);
        }
    }

    /**
     * 处理一条消息，在观察到一条数据变化时调用。下拉刷新出来的数据不应该调用此方法
     * @param entity
     */
    public void processOneMessage(List<MsgViewModel> msgViewModels, MessageBean entity, int action){
        int position = -1;
        for(int i = 0 ; i < msgViewModels.size() ; i++){
            MsgViewModel viewModel = msgViewModels.get(i);
            if(entity.getId() == viewModel.getId()){
                position = i;
                break;
            }
        }
        switch (action){
            case DbDataEvent.SAVE:
                addEntityToViewModel(msgViewModels, entity);
                Collections.sort(msgViewModels);
                chatActivity.refreshOneData(true);
            break;
            case DbDataEvent.UPDATE:
                if(position != -1){
                    msgViewModels.remove(position);
                }
                addEntityToViewModel(msgViewModels, entity);
                Collections.sort(msgViewModels);
                chatActivity.refreshOneData(true);
                break;
            default:
                break;
        }
    }


    /**
     * 将未读消息状态置为已读状态
     * @param otherName
     */
    public void markAsRead(final String myselfName, final String otherName){
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                messageDao.markAsRead(myselfName, otherName);
            }
        });
    }

    public void onDestroy(){
        chatActivity = null;
    }

    public int getQueryIndex() {
        return mQueryIndex;
    }

    public void setQueryIndex(int mQueryIndex) {
        this.mQueryIndex = mQueryIndex;
    }
}
