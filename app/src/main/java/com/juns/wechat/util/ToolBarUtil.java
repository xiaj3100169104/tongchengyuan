package com.juns.wechat.util;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.same.city.love.R;


/*******************************************************
 * description ：加载toolbar的工具类
 *
 * @since 1.5.5
 * Created by 王宗文 on 2015/9/14
 *******************************************************/
@Deprecated
public class ToolBarUtil {
  /*  public static final int TAG_TEXT = 1;
    public static final int TAG_IMG = 2;
    *//**
     * 自定义toolbar的显示
     *
     * @param activity
     *//*
    public static Toolbar setToolBar(final AppCompatActivity activity) {
        Toolbar toolBar = (Toolbar) activity.findViewById(R.id.toolbar);

        if(activity.getSupportActionBar() != null){
            return toolBar;
        }

        TextView tvTitle = (TextView) toolBar.findViewById(R.id.tvTitle);
        tvTitle.setText(AppUtil.getActivityLabel(activity));

        ImageView ivReturn = (ImageView) toolBar.findViewById(R.id.ivReturn);
        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        activity.setSupportActionBar(toolBar);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);

        return toolBar;

    }

    *//**
     * 自定义标题栏文字，需要在{@link ToolBarUtil#setToolBar(AppCompatActivity)}方法后调用，否则会被覆盖
     *
     * @param activity
     * @param text
     *//*
    public static void setTitle(AppCompatActivity activity, String text) {
        Toolbar toolBar = setToolBar(activity);
        TextView tvTitle = (TextView) toolBar.findViewById(R.id.tvTitle);
        if(!TextUtils.isEmpty(text)){
            tvTitle.setText(text);
        }
    }

    *//**
     * 自定义标题栏文字，需要在{@link ToolBarUtil#setToolBar(AppCompatActivity)}方法后调用，否则会被覆盖
     *
     * @param activity
     * @param resId
     *//*
    public static void setTitle(AppCompatActivity activity, int resId) {
        setTitle(activity, activity.getString(resId));
    }

    public static void setToolbarRight(AppCompatActivity activity, int type, int resId){
        Toolbar toolBar = setToolBar(activity);
        TextView tvRightText = (TextView) toolBar.findViewById(R.id.tvRightText);
        ImageView ivRightBtn = (ImageView) toolBar.findViewById(R.id.ivRightBtn);
        if(type == 1){
            tvRightText.setVisibility(View.VISIBLE);
            ivRightBtn.setVisibility(View.GONE);
            tvRightText.setText(resId);
        }else if(type == 2){
            tvRightText.setVisibility(View.GONE);
            ivRightBtn.setVisibility(View.VISIBLE);
            ivRightBtn.setImageResource(resId);
        }
    }

    public static void setToolbarRightText(AppCompatActivity activity, int resId){
        setToolbarRightText(activity, activity.getString(resId));
    }

    public static void setToolbarRightText(AppCompatActivity activity, String text){
        Toolbar toolBar = setToolBar(activity);
        TextView tvRightText = (TextView) toolBar.findViewById(R.id.tvRightText);
        ImageView ivRightBtn = (ImageView) toolBar.findViewById(R.id.ivRightBtn);

        tvRightText.setVisibility(View.VISIBLE);
        if(ivRightBtn != null){ //comm_toolbar_btn layout中不存在这个控件
            ivRightBtn.setVisibility(View.GONE);
        }
        tvRightText.setText(text);
    }

    public static void setToolbarRightImage(AppCompatActivity activity, int resId){
        Toolbar toolBar = setToolBar(activity);
        TextView tvRightText = (TextView) toolBar.findViewById(R.id.tvRightText);
        ImageView ivRightBtn = (ImageView) toolBar.findViewById(R.id.ivRightBtn);

        tvRightText.setVisibility(View.GONE);
        ivRightBtn.setVisibility(View.VISIBLE);
        ivRightBtn.setImageResource(resId);
    }

    public static void hideToolbarRight(AppCompatActivity activity){
        Toolbar toolBar = setToolBar(activity);
        TextView tvRightText = (TextView) toolBar.findViewById(R.id.tvRightText);
        ImageView ivRightBtn = (ImageView) toolBar.findViewById(R.id.ivRightBtn);
        tvRightText.setVisibility(View.GONE);
        ivRightBtn.setVisibility(View.GONE);
    }

*/
}
