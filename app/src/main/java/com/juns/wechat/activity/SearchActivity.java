package com.juns.wechat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.juns.wechat.R;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.net.common.HttpActionImpl;
import com.juns.wechat.net.common.NetDataBeanCallback;
import com.juns.wechat.util.LogUtil;
import com.style.base.BaseActivity;
import com.style.constant.Skip;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 王宗文 on 2016/6/20.
 */

public class SearchActivity extends BaseActivity {

    private EditText etSearch;
    private RelativeLayout rlSearch;
    private TextView tvSearchContent;
    private String search;

    @Override
    public void initData() {
        initView();
        setListener();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_search;
        super.onCreate(savedInstanceState);

    }

    private void initView(){
        etSearch = (EditText) findViewById(R.id.et_search);
        rlSearch = (RelativeLayout) findViewById(R.id.re_search);
        tvSearchContent = (TextView) findViewById(R.id.tv_search);
    }

    private void setListener(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search = etSearch.getText().toString();
                if(!TextUtils.isEmpty(search)){
                    tvSearchContent.setText(search);
                    rlSearch.setVisibility(View.VISIBLE);
                }else {
                    tvSearchContent.setText("");
                    rlSearch.setVisibility(View.GONE);
                }
            }
        });

        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });
    }

    ProgressDialog progressDialog;

    private void showSearchDialog(){
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("正在查找联系人...");
        progressDialog.show();

        LogUtil.i("start search");

        HttpActionImpl.getInstance().searchUser(TAG, search, new NetDataBeanCallback<List<UserBean>>(new TypeReference<List<UserBean>>() {
        }) {
            @Override
            protected void onCodeSuccess(List<UserBean> data) {
                progressDialog.dismiss();
                showSearchResult(data);
            }

            @Override
            protected void onCodeFailure(String msg) {
                progressDialog.dismiss();
                showToast(msg);
            }
        });
    }

    private void showSearchResult(List<UserBean> userBeans){
        if(userBeans != null && !userBeans.isEmpty()){
            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
            intent.putExtra(Skip.KEY_SEARCH_RESULTS, (Serializable) userBeans);
            startActivity(intent);
        }else {
            showToast("没有搜索到用户");
        }
    }

}
