package com.juns.wechat.activity;

import android.os.Bundle;

import com.juns.wechat.R;
import com.style.base.BaseToolbarBtnActivity;


public class AddNoteActivity extends BaseToolbarBtnActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_add_note;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        setToolbarTitle(R.string.note);
        getToolbarRightView().setText(R.string.complete);
        getToolbarRightView().setEnabled(false);
    }

}
