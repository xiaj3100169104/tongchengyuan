package com.juns.wechat.annotation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.juns.wechat.R;
import com.juns.wechat.util.LogUtil;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * ****************************************************
 * description ：解析注解的工具类
 *
 * @since 1.5.5
 * Created by 王宗文 on 2015/9/20
 * *****************************************************
 */
public class AnnotationUtil {
    private static final HashSet<Class<?>> IGNORED = new HashSet<Class<?>>();
    static {
        IGNORED.add(Object.class);
        IGNORED.add(Activity.class);
        IGNORED.add(android.app.Fragment.class);
        try {
            IGNORED.add(Class.forName("android.support.v4.app.Fragment"));
            IGNORED.add(Class.forName("android.support.v4.app.FragmentActivity"));
        } catch (Throwable ignored) {
        }
    }

    public static void initAnnotation(FragmentActivity activity){
        //获取Activity的ContentView的注解
        Class<?> handlerType = activity.getClass();
        try {
            Content contentView = findContentView(handlerType);
            if (contentView != null) {
                int viewId = contentView.value();
                if (viewId > 0) {
                    Method setContentViewMethod = handlerType.getMethod("setContentView", int.class);
                    setContentViewMethod.invoke(activity, viewId);
                }
            }
        } catch (Throwable ex) {
            LogUtil.e(ex.getMessage(), ex);
        }
        initAnnotation(activity, activity.getWindow().getDecorView());
    }

    /**
     * 从父类获取注解View
     */
    private static Content findContentView(Class<?> thisCls) {
        if (thisCls == null || IGNORED.contains(thisCls)) {
            return null;
        }
        Content contentView = thisCls.getAnnotation(Content.class);
        if (contentView == null) {
            return findContentView(thisCls.getSuperclass());
        }
        return contentView;
    }

    public static void initAnnotation(Fragment fragment){
        initAnnotation(fragment, fragment.getView());
    }

    private static void initAnnotation(Object injectedSource, View sourceView){
        Class<?> clazz = injectedSource.getClass();
        for(; clazz != Object.class; clazz = clazz.getSuperclass()){
            Field[] fields = clazz.getDeclaredFields();
            if(fields != null && fields.length != 0){
                injectFields(injectedSource, fields, sourceView);
                getExtras(injectedSource, fields, sourceView);
            }

            Method[] methods = clazz.getDeclaredMethods();
            if(methods != null && methods.length != 0){
                injectMethods(injectedSource, methods, sourceView);
            }
        }
    }

    public static void injectFields(final Object injectedSource, Field[] fields, View sourceView){
        for(Field f : fields){
            Id id = f.getAnnotation(Id.class);
            if(id != null){
                int viewId = id.value();
                f.setAccessible(true);
                if(viewId == 0){   //如果没有知道view的Id,则尝试根据编程名称去从R文件中取
                    try {
                        Field rf = R.id.class.getDeclaredField(f.getName());
                        rf.setAccessible(true);
                        viewId = rf.getInt(R.id.class);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    /*
	                    * 当已经被赋值时，不在重复赋值，用于include，inflate情景下的viewinject组合
					*/
                    if (f.get(injectedSource) == null) {
                        f.set(injectedSource, sourceView.findViewById(viewId));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void getExtras(final Object injectedSource, Field[] fields, View sourceView){
        Bundle bundle = null;
        if (injectedSource instanceof Activity) {
            Activity ac = (Activity) injectedSource;
            bundle = ac.getIntent().getExtras();
        } else if (injectedSource instanceof Fragment) {
            Fragment fg = (Fragment) injectedSource;
            bundle = fg.getArguments();
        }
        if(bundle==null) bundle=new Bundle();

        for(Field f : fields){
            f.setAccessible(true);
            Extra extra = f.getAnnotation(Extra.class);
            if(extra != null){
                try{
                    Object obj = null;
                    Class<?> clazz = f.getType();
                    if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                        if (!TextUtils.isEmpty(extra.defValue())) {
                            obj = bundle.getInt(extra.name(),
                                    Integer.parseInt(extra.defValue()));
                        } else {
                            obj = bundle.getInt(extra.name(), 0);
                        }
                    } else if (clazz.equals(String.class)) {
                        obj = bundle.getString(extra.name());
                        if (obj == null) {
                            if (!TextUtils.isEmpty(extra.defValue())) {
                                obj = extra.defValue();
                            }
                        }
                    } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                        if (!TextUtils.isEmpty(extra.defValue())) {
                            obj = bundle.getLong(extra.name(),
                                    Long.parseLong(extra.defValue()));
                        } else {
                            obj = bundle.getLong(extra.name(), 0);
                        }
                    } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
                        if (!TextUtils.isEmpty(extra.defValue())) {
                            obj = bundle.getFloat(extra.name(),
                                    Float.parseFloat(extra.defValue()));
                        } else {
                            obj = bundle.getFloat(extra.name(), 0);
                        }
                    } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                        if (!TextUtils.isEmpty(extra.defValue())) {
                            obj = bundle.getDouble(extra.name(),
                                    Double.parseDouble(extra.defValue()));
                        } else {
                            obj = bundle.getDouble(extra.name(), 0);
                        }
                    } else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
                        if (!TextUtils.isEmpty(extra.defValue())) {
                            obj = bundle.getBoolean(extra.name(),
                                    Boolean.parseBoolean(extra.defValue()));
                        } else {
                            obj = bundle.getBoolean(extra.name(), true);
                        }
                    } else if(clazz.equals(List.class)){
                        obj = bundle.getParcelableArrayList(extra.name());
                    } else {
                        obj = bundle.getParcelable(extra.name());
                    }

                    if (obj != null) {
                        f.set(injectedSource, obj);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static void injectMethods(final Object injectedSource, Method[] methods, View sourceView){
        for(final Method m : methods){
            Click click = m.getAnnotation(Click.class);
            if(click != null){
                m.setAccessible(true);
                int id = click.viewId();
                ViewClickType viewClickType = click.clickType();
                View view = sourceView.findViewById(id);
                switch (viewClickType){
                    case CLICK:
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    m.invoke(injectedSource, v);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;

                    case ITEM_CLICK:
                        if(view instanceof AbsListView){
                            ((AbsListView) view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    try {
                                        m.invoke(injectedSource, parent, view, position, id);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }
                        break;

                    case LONG_CLICK:
                        view.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                try {
                                    return (boolean) m.invoke(injectedSource, v);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }finally {
                                    return false;
                                }
                            }
                        });
                        break;

                    default:
                        break;
                }

            }
        }
    }
}
