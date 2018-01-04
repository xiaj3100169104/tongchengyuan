package cn.tongchengyuan.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.same.city.love.R;

import cn.tongchengyuan.net.request.HttpActionImpl;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.databinding.ActivitySearchBinding;
import com.style.net.core.NetDataBeanCallback;
import cn.tongchengyuan.util.LogUtil;
import com.style.base.BaseActivity;
import com.style.constant.Skip;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 王宗文 on 2016/6/20.
 */

public class SearchActivity extends BaseActivity {

    ActivitySearchBinding bd;
    private String search;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_search);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {

        bd.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search = bd.etSearch.getText().toString();
                if(!TextUtils.isEmpty(search)){
                    bd.tvSearch.setText(search);
                    bd.reSearch.setVisibility(View.VISIBLE);
                }else {
                    bd.tvSearch.setText("");
                    bd.reSearch.setVisibility(View.GONE);
                }
            }
        });

        bd.reSearch.setOnClickListener(new View.OnClickListener() {
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
