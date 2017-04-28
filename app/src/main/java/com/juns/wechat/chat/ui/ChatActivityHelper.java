package com.juns.wechat.chat.ui;

import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.bean.MessageObject;
import com.juns.wechat.database.dao.DbDataEvent;
import com.juns.wechat.database.dao.MessageDao;
import com.juns.wechat.database.dao.MessageObjDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.realm.RealmHelper;
import com.juns.wechat.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by 王者 on 2016/8/8.
 */
public class ChatActivityHelper {
    private ChatActivity chatActivity;

    private static final int SIZE = 10;  //一次最多查询10条数据

    private String myselfName;
    private MessageDao messageDao;
    private MessageObjDao messageObjDao;
    private String otherName;
    private int mFromIndex; //从该位置开始查询
    private int mEndIndex;
    private Realm realm;
    private RealmResults<MessageObject> realmResults;
    private List<MessageObject> dataList = new ArrayList<>();
    private boolean mHaveMoreData = true;
    private RealmChangeListener<RealmResults<MessageObject>> resultsRealmChangeListener =
           new MyRealmChangeListener();

    private UserBean account = AccountManager.getInstance().getUser();


    public ChatActivityHelper(ChatActivity chatActivity, String contactName) {
        this.chatActivity = chatActivity;
        this.myselfName = account.getUserName();
        this.otherName = contactName;
        messageDao = MessageDao.getInstance();
    }

    public void onCreate() {
        realm = RealmHelper.getIMInstance();
        messageObjDao = MessageObjDao.getInstance();
        initQueryIndex(myselfName, otherName);
        markAsRead(myselfName, otherName);
    }

    private void initQueryIndex(String myselfName, String otherName) {
        //结果是按升序排列的。
        realmResults = messageObjDao.getMessagesBetweenTwoUsers(realm, myselfName, otherName);
        int count = realmResults.size();
        mFromIndex = count;
        mEndIndex = count;
        realmResults.addChangeListener(resultsRealmChangeListener);
    }

    private List<MessageBean> getMessagesByIndexAndSize() {
        return messageDao.getMessagesByIndexAndSize(myselfName, otherName, mFromIndex, SIZE);
    }

    public void loadMessagesFromDb() {
        //取出来结果是按升序排列的


      /*  List<MessageBean> list = getMessagesByIndexAndSize();
        if (list == null || list.isEmpty()) {
            notifyActivityDataSetChanged(false);
            return;
        }
        mFromIndex = mFromIndex - list.size();

        addEntityToViewModel(chatActivity.getMsgViewModels(), list);*/
        if(!mHaveMoreData){
            LogUtil.w("no more data can be fetched!");
            return;
        }

        mFromIndex = mFromIndex - SIZE > 0 ? mFromIndex - SIZE : 0;
        LogUtil.i("mFromIndex: " + mFromIndex + ", mEndIndex: " + mEndIndex);
        for(int i = mFromIndex; i < mEndIndex ; i++){
            dataList.add(realmResults.get(i));
        }
        if(mFromIndex == 0){ //no more data!
           mHaveMoreData = false;
        }

        LogUtil.i("dataList: " + dataList.size());
        notifyActivityDataSetChanged(true, dataList);
    }

    private void notifyActivityDataSetChanged(boolean hasNewData, List<MessageObject> dataList) {
        chatActivity.loadDataComplete(hasNewData, dataList);
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

    /**
     * 处理一条消息，在观察到一条数据变化时调用。下拉刷新出来的数据不应该调用此方法
     *
     * @param entity
     */
    public void processOneMessage(List<MessageBean> msgViewModels, MessageBean entity, int action) {
        int position = -1;
        for (int i = 0; i < msgViewModels.size(); i++) {
            MessageBean viewModel = msgViewModels.get(i);
            if (entity.getId() == viewModel.getId()) {
                position = i;
                break;
            }
        }
        switch (action) {
            case DbDataEvent.SAVE:
                addEntityToViewModel(msgViewModels, entity);
                Collections.sort(msgViewModels, new MessageBeanComparator());
                chatActivity.refreshOneData(true);
                break;
            case DbDataEvent.UPDATE:
                if (position != -1) {
                    msgViewModels.remove(position);
                }
                addEntityToViewModel(msgViewModels, entity);
                Collections.sort(msgViewModels, new MessageBeanComparator());
                chatActivity.refreshOneData(true);
                break;
            case DbDataEvent.DELETE_ONE:
                if (position != -1) {
                    msgViewModels.remove(position);
                }
                chatActivity.refreshOneData(false);
                break;
            default:
                break;
        }
    }

    /**
     * 将未读消息状态置为已读状态
     *
     * @param otherName
     */
    public void markAsRead(final String myselfName, final String otherName) {
      /*  ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                messageDao.markAsRead(myselfName, otherName);
            }
        });*/
        messageObjDao.markAsRead(realm, myselfName, otherName);
    }

    public void onDestroy() {
        chatActivity = null;
        realm.close();
    }

    public int getQueryIndex() {
        return mFromIndex;
    }

    public void setQueryIndex(int mQueryIndex) {
        this.mFromIndex = mQueryIndex;
    }

    class MyRealmChangeListener implements RealmChangeListener<RealmResults<MessageObject>>{
        @Override
        public void onChange(RealmResults<MessageObject> realmResults) {
           if(mEndIndex < realmResults.size()){
               for(int i = mEndIndex ; i < realmResults.size() ; i++){
                   dataList.add(realmResults.get(i));
                   mEndIndex++;
               }
           }else if(mEndIndex == realmResults.size()){
               for(int i = 0 ; i < dataList.size() ; i++){
                   if(!dataList.get(i).equals(realmResults.get(mFromIndex + i))){
                       dataList.set(i, realmResults.get(mFromIndex + i));
                   }
               }
               LogUtil.i("some data changed!");
           }else {  //some data is deleted!
               Iterator<MessageObject> iterator = dataList.iterator();
               int realmResultsCount = realmResults.size();
               int i = 0;
               while (iterator.hasNext()){
                   if(mFromIndex + i > realmResultsCount - 1){
                       break;
                   }
                   MessageObject messageObject = iterator.next();
                   if(!messageObject.equals(realmResults.get(mFromIndex + i))){
                       iterator.remove();
                   }
                   i++;
               }
           }
           notifyActivityDataSetChanged(true, dataList);
        }
    }
}
