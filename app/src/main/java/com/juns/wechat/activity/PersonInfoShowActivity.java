package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.ShowBigImage;
import com.juns.wechat.database.dao.DbDataEvent;
import com.juns.wechat.database.UserTable;
import com.juns.wechat.manager.AccountManager;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;

import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.Bind;
import me.kaede.tagview.TagView;

public class PersonInfoShowActivity extends BaseToolbarActivity {
    @Bind(R.id.layout_base_info)
    LinearLayout layoutBaseInfo;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.tv_constellation)
    TextView tvConstellation;
    @Bind(R.id.tv_industry)
    TextView tvIndustry;
    @Bind(R.id.layout_industry)
    LinearLayout layoutIndustry;
    @Bind(R.id.tv_work_area)
    TextView tvWorkArea;
    @Bind(R.id.layout_work_area)
    LinearLayout layoutWorkArea;
    @Bind(R.id.tv_company_info)
    TextView tvCompanyInfo;
    @Bind(R.id.layout_company_info)
    LinearLayout layoutCompanyInfo;
    @Bind(R.id.tv_hometown_info)
    TextView tvHometownInfo;
    @Bind(R.id.layout_hometown_info)
    LinearLayout layoutHometownInfo;
    @Bind(R.id.tv_my_heart)
    TextView tvMyHeart;
    @Bind(R.id.layout_my_heart)
    LinearLayout layoutMyHeart;
    @Bind(R.id.tv_my_label)
    TextView tvMyLabel;
    @Bind(R.id.layout_content_my_label)
    RelativeLayout layoutContentMyLabel;
    @Bind(R.id.layout_my_label)
    LinearLayout layoutMyLabel;
    @Bind(R.id.tv_interest_sport)
    TextView tvInterestSport;
    @Bind(R.id.layout_content_interest_sport)
    RelativeLayout layoutContentInterestSport;
    @Bind(R.id.layout_interest_sport)
    LinearLayout layoutInterestSport;
    @Bind(R.id.tv_interest_music)
    TextView tvInterestMusic;
    @Bind(R.id.layout_content_interest_music)
    RelativeLayout layoutContentInterestMusic;
    @Bind(R.id.layout_interest_music)
    LinearLayout layoutInterestMusic;
    @Bind(R.id.tv_interest_food)
    TextView tvInterestFood;
    @Bind(R.id.layout_content_interest_food)
    RelativeLayout layoutContentInterestFood;
    @Bind(R.id.layout_interest_food)
    LinearLayout layoutInterestFood;
    @Bind(R.id.tv_interest_movie)
    TextView tvInterestMovie;
    @Bind(R.id.layout_content_interest_movie)
    RelativeLayout layoutContentInterestMovie;
    @Bind(R.id.layout_interest_movie)
    LinearLayout layoutInterestMovie;
    @Bind(R.id.tv_question)
    TextView tvQuestion;
    @Bind(R.id.layout_content_question)
    RelativeLayout layoutContentQuestion;
    @Bind(R.id.layout_question)
    LinearLayout layoutQuestion;
    @Bind(R.id.tv_emotion)
    TextView tvEmotion;
    @Bind(R.id.layout_emotion)
    LinearLayout layoutEmotion;
    @Bind(R.id.tv_education)
    TextView tvEducation;
    @Bind(R.id.layout_education)
    LinearLayout layoutEducation;

    @Bind(R.id.tag_view_my_label)
    TagView tagViewMyLabel;
    @Bind(R.id.tag_view_interest_sport)
    TagView tagViewInterestSport;
    @Bind(R.id.tag_view_interest_music)
    TagView tagViewInterestMusic;
    @Bind(R.id.tag_view_interest_food)
    TagView tagViewInterestFood;
    @Bind(R.id.tag_view_interest_movie)
    TagView tagViewInterestMovie;

    private UserBean curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_person_info_show;
        super.onCreate(savedInstanceState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.person_info_show, menu);
        //menu.getItem(0).setTitle(R.string.edit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_photo_wall:
                skip(PersonInfoEditPhotoWallActivity.class);
                break;
            case R.id.edit_basic_info:
                skip(PersonInfoEditBasicActivity.class);
                break;
            case R.id.edit_extend_info:
                skip(PersonInfoEditExtendActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void initData() {
        setToolbarTitle(R.string.my_profile);
        //getToolbarRightView().setText("编辑");
        setData();
    }

    private void setData() {
        curUser = AccountManager.getInstance().getUser();
        setText(tvName, curUser.getNickName());
        //CommonViewHelper.setUserViewInfo(curUser, ivAvatar, tvNickName, null, tvUserName, false);

        String sex = curUser.getSex();
        //setText(UserBean.Sex.isMan(sex) ? "男" : "女");
    }

    //@OnClick(R.id.ivAvatar)
    public void showBigAvatar() {
        Intent intent = new Intent(this, ShowBigImage.class);
        intent.putExtra(Skip.KEY_IMG_NAME, curUser.getHeadUrl());
        startActivity(intent);
    }

    @Subscriber(tag = UserTable.TABLE_NAME)
    private void onDbDataChanged(DbDataEvent<UserBean> event) {
        if (event.action == DbDataEvent.REPLACE || event.action == DbDataEvent.UPDATE) {
            List<UserBean> updateData = event.data;
            if (updateData != null && !updateData.isEmpty()) {
                for (UserBean userBean : updateData) {
                    if (userBean.getUserName().equals(curUser.getUserName())) {
                        setData();
                    }
                }
            }
        }
    }


}
