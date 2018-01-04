package cn.tongchengyuan.activity;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.tongchengyuan.adpter.MainAdapter;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.chat.xmpp.event.XmppEvent;
import cn.tongchengyuan.fragment.Fragment_Discover;
import cn.tongchengyuan.fragment.Fragment_Friends;
import cn.tongchengyuan.fragment.msg.Fragment_Msg;
import cn.tongchengyuan.helper.CommonViewHelper;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.service.XmppService;
import com.same.city.love.R;
import com.same.city.love.databinding.ActivityMain2Binding;
import com.style.base.BaseActivity;
import com.style.event.EventCode;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    ActivityMain2Binding bd;

    private ImageView[] imagebuttons;
    private TextView[] textviews;
    private int index;

    private MainAdapter mainAdapter;
    private Fragment_Msg fragmentMsg;
    private Fragment_Friends fragmentFriends;
    private Fragment_Discover fragmentDicover;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_main2);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setSupportActionBar(bd.main.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, bd.drawerLayout, bd.main.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        bd.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        initView();
        XmppService.login(this);

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

        imagebuttons = new ImageView[3];
        imagebuttons[0] = bd.main.body.ibWeixin;
        imagebuttons[1] = bd.main.body.ibContactList;
        imagebuttons[2] = bd.main.body.ibFind;

        textviews = new TextView[3];
        textviews[0] = bd.main.body.tvWeixin;
        textviews[1] = bd.main.body.tvContactList;
        textviews[2] = bd.main.body.tvFind;

        List<Fragment> fragments = new ArrayList<>();
        fragmentMsg = new Fragment_Msg();
        fragmentFriends = new Fragment_Friends();
        fragmentDicover = new Fragment_Discover();
        fragments.add(fragmentMsg);
        fragments.add(fragmentFriends);
        fragments.add(fragmentDicover);

        mainAdapter = new MainAdapter(getSupportFragmentManager(), fragments);
        bd.main.body.viewpager.setOffscreenPageLimit(3);
        bd.main.body.viewpager.setAdapter(mainAdapter);
        bd.main.body.viewpager.addOnPageChangeListener(pageChangeListener);

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
        bd.main.body.viewpager.setCurrentItem(index, false);
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
        CommonViewHelper.setUserViewInfo(curUser, bd.menu.ivAvatar, bd.menu.tvNickName, null, bd.menu.tvUserName, true);

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
            bd.main.body.tagUnreadMsg.setText(unreadNum + "");
            bd.main.body.tagUnreadMsg.setVisibility(View.VISIBLE);
        } else {
            bd.main.body.tagUnreadMsg.setVisibility(View.GONE);
        }
    }

    public void setUnreadInviteMsgLabel(int unreadNum) {
        if (unreadNum > 0) {
            bd.main.body.tagUnreadAddress.setText(unreadNum + "");
            bd.main.body.tagUnreadAddress.setVisibility(View.VISIBLE);
        } else {
            bd.main.body.tagUnreadAddress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            this.moveTaskToBack(true);
        }
    }

    private boolean isDrawerOpen() {
        if (bd.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            return true;
        }
        return false;
    }

    private void openDrawer() {
        bd.drawerLayout.openDrawer(GravityCompat.START);
    }

    private void closeDrawer() {
        bd.drawerLayout.closeDrawer(GravityCompat.START);
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

    public void onClickEvent(View v) {
        startActivity(new Intent(this, PersonInfoShowActivity.class));
    }

    public void onClickEvent5(View v) {
        skip(MyQRCodeActivity.class);
    }

    public void onClickEvent6(View v) {
        skip(QRScanActivity.class);
    }

    public void onClickEvent7(View v) {
        skip(SettingActivity.class);
    }

    public void skip(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }
}