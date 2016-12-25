package com.juns.wechat.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.activity.FriendCircleActivity;
import com.juns.wechat.activity.MyCollectActivity;
import com.juns.wechat.activity.MyProfileActivity;
import com.juns.wechat.activity.SettingActivity;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.database.UserTable;
import com.juns.wechat.helper.CommonViewHelper;
import com.juns.wechat.manager.AccountManager;
import com.style.base.BaseBusFragment;

import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

//我
public class Fragment_Profile extends BaseBusFragment implements OnClickListener {
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNickName)
    TextView tvNickName;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.ivSex)
    ImageView ivSex;

    private UserBean curUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_profile;
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected void initData() {
        updateUserView();
        setOnListener();
    }

    private void setOnListener() {
        getView().findViewById(R.id.view_user).setOnClickListener(this);
        getView().findViewById(R.id.txt_album).setOnClickListener(this);
        getView().findViewById(R.id.txt_collect).setOnClickListener(this);
        getView().findViewById(R.id.txt_money).setOnClickListener(this);
        getView().findViewById(R.id.txt_card).setOnClickListener(this);
        getView().findViewById(R.id.txt_smail).setOnClickListener(this);
        getView().findViewById(R.id.txt_setting).setOnClickListener(this);
    }

    @Subscriber(tag = UserTable.TABLE_NAME)
    private void onDbDataChanged(DbDataEvent<UserBean> event) {
        if (event.action == DbDataEvent.REPLACE || event.action == DbDataEvent.UPDATE) {
            List<UserBean> updateData = event.data;
            if (updateData != null && !updateData.isEmpty()) {
                for (UserBean userBean : updateData) {
                    if (userBean.getUserName().equals(curUser.getUserName())) {
                        updateUserView();
                    }
                }
            }
        }
    }

    private void updateUserView() {
        curUser = AccountManager.getInstance().getUser();
        CommonViewHelper.setUserViewInfo(curUser, ivAvatar, tvNickName, ivSex, tvUserName, true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_user:
                skip(MyProfileActivity.class);
                break;
            case R.id.txt_album:// 相册
                skip(FriendCircleActivity.class);
                break;
            case R.id.txt_collect:// 收藏
                skip(MyCollectActivity.class);
                break;
        /*case R.id.txt_money:// 钱包
            CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME,
							getString(R.string.wallet)));
			break;
		case R.id.txt_card:// 相册
			CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME,
							getString(R.string.card_bag)));
			break;
		case R.id.txt_smail:// 表情
			CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME,
							getString(R.string.expression)));
			break;*/
            case R.id.txt_setting:// 设置
                skip(SettingActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}