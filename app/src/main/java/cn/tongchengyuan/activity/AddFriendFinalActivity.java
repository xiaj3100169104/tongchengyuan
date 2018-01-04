package cn.tongchengyuan.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityAddFriendFinalBinding;

import cn.tongchengyuan.chat.xmpp.util.SendMessage;
import com.style.base.BaseToolbarBtnActivity;
import com.style.constant.Skip;

public class AddFriendFinalActivity extends BaseToolbarBtnActivity {
    ActivityAddFriendFinalBinding bd;

    private String wantToAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_add_friend_final);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        wantToAddUser = getIntent().getStringExtra(Skip.KEY_USER_ID);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_with_text, menu);
        menu.getItem(0).setTitle(R.string.send);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_text_only:
                goonNext();
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onClickTitleRightView() {
        goonNext();
    }

    private void goonNext() {
        String reason = bd.etReason.getText().toString();
        if (TextUtils.isEmpty(reason)) {
            showToastLong("请输入添加好友理由");
            return;
        }
        SendMessage.sendInviteMsg(wantToAddUser, reason);
    }
}
