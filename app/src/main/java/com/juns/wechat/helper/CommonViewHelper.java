package com.juns.wechat.helper;

import android.widget.ImageView;
import android.widget.TextView;

import com.juns.wechat.App;
import com.juns.wechat.R;
import com.juns.wechat.bean.UserBean;
import com.style.manager.ImageLoader;

/**
 * Created by xiajun on 2016/12/25.
 */

public class CommonViewHelper {
    public static void setUserViewInfo(UserBean user, ImageView ivAvatar, TextView tvNick, ImageView ivSex, TextView tvAccount, boolean isAccountPre) {
        if (user != null) {
            if (ivAvatar != null)
                ImageLoader.loadAvatar(App.getInstance(), ivAvatar, user.getHeadUrl());
            if (tvNick != null)
                tvNick.setText(user.getShowName());
            if (tvAccount != null) {
                String str = user.getUserName();
                if (isAccountPre)
                    str = "微信号：" + str;
                tvAccount.setText(str);
            }
            if (ivSex != null) {
                String sex = user.getSex();
                int resId = R.drawable.ic_sex_female;
                if (UserBean.Sex.isMan(sex))
                    resId = R.drawable.ic_sex_male;
                ivSex.setImageResource(resId);
            }
        }
    }
}
