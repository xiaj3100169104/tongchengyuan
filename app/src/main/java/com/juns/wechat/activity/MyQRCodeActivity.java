package com.juns.wechat.activity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.helper.CommonViewHelper;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ImageLoader;
import com.style.base.BaseToolbarActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.Bind;


/**
 * Created by Administrator on 2016/4/11.
 */
public class MyQRCodeActivity extends BaseToolbarActivity {


    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNickName)
    TextView tvNickName;
    @Bind(R.id.ivSex)
    ImageView ivSex;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.img_code)
    ImageView imgCode;
    private UserBean curUser;

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_mycode;
        super.onCreate(arg0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_circle, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select:
                //skip(DynamicPublishActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        setToolbarTitle(R.string.qr_card);
        setData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            initQRcode();

        }
    }

    private void initQRcode() {
        Bitmap mBitmap = CodeUtils.createImage(curUser.getUserName(), imgCode.getWidth(), imgCode.getHeight(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        imgCode.setImageBitmap(mBitmap);
    }

    private void setData() {
        curUser = AccountManager.getInstance().getUser();
        CommonViewHelper.setUserViewInfo(curUser, ivAvatar, tvNickName, ivSex, tvUserName, true);
    }
}