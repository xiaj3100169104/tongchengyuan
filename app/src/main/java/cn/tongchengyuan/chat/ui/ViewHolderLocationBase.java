package cn.tongchengyuan.chat.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.same.city.love.R;

import cn.tongchengyuan.activity.SendLocationActivity;
import cn.tongchengyuan.chat.bean.LocationMsgData;
import com.style.utils.CommonUtil;


/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderLocationBase extends BaseMsgViewHolder {
    TextView viewContent;
    private LocationMsgData msg;

    ViewHolderLocationBase(View view) {
        super(view);
        viewContent = (TextView) view.findViewById(R.id.tv_address);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        msg = (LocationMsgData) messageBean.getMsgDataObj();
        super.updateView();
        viewContent.setText(CommonUtil.getNotNullText(msg.address));
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
