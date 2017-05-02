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

    public void loadMessagesFromDb(Action action) {
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
        notifyActivityDataSetChanged(action, dataList);
    }

    private void notifyActivityDataSetChanged(Action action, List<MessageObject> dataList) {
        chatActivity.notifyDataSetChanged(action, dataList);
    }

    public boolean isHaveMoreData() {
        return mHaveMoreData;
    }

    /**
     * 将未读消息状态置为已读状态
     *
     * @param otherName
     */
    public void markAsRead(final String myselfName, final String otherName) {
        messageObjDao.markAsRead(realm, myselfName, otherName);
    }

    public void onDestroy() {
        chatActivity = null;
        realm.close();
    }

    public enum Action{
        FIRST_LOAD(0), LOAD_MORE(1), DATA_INSERT(2), DATA_UPDATE(3), DATA_DELETE(4);

        Action(int i){

        }
    }

    class MyRealmChangeListener implements RealmChangeListener<RealmResults<MessageObject>>{
        @Override
        public void onChange(RealmResults<MessageObject> realmResults) {
           if(mEndIndex < realmResults.size()){
               for(int i = mEndIndex ; i < realmResults.size() ; i++){
                   dataList.add(realmResults.get(i));
                   mEndIndex++;
               }
               notifyActivityDataSetChanged(Action.DATA_INSERT, dataList);
           }else if(mEndIndex == realmResults.size()){
               for(int i = 0 ; i < dataList.size() ; i++){
                   if(!dataList.get(i).equals(realmResults.get(mFromIndex + i))){
                       dataList.set(i, realmResults.get(mFromIndex + i));
                   }
               }
               notifyActivityDataSetChanged(Action.DATA_UPDATE, dataList);
           }else {  //数据被删除了
               int realmResultsCount = realmResults.size();
               if(realmResultsCount == 0){
                   dataList.clear();
               }else {
                   Iterator<MessageObject> iterator = dataList.iterator();
                   while (iterator.hasNext()){
                       MessageObject messageObject = iterator.next();
                       if(!realmResults.contains(messageObject)){
                           iterator.remove();
                       }
                   }
               }
               //realResults 集合数组下标项已经改变，需要重新计算EndIndex和FromIndex.
               mEndIndex = realmResultsCount;
               mFromIndex = realmResultsCount - dataList.size();

               notifyActivityDataSetChanged(Action.DATA_DELETE, dataList);
           }
        }
    }
}
