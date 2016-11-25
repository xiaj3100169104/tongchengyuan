package com.juns.wechat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ****************************************************
 * Copyright (C) 2014-2015 Yunyun Network <yynetworks@yycube.com>
 * description ：Intent传值的注解
 *
 * 需要传递的值的类型出了8中基本类型外还支持实现了parcelable接口的对象
 *
 * @since 1.5.5
 * Created by 王宗文 on 2015/9/ic_card_detail_arrow_down
 * *****************************************************
 */
@Deprecated //跳转之类不用用注解吧，数据库操作那些倒是用注解比较好
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Extra {
    public String name();
    public String defValue() default "";

}
