package cn.tongchengyuan.chat.ui;


import cn.tongchengyuan.chat.bean.MessageBean;

import java.util.Comparator;

public class MessageBeanComparator implements Comparator<MessageBean> {
	public int compare(MessageBean o1, MessageBean o2) {
			return String.valueOf(new Long(o1.getDate().getTime())).compareTo(String.valueOf(new Long(o2.getDate().getTime())));
	}
}
