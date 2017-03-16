package com.juns.wechat.chat.ui;

import android.view.View;
import android.widget.TextView;

import com.juns.wechat.App;
import com.juns.wechat.R;
import com.juns.wechat.chat.bean.TextMsg;
import com.juns.wechat.chat.utils.SmileUtils;
import com.style.utils.CommonUtil;


/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderTextBase extends BaseMsgViewHolder {
    TextView viewContent;

    ViewHolderTextBase(View view) {
        super(view);
        viewContent = (TextView) view.findViewById(R.id.tv_chat_content);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        TextMsg textMsg1 = (TextMsg) messageBean.getMsgObj();
        super.updateView();
        CommonUtil.setText(viewContent, SmileUtils.getSmiledText(App.getInstance(), textMsg1.content));
    }
    @Override
    protected void onClickLayoutContainer(){
        super.onClickLayoutContainer();
    }
}
