package com.juns.wechat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ****************************************************
 * description ：点击事件的注解
 *
 * @since 1.5.5
 * Created by 王宗文 on 2015/9/20
 * *****************************************************
 */
@Deprecated //建议与bufferknife使用一致，已经包含了onclick注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Click {
    public int viewId();
    public ViewClickType clickType() default ViewClickType.CLICK;

}
