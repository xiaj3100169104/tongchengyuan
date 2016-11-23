package com.juns.wechat.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.juns.wechat.R;
import com.juns.wechat.annotation.Click;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.callback.UpdateUserCallBack;
import com.juns.wechat.net.response.UpdateUserResponse;
import com.juns.wechat.util.NetWorkUtil;

@Content(R.layout.activity_modify_name)
public class ModifyNameActivity extends ToolbarActivity {
    @Id
    private EditText etInputNick;

    private String nickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nickName = AccountManager.getInstance().getUser().getNickName();
        etInputNick.setText(nickName == null ? "" : nickName);
        etInputNick.setSelection(nickName == null ? 0 : nickName.length());
    }

    /**
     * 保存按钮的点击事件
     * @param v
     */
    @Click(viewId = R.id.tvRightText)
    private void saveInfo(View v){
        if(!NetWorkUtil.isNetworkAvailable()){
            showToast(R.string.toast_network_unavailable);
            return;
        }
        String userName = AccountManager.getInstance().getUserName();
        nickName = etInputNick.getText().toString();
        UserRequest.updateUser(userName, UserBean.NICKNAME, nickName, updateUserCallBack);
    }

    private UpdateUserCallBack updateUserCallBack = new UpdateUserCallBack() {
        @Override
        protected void handleResponse(UpdateUserResponse result) {
            super.handleResponse(result);
            finish();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showToast(R.string.toast_network_error);
        }
    };
}
