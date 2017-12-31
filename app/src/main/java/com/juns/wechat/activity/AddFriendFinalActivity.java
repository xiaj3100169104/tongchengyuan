package com.juns.wechat.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityAddFriendFinalBinding;
import com.style.base.BaseToolbarActivity;
import com.juns.wechat.chat.xmpp.util.SendMessage;
import com.style.constant.Skip;

public class AddFriendFinalActivity extends BaseToolbarActivity {
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
