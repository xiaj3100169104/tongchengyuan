package com.juns.wechat.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import com.juns.wechat.R;
import com.juns.wechat.adpter.PickContactAdapter;
import com.juns.wechat.annotation.Click;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.dao.FriendDao;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.LogUtil;
import com.juns.wechat.util.ThreadPoolUtil;
import com.juns.wechat.widget.SideBar;
import com.juns.wechat.xmpp.XmppGroup;
import com.juns.wechat.xmpp.XmppGroupImpl;
import com.juns.wechat.xmpp.event.XmppEvent;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.Affiliate;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

@Content(R.layout.activity_chatroom)
public class AddGroupChatActivity extends ToolbarActivity{
    @Id
    private TextView tvRightText;
	private ImageView iv_search;
	private TextView tv_header;
	private ListView listView;
	private EditText et_search;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;
	/** 是否为一个新建的群组 */
	protected boolean isCreatingNewGroup;
	/** 是否为单选 */
	private boolean isSignleChecked;
	private PickContactAdapter contactAdapter;
	/** group中一开始就有的成员 */
	private List<String> exitingMembers = new ArrayList<String>();
	private List<FriendBean> myFriends;// 好友列表
	// 可滑动的显示选中用户的View
	private LinearLayout menuLinerLayout;

	// 选中用户总数,右上角显示
	int total = 0;
	private String userId = null;
	private String groupId = null;
	private String groupname;
	// 添加的列表
	private List<String> addList = new ArrayList<String>();
	private String hxid;
	private UserBean user = AccountManager.getInstance().getUser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        initControl();
        initData();
        EventBus.getDefault().register(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
        EventBus.getDefault().unregister(this);
		mWindowManager.removeView(mDialogText);
	}

	private void initControl() {
        tvRightText.setText("确定");
		menuLinerLayout = (LinearLayout) this
				.findViewById(R.id.linearLayoutMenu);
		et_search = (EditText) this.findViewById(R.id.et_search);
		listView = (ListView) findViewById(R.id.lvMessages);
		iv_search = (ImageView) this.findViewById(R.id.iv_search);
		mDialogText = (TextView) LayoutInflater.from(this).inflate(
				R.layout.list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		indexBar = (SideBar) findViewById(R.id.sideBar);
		indexBar.setListView(listView);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View headerView = layoutInflater.inflate(R.layout.item_chatroom_header,
				null);
		tv_header = (TextView) headerView.findViewById(R.id.tv_header);
		listView.addHeaderView(headerView);
	}

	protected void initData() {
		// 获取好友列表
		myFriends = FriendDao.getInstance().getMyFriends(user.getUserName());
		contactAdapter = new PickContactAdapter(AddGroupChatActivity.this,
                myFriends);
		listView.setAdapter(contactAdapter);
	}

	protected void setListener() {
		et_search.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				/*if (s.length() > 0) {
					String str_s = et_search.getText().toString().trim();
					List<FriendBean> users_temp = new ArrayList<>();
					for (FriendBean friendBean : myFriends) {
						String usernick = user.getUserName();
						if (usernick.contains(str_s)) {
							users_temp.add(user);
						}
						contactAdapter = new PickContactAdapter(
								AddGroupChatActivity.this, users_temp);
						listView.setAdapter(contactAdapter);
					}
				} else {
					contactAdapter = new PickContactAdapter(
							AddGroupChatActivity.this, myFriends);
					listView.setAdapter(contactAdapter);
				}*/
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
			}
		});
	}



	/**
	 * 确认选择的members
	 *
	 */
    @Click(viewId = R.id.tvRightText)
	public void save(View v) {
        if (isCreatingNewGroup) {
            getLoadingDialog("正在创建群聊...").show();
        } else {
            getLoadingDialog("正在加人...").show();
        }
        creatNewGroup();// 创建群组

	}

    @Subscriber
    private void onCreateGroupFinish(XmppEvent event){
        if(event.getResultCode() == XmppEvent.CREATE_GROUP_SUCCESS || event.getResultCode() == XmppEvent.CREATE_GROUP_FAILED){
            getLoadingDialog("正在加人...").dismiss();
        }
    }

	/**
	 * 创建新群组
	 * 
	 * @param newmembers
	 */
	String groupName = "";
	String manber = "";

    XmppGroup xmppGroup = XmppGroupImpl.getInstance();

	private void creatNewGroup() {
        final List<FriendBean> selectedFriends = contactAdapter.getSelectedFriends();

        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                boolean create = xmppGroup.createOrJoinGroup("room1", user.getShowName());
                if(create){
                    String jid = selectedFriends.get(0).getContactName() + "@wangzhe";
                    xmppGroup.inviteUser("room1", jid, "邀请你加入群聊");
                    MultiUserChat multiUserChat = xmppGroup.getMultiUserChat("room1");
                    List<Affiliate> affiliates = null;
                    try {
                        affiliates = multiUserChat.getMembers();
                        for(Affiliate affiliate : affiliates){
                            LogUtil.i("role: " + affiliate.getRole());
                            LogUtil.i("affilation: " + affiliate.getAffiliation());
                        }
                    } catch (SmackException.NoResponseException e) {
                        e.printStackTrace();
                    } catch (XMPPException.XMPPErrorException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


}
