package com.juns.wechat.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.juns.wechat.MainActivity;
import com.juns.wechat.R;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Id;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.request.UserRequest;
import com.juns.wechat.net.callback.LoginCallBack;
import com.juns.wechat.net.response.BaseResponse;

/**
 * create by 王者 on 2016/7/12
 */
@Content(R.layout.activity_login)
public class LoginActivity extends ToolbarActivity implements OnClickListener {
    @Id
	private Button btn_login;
    @Id
    private Button btn_register;
    @Id
	private EditText etInputName;
    @Id
    private EditText etPassWord;
    private String userName, password;
    private UserBean userBean = AccountManager.getInstance().getUser();

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initControl();
        setListener();
    }

	protected void initControl() {
        findViewById(R.id.ivReturn).setVisibility(View.GONE);
        if(userBean != null){
            etInputName.setText(userBean.getUserName());
            etPassWord.setText(userBean.getPassWord());
            userName = userBean.getUserName();
            password = userBean.getPassWord();
            btn_login.setEnabled(true);
        }
	}

    protected void setListener() {
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		findViewById(R.id.tv_wenti).setOnClickListener(this);
		etInputName.addTextChangedListener(new TextChange());
		etPassWord.addTextChangedListener(new TextChange());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			break;
		case R.id.tv_wenti:
			/*CommonUtil.startActivity(LoginActivity.this, WebViewActivity.class,
					new BasicNameValuePair(Constants.Title, "帮助"),
					new BasicNameValuePair(Constants.URL,
							"http://weixin.qq.com/"));*/
			break;
		case R.id.btn_register:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			break;
		case R.id.btn_login:
			startLogin();
			break;
		default:
			break;
		}
	}

	private void startLogin() {
		userName = etInputName.getText().toString().trim();
		password = etPassWord.getText().toString().trim();
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            login(userName, password);
        } else {
            showToast("请填写账号或密码！");
        }
	}

    private void login(String userName, String password){
        if(!com.style.utils.CommonUtil.isNetWorkConnected(this)){
            showToast(R.string.toast_network_unavailable);
            return;
        }
        getLoadingDialog("正在登录...").show();
        UserRequest.login(userName, password, loginCallBack);
    }

    private LoginCallBack loginCallBack = new LoginCallBack() {

        @Override
        protected void handleResponse(BaseResponse.LoginResponse result) {
            super.handleResponse(result);
            if(result.code == 0){
                AccountManager.getInstance().setUserPassWord(password);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }else {
                handleFailed(result);
            }

        }

        protected void handleFailed(BaseResponse.LoginResponse result) {
            showToast("用户名或密码错误");
            getLoadingDialog("正在登录...").dismiss();
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            showToast(R.string.toast_network_error);
            getLoadingDialog("正在登录...").dismiss();
        }
    };

    // EditText监听器
	class TextChange implements TextWatcher {

		@Override
		public void afterTextChanged(Editable arg0) {

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {

		}

		@Override
		public void onTextChanged(CharSequence cs, int start, int before,
				int count) {
			boolean Sign2 = etInputName.getText().length() > 0;
			boolean Sign3 = etPassWord.getText().length() > 4;
			if (Sign2 & Sign3) {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_bg_green));
				btn_login.setEnabled(true);
			} else {
				btn_login.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.btn_enable_green));
				btn_login.setTextColor(0xFFD0EFC6);
				btn_login.setEnabled(false);
			}
		}
	}

}
