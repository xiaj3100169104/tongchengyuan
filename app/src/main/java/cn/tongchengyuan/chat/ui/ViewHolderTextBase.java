package cn.tongchengyuan.chat.ui;

import android.view.View;
import android.widget.TextView;

import com.same.city.love.R;

import cn.tongchengyuan.chat.utils.SmileUtils;
import cn.tongchengyuan.chat.bean.TextMsgData;

import com.style.utils.CommonUtil;


/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderTextBase extends BaseMsgViewHolder {
    TextView viewContent;
    private TextMsgData msg;

    ViewHolderTextBase(View view) {
        super(view);
        viewContent = (TextView) view.findViewById(R.id.tv_chat_content);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        msg = (TextMsgData) messageBean.getMsgDataObj();
        super.updateView();
        viewContent.setText(CommonUtil.getNotNullText(SmileUtils.getInstance().getSmiledText(msg.content)));
    }

    @Override
    protected void onClickLayoutContainer() {
        super.onClickLayoutContainer();
    }
}
