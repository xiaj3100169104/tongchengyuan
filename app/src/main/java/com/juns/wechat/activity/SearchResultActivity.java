package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.juns.wechat.R;
import com.juns.wechat.adpter.SearchResultAdapter;
import com.juns.wechat.bean.UserBean;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;

import java.util.List;

public class SearchResultActivity extends BaseToolbarActivity {

    private ListView lvSearchResultList;
    private List<UserBean> searchResults;
    private SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_search_result;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        setToolbarTitle("搜索结果");
        searchResults = (List<UserBean>) getIntent().getSerializableExtra(Skip.KEY_SEARCH_RESULTS);

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
                intent.putExtra(Skip.KEY_USER_ID, userBean.getUserId());
                startActivity(intent);
            }
        });
    }

}
