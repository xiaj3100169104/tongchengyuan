package com.juns.wechat.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Click;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Extra;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.util.ToastUtil;
import com.juns.wechat.util.ToolBarUtil;
import com.juns.wechat.xmpp.util.SendMessage;

@Content(R.layout.activity_add_friend_final)
public class AddFriendFinalActivity extends ToolbarActivity {
    public static final String ARG_USER_NAME = "user_name";
    @Extra(name = ARG_USER_NAME)
    private String wantToAddUser;
    @Id
    private EditText etReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToolBarUtil.setToolbarRightText(this, "发送");

    }

    @Click(viewId = R.id.tvRightText)
    private void onRightBtnClicked(View v){
        String reason = etReason.getText().toString();
        if(TextUtils.isEmpty(reason)){
            ToastUtil.showToast("请输入添加好友理由", Toast.LENGTH_SHORT);
            return;
        }
        SendMessage.sendInviteMsg(wantToAddUser, reason);
    }
}
