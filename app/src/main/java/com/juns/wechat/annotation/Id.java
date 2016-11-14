package com.juns.wechat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ****************************************************
 * description ：View类型控件的注解
 * 为了方便，可以不写value值，但要求其注释的成员变量名称与其在xml文件中id值相同，否则反射调用出错
 * @since 1.5.6
 * Created by 王宗文 on 2015/9/20
 * *****************************************************
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    int value() default 0;
}
