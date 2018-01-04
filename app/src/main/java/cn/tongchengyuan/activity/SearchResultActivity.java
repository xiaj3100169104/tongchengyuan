package cn.tongchengyuan.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import cn.tongchengyuan.adpter.SearchResultAdapter;
import cn.tongchengyuan.bean.UserBean;
import com.same.city.love.R;
import com.same.city.love.databinding.ActivitySearchResultBinding;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;

import java.util.List;

public class SearchResultActivity extends BaseToolbarActivity {

    ActivitySearchResultBinding bd;
    private List<UserBean> searchResults;
    private SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_search_result);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("搜索结果");
        searchResults = (List<UserBean>) getIntent().getSerializableExtra(Skip.KEY_SEARCH_RESULTS);

        searchResultAdapter = new SearchResultAdapter(this);
        searchResultAdapter.setData(searchResults);
        bd.lvSearchResultList.setAdapter(searchResultAdapter);

        bd.lvSearchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
