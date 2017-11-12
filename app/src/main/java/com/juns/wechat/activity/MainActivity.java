package com.juns.wechat.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juns.wechat.adpter.MainAdapter;
import com.juns.wechat.bean.DynamicBean;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.bean.MessageBean;
import com.juns.wechat.chat.xmpp.event.XmppEvent;
import com.juns.wechat.fragment.Fragment_Discover;
import com.juns.wechat.fragment.Fragment_Friends;
import com.juns.wechat.fragment.msg.Fragment_Msg;
import com.juns.wechat.helper.CommonViewHelper;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.service.XmppService;
import com.juns.wechat.util.LogUtil;
import com.same.city.love.R;
import com.style.event.EventCode;
import com.style.base.BaseActivity;
import com.style.utils.StringUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNickName)
    TextView tvNickName;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.layout_left_menu_1)
    LinearLayout layoutLeftMenu1;
    @Bind(R.id.layout_left_menu_2)
    LinearLayout layoutLeftMenu2;
    @Bind(R.id.layout_left_menu_3)
    LinearLayout layoutLeftMenu3;
    @Bind(R.id.layout_left_menu_4)
    LinearLayout layoutLeftMenu4;
    @Bind(R.id.layout_left_menu_5)
    LinearLayout layoutLeftMenu5;
    @Bind(R.id.layout_left_menu_6)
    LinearLayout layoutLeftMenu6;
    @Bind(R.id.layout_left_menu_7)
    LinearLayout layoutLeftMenu7;
    private ViewPager vpMainContent;
    private TextView unreaMsgdLabel;// 未读消息textview
    private TextView unreadAddressLable;// 未读通讯录textview
    private ImageView[] imagebuttons;
    private TextView[] textviews;
    private int index;

    private MainAdapter mainAdapter;
    DrawerLayout drawer;
    private Fragment_Msg fragmentMsg;
    private Fragment_Friends fragmentFriends;
    private Fragment_Discover fragmentDicover;

    @Override
    protected boolean isWrapContentView() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_main2;
        super.onCreate(savedInstanceState);


        /*List<MessageBean> friendBeanList = MessageDao.getInstance().findAllByParams(WhereBuilder.b());
        for (MessageBean f : friendBeanList) {
            Log.e(TAG, f.toString());
            if (TextUtils.isEmpty(f.getOtherName())){
                MessageDao.getInstance().deleteOne(f);
            }
        }*/
       /* List<FriendBean> friendBeanList = FriendDao.getInstance().findAllByParams(WhereBuilder.b());
        for (FriendBean f : friendBeanList) {
            Log.e(TAG, f.toString());
        }*/
      /*  List<UserBean> userList = UserDao.getInstance().findAllByParams(WhereBuilder.b());

        long i = 18202820000l;
        for (UserBean u : userList) {
            Log.e(TAG, u.toString());
            if (TextUtils.isEmpty(u.getUserName())) {
                u.setUserName(i + "");
            }
            UserDao.getInstance().update(u);
            i++;

        }*/

    }

    @Override
    public void initData() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initView();
        XmppService.login(this);

        int userId = Process.myUid();

        //new Watcher(this).createAppMonitor(userId + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initView() {

        unreaMsgdLabel = (TextView) findViewById(R.id.tv_unread_msg_number);
        unreadAddressLable = (TextView) findViewById(R.id.unread_contact_number);

        imagebuttons = new ImageView[3];
        imagebuttons[0] = (ImageView) findViewById(R.id.ib_weixin);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_contact_list);
        imagebuttons[2] = (ImageView) findViewById(R.id.ib_find);

        textviews = new TextView[3];
        textviews[0] = (TextView) findViewById(R.id.tv_weixin);
        textviews[1] = (TextView) findViewById(R.id.tv_contact_list);
        textviews[2] = (TextView) findViewById(R.id.tv_find);

        vpMainContent = (ViewPager) findViewById(R.id.vp_main_content);
        List<Fragment> fragments = new ArrayList<>();
        fragmentMsg = new Fragment_Msg();
        fragmentFriends = new Fragment_Friends();
        fragmentDicover = new Fragment_Discover();
        fragments.add(fragmentMsg);
        fragments.add(fragmentFriends);
        fragments.add(fragmentDicover);

        mainAdapter = new MainAdapter(getSupportFragmentManager(), fragments);
        vpMainContent.setOffscreenPageLimit(3);
        vpMainContent.setAdapter(mainAdapter);
        vpMainContent.addOnPageChangeListener(pageChangeListener);

        setSelectedIndex(0);

        updateUserView();
    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.re_weixin:
                index = 0;
                break;
            case R.id.re_contact_list:
                index = 1;
                break;
            case R.id.re_find:
                index = 2;
                break;
        }
        setSelectedIndex(index);
        vpMainContent.setCurrentItem(index, false);
    }

    private void setSelectedIndex(int index) {
        for (int i = 0; i < textviews.length; i++) {
            if (i != index) {
                textviews[i].setSelected(false);
                imagebuttons[i].setSelected(false);
            } else {
                textviews[i].setSelected(true);
                imagebuttons[i].setSelected(true);
            }
        }

        switch (index) {
            case 0:
                setToolbarTitle(R.string.chat);

                break;
            case 1:
                setToolbarTitle(R.string.contacts);
                break;
            case 2:
                setToolbarTitle(R.string.discover);

                break;
        }
    }

    public void setToolbarTitle(int resId) {
        setTitle(resId);
    }

    @Subscriber(tag = EventCode.UPDATE_USER_HEAD)
    private void onDbDataChanged(UserBean event) {
        updateUserView();
    }

    private void updateUserView() {
        UserBean curUser = AccountManager.getInstance().getUser();
        CommonViewHelper.setUserViewInfo(curUser, ivAvatar, tvNickName, null, tvUserName, true);

    }

    @Subscriber(tag = "tagLogout")
    public void onDbDataChanged(XmppEvent event) {
        if (event.resultCode == XmppEvent.LOGOUT) {
            logout();
        }
    }

    public void logout() {
        Intent service = new Intent(this, XmppService.class);
        stopService(service);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void setUnreadMsgLabel(int unreadNum) {
        if (unreadNum > 0) {
            unreaMsgdLabel.setText(unreadNum + "");
            unreaMsgdLabel.setVisibility(View.VISIBLE);
        } else {
            unreaMsgdLabel.setVisibility(View.GONE);
        }
    }

    public void setUnreadInviteMsgLabel(int unreadNum) {
        if (unreadNum > 0) {
            unreadAddressLable.setText(unreadNum + "");
            unreadAddressLable.setVisibility(View.VISIBLE);
        } else {
            unreadAddressLable.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
        }
    }

    private boolean isDrawerOpen() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            return true;
        }
        return false;
    }

    private void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setSelectedIndex(position);
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @OnClick(R.id.layout_left_menu_header)
    public void onClickEvent() {
        startActivity(new Intent(this, PersonInfoShowActivity.class));
    }

    @OnClick(R.id.layout_left_menu_1)
    public void onClickEvent1() {
        //skip(EditBaseInfoActivity.class);
    }

    @OnClick(R.id.layout_left_menu_2)
    public void onClickEvent2() {
        //skip(EditBaseInfoActivity.class);
    }

    @OnClick(R.id.layout_left_menu_3)
    public void onClickEvent3() {
        //skip(EditBaseInfoActivity.class);
    }

    @OnClick(R.id.layout_left_menu_4)
    public void onClickEvent4() {
        //  skip(EditBaseInfoActivity.class);
    }

    @OnClick(R.id.layout_left_menu_5)
    public void onClickEvent5() {
        skip(MyQRCodeActivity.class);
    }

    @OnClick(R.id.layout_left_menu_6)
    public void onClickEvent6() {
        skip(QRScanActivity.class);
    }

    @OnClick(R.id.layout_left_menu_7)
    public void onClickEvent7() {
        skip(SettingActivity.class);
    }

    public void skip(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }
}