package com.juns.wechat.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.juns.wechat.R;
import com.style.base.BaseToolbarActivity;
import com.juns.wechat.xmpp.util.SendMessage;
import com.style.constant.Skip;

import butterknife.Bind;

public class AddFriendFinalActivity extends BaseToolbarActivity {
    @Bind(R.id.etReason)
    EditText etReason;
    //@Extra(name = ARG_USER_NAME)
    private String wantToAddUser;

    @Override
    public void initData() {
        wantToAddUser = getIntent().getStringExtra(Skip.KEY_USER_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_add_friend_final;
        super.onCreate(savedInstanceState);
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
        String reason = etReason.getText().toString();
        if (TextUtils.isEmpty(reason)) {
            showToastLong("请输入添加好友理由");
            return;
        }
        SendMessage.sendInviteMsg(wantToAddUser, reason);
    }
}
