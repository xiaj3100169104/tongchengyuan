package cn.tongchengyuan.chat.xmpp.listener;

import cn.tongchengyuan.greendao.mydao.GreenDaoManager;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterLoadedListener;

/**
 * Created by 王宗文 on 2016/6/14.
 */
public class RosterLoadedListenerImpl implements RosterLoadedListener {

    private GreenDaoManager rosterDao = GreenDaoManager.getInstance();

    @Override
    public void onRosterLoaded(Roster roster) {
      /*  Set<RosterEntry> rosterEntrySet = roster.getEntries();
        if(rosterEntrySet == null || rosterEntrySet.isEmpty()) return;
        List<FriendBean> rosterBeans = new ArrayList<>();
        for(RosterEntry rosterEntry : rosterEntrySet){
            FriendBean rosterBean = new FriendBean();
            rosterBean.setOwnerName(AccountManager.getInstance().getUser().getUserName());
            rosterBean.setContactName(ConfigUtil.getUserName(rosterEntry.getUser()));
            rosterBean.setSubType(rosterEntry.getType().toString());
            rosterBean.setRemark(rosterEntry.getShowName());
            rosterBeans.add(rosterBean);
        }
        rosterDao.replace(rosterBeans);*/
    }
}
