package com.juns.wechat.common;

import java.util.Comparator;

import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;

public class PinyinComparator implements Comparator<FriendBean> {

	@Override
	public int compare(FriendBean friend1, FriendBean friend2) {
		// 按照名字排序
		String catalog0 = "";
		String catalog1 = "";

		if (friend1 != null && friend1.getShowName() != null
				&& friend1.getShowName().length() > 1)
			catalog0 = PingYinUtil.converterToFirstSpell(friend1.getShowName())
					.substring(0, 1);

		if (friend2 != null && friend2.getShowName() != null
				&& friend2.getShowName().length() > 1)
			catalog1 = PingYinUtil.converterToFirstSpell(friend2.getShowName())
					.substring(0, 1);
		int flag = catalog0.compareTo(catalog1);
		return flag;

	}

}
