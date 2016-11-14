package com.juns.wechat.annotation;

/**
 * ****************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：按钮点击事件的注解
 *
 * @since 1.5.6
 * Created by 王宗文 on 2015/9/ic_card_detail_arrow_down
 * *****************************************************
 */
public enum  ViewClickType {
    CLICK(0), ITEM_CLICK(1), LONG_CLICK(2);

    private int type;

    ViewClickType(int type){
        this.type = type;
    }

    public int getValue(){
        return type;
    }

    public static int valueOf(ViewClickType clickType){
        if(clickType == ViewClickType.CLICK){
            return 0;
        }else if(clickType == ViewClickType.ITEM_CLICK){
            return 1;
        }else {
            return 2;
        }
    }
}
