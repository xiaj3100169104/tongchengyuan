package cn.tongchengyuan.chat.ui;

import cn.tongchengyuan.chat.bean.MessageBean;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.util.ThreadPoolUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by 王者 on 2016/8/8.
 */
public class ChatActivityHelper {
    private ChatActivity chatActivity;

    private static final int SIZE = 10;  //一次最多查询10条数据

    private String myselfName;
    private GreenDaoManager messageDao;
    private String otherName;
    private int mQueryIndex; //从该位置开始查询

    private UserBean account = AccountManager.getInstance().getUser();
    private int position;


    public ChatActivityHelper(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
        this.myselfName = account.getUserId();
        this.otherName = chatActivity.getContactUserId();
        messageDao = GreenDaoManager.getInstance();
    }

    public void onCreate() {
        initQueryIndex(myselfName, otherName);
        markAsRead(myselfName, otherName);
    }

    private void initQueryIndex(String myselfName, String otherName) {
        int size = messageDao.getMessageCount(myselfName, otherName);
        mQueryIndex = size > SIZE ? size - SIZE : 0;
    }

    private List<MessageBean> getMessagesByIndexAndSize() {
        return messageDao.getMessagesByIndexAndSize(myselfName, otherName, mQueryIndex, SIZE);
    }

    public void loadMessagesFromDb() {
        List<MessageBean> list = getMessagesByIndexAndSize();
        if (list == null || list.isEmpty()) {
            notifyActivityDataSetChanged(false);
            return;
        }
        mQueryIndex = mQueryIndex - list.size();

        addEntityToViewModel(chatActivity.getMsgViewModels(), list);
        notifyActivityDataSetChanged(true);
    }

    private void notifyActivityDataSetChanged(boolean hasNewData) {
        chatActivity.loadDataComplete(hasNewData);
    }

    /**
     * 由于消息是从上向下展示，下拉刷新查询出消息应该放在数据链表头部
     *
     * @param messageEntities
     */
    private void addEntityToViewModel(List<MessageBean> msgViewModels, List<MessageBean> messageEntities) {
        int size = messageEntities.size();
        for (int i = size - 1; i >= 0; i--) {
            MessageBean entity = messageEntities.get(i);
            addEntityToViewModel(msgViewModels, entity);
        }
        Collections.sort(msgViewModels, new MessageBeanComparator());
    }

    private void addEntityToViewModel(List<MessageBean> msgViewModels, MessageBean entity) {
        if (entity == null) return;
        msgViewModels.add(entity);
    }

    public int getPosition(List<MessageBean> msgViewModels, MessageBean entity) {
        int position = -1;
        for (int i = 0; i < msgViewModels.size(); i++) {
            MessageBean viewModel = msgViewModels.get(i);
            if (entity.getPacketId().equals(viewModel.getPacketId())) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 处理一条消息，在观察到一条数据变化时调用。下拉刷新出来的数据不应该调用此方法
     *
     * @param entity
     */
    public void OnMessageBeforeSendSuccess(List<MessageBean> msgViewModels, MessageBean entity) {
        int position = getPosition(msgViewModels, entity);
        addEntityToViewModel(msgViewModels, entity);
        Collections.sort(msgViewModels, new MessageBeanComparator());
        chatActivity.refreshOneData(true);
    }

    public void onMessageRefreshOne(List<MessageBean> msgViewModels, MessageBean entity) {
        int position = getPosition(msgViewModels, entity);
        if (position != -1) {
            msgViewModels.remove(position);
        }
        addEntityToViewModel(msgViewModels, entity);
        Collections.sort(msgViewModels, new MessageBeanComparator());
        chatActivity.refreshOneData(true);
    }

    public void onMessageDeleteOne(List<MessageBean> msgViewModels, MessageBean entity) {
        int position = getPosition(msgViewModels, entity);
        if (position != -1) {
            msgViewModels.remove(position);
        }
        chatActivity.refreshOneData(false);
    }

    /**
     * 将未读消息状态置为已读状态
     *
     * @param otherName
     */
    public void markAsRead(final String myselfName, final String otherName) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                messageDao.markAsRead(myselfName, otherName);
            }
        });
    }

    public void onDestroy() {
        chatActivity = null;
    }

    public int getQueryIndex() {
        return mQueryIndex;
    }

    public void setQueryIndex(int mQueryIndex) {
        this.mQueryIndex = mQueryIndex;
    }

}
