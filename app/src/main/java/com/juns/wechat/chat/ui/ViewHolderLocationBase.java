package com.juns.wechat.chat.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.activity.SendLocationActivity;
import com.juns.wechat.chat.BaiduMapActivity;
import com.juns.wechat.chat.bean.LocationMsg;
import com.style.utils.CommonUtil;


/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderLocationBase extends BaseMsgViewHolder {
    TextView viewContent;
    private LocationMsg msg;

    ViewHolderLocationBase(View view) {
        super(view);
        viewContent = (TextView) view.findViewById(R.id.tv_address);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        msg = (LocationMsg) messageBean.getMsgObj();
        super.updateView();
        CommonUtil.setText(viewContent, msg.address);
    }

    @Override
    protected void onClickLayoutContainer() {
        super.onClickLayoutContainer();
        Intent i = new Intent(context, SendLocationActivity.class);
        i.putExtra("latitude", msg.latitude);
        i.putExtra("longitude", msg.longitude);
        i.putExtra("address", msg.address);
        i.putExtra("select", false);
        context.startActivity(i);

    }
}
