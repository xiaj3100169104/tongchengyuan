package com.juns.wechat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.adpter.SearchResultAdapter;
import com.juns.wechat.annotation.Content;
import com.juns.wechat.annotation.Extra;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.common.BaseActivity;
import com.juns.wechat.common.ToolbarActivity;
import com.juns.wechat.xmpp.bean.SearchResult;
import com.style.constant.Skip;

import java.util.List;

public class SearchResultActivity extends ToolbarActivity {

    private ListView lvSearchResultList;
    //@Extra(name = ARG_SEARCH_RESULTS)
    private List<UserBean> searchResults;
    private SearchResultAdapter searchResultAdapter;

    @Override
    public void initData() {
        searchResults = (List<UserBean>) getIntent().getParcelableExtra(Skip.KEY_SEARCH_RESULTS);

        if(searchResults == null || searchResults.isEmpty()){
            throw new IllegalArgumentException("search results is null or empty!");
        }
        searchResultAdapter = new SearchResultAdapter(this);
        searchResultAdapter.setData(searchResults);
        lvSearchResultList = (ListView) findViewById(R.id.lvSearchResultList);
        lvSearchResultList.setAdapter(searchResultAdapter);

        lvSearchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserBean userBean = searchResults.get(position);
                Intent intent = new Intent(SearchResultActivity.this, UserInfoActivity.class);
                intent.putExtra(Skip.KEY_USER_NAME, userBean.getUserName());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_search_result;
        super.onCreate(savedInstanceState);
    }
}
