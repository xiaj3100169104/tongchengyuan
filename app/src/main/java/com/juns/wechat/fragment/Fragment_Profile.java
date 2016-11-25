package com.juns.wechat.fragment;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.activity.MyProfileActivity;
import com.juns.wechat.bean.UserBean;
import com.style.base.BaseBusFragment;
import com.juns.wechat.dao.DbDataEvent;
import com.juns.wechat.database.UserTable;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.util.ImageLoader;
import com.juns.wechat.activity.SettingActivity;

import java.util.List;

//我
public class Fragment_Profile extends BaseBusFragment implements OnClickListener {
	private View layout;
    private ImageView ivAvatar;
	private TextView tvNickName, tvUserName;
    private UserBean account;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_profile, container, false);

        initViews();
        initData();
        setOnListener();

		return layout;
	}

	private void initViews() {
        ivAvatar = (ImageView) layout.findViewById(R.id.ivAvatar);
		tvNickName = (TextView) layout.findViewById(R.id.tvNickName);
		tvUserName = (TextView) layout.findViewById(R.id.tvUserName);
	}

	private void setOnListener() {
		layout.findViewById(R.id.view_user).setOnClickListener(this);
		layout.findViewById(R.id.txt_album).setOnClickListener(this);
		layout.findViewById(R.id.txt_collect).setOnClickListener(this);
		layout.findViewById(R.id.txt_money).setOnClickListener(this);
		layout.findViewById(R.id.txt_card).setOnClickListener(this);
		layout.findViewById(R.id.txt_smail).setOnClickListener(this);
		layout.findViewById(R.id.txt_setting).setOnClickListener(this);
	}

	protected void initData() {
		account = AccountManager.getInstance().getUser();
        tvUserName.setText("微信号：" + account.getUserName());
        tvNickName.setText(account.getNickName() == null ? account.getUserName() : account.getNickName());

        ImageLoader.loadAvatar(ivAvatar, account.getHeadUrl());
	}

    @Subscriber(tag = UserTable.TABLE_NAME)
    private void onDbDataChanged(DbDataEvent<UserBean> event){
        if(event.action == DbDataEvent.REPLACE || event.action == DbDataEvent.UPDATE){
            List<UserBean> updateData = event.data;
            if(updateData != null && !updateData.isEmpty()){
                for(UserBean userBean : updateData){
                    if(userBean.getUserName().equals(account.getUserName())){
                        initData();
                    }
                }
            }
        }
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_user:
			startActivity(new Intent(getActivity(), MyProfileActivity.class));
			break;
		/*case R.id.txt_album:// 相册
			CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME,
							getString(R.string.my_posts)));
			break;
		case R.id.txt_collect:// 收藏
			CommonUtil.startActivity(getActivity(), PublicActivity.class,
					new BasicNameValuePair(Constants.NAME,
							getString(R.string.collection)));
			break;
		case R.id.txt_money:// 钱包
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
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
		default:
			break;
		}
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}