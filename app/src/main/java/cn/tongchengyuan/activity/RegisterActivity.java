package cn.tongchengyuan.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;

import cn.tongchengyuan.net.request.HttpActionImpl;
import cn.tongchengyuan.util.NetWorkUtil;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.net.response.LoginBean;
import cn.tongchengyuan.net.response.RegisterBean;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityRegisterBinding;
import com.style.base.BaseToolbarActivity;
import com.style.net.core.NetDataBeanCallback;
import com.style.utils.FormatUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * 用户注册
 * create by 王者 on 2061/2/7
 */
public class RegisterActivity extends BaseToolbarActivity implements OnClickListener {
    ActivityRegisterBinding bd;
    private MyCount mc;
    private Handler handler = new Handler();

    private String userName;
    private String passWord;
    private String verifyCode;
    private int validCodeResult = 0; //校验二维码结果
    private static final int VALID_CODE_SUCCESS = 1;
    private static final int VALID_CODE_FAILED = -1;

    private EventHandler eventHandler; //短信验证码的事件监听

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_register);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("注册");
        bd.btnSend.setOnClickListener(this);
        bd.btnRegister.setOnClickListener(this);
        bd.etInputName.addTextChangedListener(new TelTextChange());
        bd.etPassword.addTextChangedListener(new TextChange());
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        validCodeResult = VALID_CODE_SUCCESS;
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    Throwable throwable = (Throwable) data;
                    throwable.printStackTrace();
                    try {
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");//错误描述
                        int status = object.optInt("status");//错误代码
                        if (status == 468) { //验证码错误
                            validCodeResult = VALID_CODE_FAILED;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                //CommonUtil.finish(RegisterActivity.this);
                break;
            case R.id.btn_send:
                if (mc == null) {
                    mc = new MyCount(60000, 1000); // 第一参数是总的时间，第二个是间隔时间
                }
                mc.start();
                sendVerifyCode();
                break;

            case R.id.btnRegister:
                checkInput();
                break;
            default:
                break;
        }
    }

    private void sendVerifyCode() {
        userName = bd.etInputName.getText().toString().trim();
        if (!FormatUtil.isMobileNum(userName)) {
            showToast("请使用手机号码注册账户！ ");
            return;
        }
        SMSSDK.getVerificationCode("86", userName);
    }

    private void checkInput() {
        userName = bd.etInputName.getText().toString().trim();
        passWord = bd.etPassword.getText().toString();
        verifyCode = bd.etVerifyCode.getText().toString().trim();
        if (!FormatUtil.isMobileNum(userName)) {
            showToast("请使用手机号码注册账户！ ");
            return;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            showToast("请填写手机号码，并获取验证码！");
            return;
        }
        if (TextUtils.isEmpty(passWord) || passWord.length() < 6) {
            showToast("密码不能少于6位！");
            return;
        }

        showProgressDialog("正在注册...  ");

        validCodeResult = 0;
        SMSSDK.submitVerificationCode("86", userName, verifyCode);
        while (validCodeResult == 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (validCodeResult == VALID_CODE_FAILED) {
            showToast("验证码错误！");
            dismissProgressDialog();
            return;
        }

        register(userName, passWord);
    }

    private void register(String name, String pwd) {
        if (!NetWorkUtil.isNetworkAvailable()) {
            showToast(R.string.toast_network_unavailable);
            return;
        }
        HttpActionImpl.getInstance().register(TAG, name, pwd, new NetDataBeanCallback<RegisterBean>(RegisterBean.class) {
            @Override
            protected void onCodeSuccess() {
                //dismissProgressDialog();
                login();
            }

            @Override
            protected void onCodeFailure(int code, RegisterBean data) {
                dismissProgressDialog();
                if (code == 1) {  //参数错误
                /*	if(data.errField.equalsIgnoreCase(UserBean.USERNAME)){
						showToast("用户名不合法");
					}else if(data.errField.equalsIgnoreCase(UserBean.PASSWORD)){
						showToast("密码长度不能小于6位");
					}*/
                } else if (code == 2) {
                    showToast("该用户已注册，可以直接登录");
                }
            }
        });
    }

    private void login() {
        HttpActionImpl.getInstance().login(TAG, userName, passWord, new NetDataBeanCallback<LoginBean>(LoginBean.class) {
            @Override
            protected void onCodeSuccess(LoginBean data) {
                dismissProgressDialog();
                AccountManager.getInstance().setUser(data.userBean);
                AccountManager.getInstance().setToken(data.token);
                AccountManager.getInstance().setUserPassWord(passWord);
                skip(MainActivity.class);
                finish();
            }

            @Override
            protected void onCodeFailure(String msg) {
                dismissProgressDialog();
                showToast(msg);
            }
        });
    }

    // 手机号 EditText监听器
    class TelTextChange implements TextWatcher {

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
            String phone = bd.etInputName.getText().toString();
            if (phone.length() == 11) {
                if (FormatUtil.isMobileNum(phone)) {
                    bd.btnSend.setTextColor(0xFFFFFFFF);
                    bd.btnSend.setEnabled(true);
                    bd.btnRegister.setTextColor(0xFFFFFFFF);
                    bd.btnRegister.setEnabled(true);
                } else {
                    bd.etInputName.requestFocus();
                    showToast("请输入正确的手机号码！");
                }
            } else {
                bd.btnSend.setEnabled(false);
                bd.btnRegister.setEnabled(true);
            }
        }
    }

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
            boolean Sign1 = bd.etVerifyCode.getText().length() > 0;
            boolean Sign2 = bd.etInputName.getText().length() > 0;
            boolean Sign3 = bd.etPassword.getText().length() > 0;

            if (Sign1 & Sign2 & Sign3) {
                bd.btnRegister.setEnabled(true);
            } else {
                bd.btnRegister.setEnabled(false);
            }
        }
    }

    /* 定义一个倒计时的内部类 */
    private class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            bd.btnSend.setEnabled(true);
            bd.btnSend.setText("发送验证码");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bd.btnSend.setEnabled(false);
            bd.btnSend.setText("(" + millisUntilFinished / 1000 + ")秒");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
