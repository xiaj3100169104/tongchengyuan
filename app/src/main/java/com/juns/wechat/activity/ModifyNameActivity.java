package com.juns.wechat.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.same.city.love.R;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.request.HttpActionImpl;
import com.style.net.core.NetDataBeanCallback;
import com.juns.wechat.util.NetWorkUtil;
import com.style.base.BaseToolbarActivity;

import butterknife.Bind;

public class ModifyNameActivity extends BaseToolbarActivity {
    @Bind(R.id.etInputNick)
    EditText etInputNick;
    private String nickName;

    @Override
    public void initData() {

        nickName = AccountManager.getInstance().getUser().getNickName();
        etInputNick.setText(nickName == null ? "" : nickName);
        etInputNick.setSelection(nickName == null ? 0 : nickName.length());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_modify_name;
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_with_text, menu);
        menu.getItem(0).setTitle(R.string.save);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_text_only:
                saveInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveInfo() {
        if (!NetWorkUtil.isNetworkAvailable()) {
            showToast(R.string.toast_network_unavailable);
            return;
        }
        String userName = AccountManager.getInstance().getUserName();
        nickName = etInputNick.getText().toString();
        //UserRequest.updateUser(userName, UserBean.NICKNAME, nickName, updateUserCallBack);
        HttpActionImpl.getInstance().updateUser(TAG, UserBean.NICKNAME, nickName, new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                dismissProgressDialog();
                AccountManager.getInstance().setUser(data);
                finish();
            }

            @Override
            protected void onCodeFailure(String msg) {
                dismissProgressDialog();
                showToast(msg);
            }
        });
    }
}
