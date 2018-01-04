package cn.tongchengyuan.activity;


import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import cn.tongchengyuan.helper.CommonViewHelper;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityMycodeBinding;
import com.style.base.BaseToolbarActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;


/**
 * Created by Administrator on 2016/4/11.
 */
public class MyQRCodeActivity extends BaseToolbarActivity {

    ActivityMycodeBinding bd;
    private UserBean curUser;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_mycode);
        super.setContentView(bd.getRoot());
        initData();
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
        Bitmap mBitmap = CodeUtils.createImage(curUser.getUserId(), bd.imgCode.getWidth(), bd.imgCode.getHeight(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        bd.imgCode.setImageBitmap(mBitmap);
    }

    private void setData() {
        curUser = AccountManager.getInstance().getUser();
        CommonViewHelper.setUserViewInfo(curUser, bd.ivAvatar, bd.tvNickName, bd.ivSex, bd.tvUserName, true);
    }
}