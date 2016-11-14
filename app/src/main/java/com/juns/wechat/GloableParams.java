package com.juns.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.juns.wechat.bean.GroupInfo;
import com.juns.wechat.bean.UserBean;

public class GloableParams {

	// 屏幕高度 宽度
	public static int WIN_WIDTH;
	public static int WIN_HEIGHT;
	public static Map<String, UserBean> Users = new HashMap<String, UserBean>();
	public static List<UserBean> UserInfos = new ArrayList<UserBean>();// 好友信息
	public static List<GroupInfo> ListGroupInfos = new ArrayList<GroupInfo>();// 群聊信息
	public static Map<String, GroupInfo> GroupInfos = new HashMap<String, GroupInfo>();
	public static Boolean isHasPulicMsg = false;
}
