package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.juns.wechat.bean.UserBasicInfo;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.bean.UserExtendInfo;
import com.juns.wechat.bean.UserPropertyBean;
import com.juns.wechat.greendao.mydao.GreenDaoManager;
import com.juns.wechat.helper.CacheDataHelper;
import com.juns.wechat.helper.CommonViewHelper;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.request.HttpActionImpl;
import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;
import com.style.event.EventCode;
import com.style.net.core.NetDataBeanCallback;
import com.style.utils.StringUtil;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;

public class PersonInfoShowActivity extends BaseToolbarActivity {
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNickName)
    TextView tvNickName;
    @Bind(R.id.ivSex)
    ImageView ivSex;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
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
    @Bind(R.id.layout_my_label)
    LinearLayout layoutMyLabel;
    @Bind(R.id.tv_interest_sport)
    TextView tvInterestSport;
    @Bind(R.id.layout_interest_sport)
    LinearLayout layoutInterestSport;
    @Bind(R.id.tv_interest_music)
    TextView tvInterestMusic;
    @Bind(R.id.layout_interest_music)
    LinearLayout layoutInterestMusic;
    @Bind(R.id.tv_interest_food)
    TextView tvInterestFood;
    @Bind(R.id.layout_interest_food)
    LinearLayout layoutInterestFood;
    @Bind(R.id.tv_interest_movie)
    TextView tvInterestMovie;
    @Bind(R.id.layout_interest_movie)
    LinearLayout layoutInterestMovie;
    @Bind(R.id.tv_question)
    TextView tvQuestion;
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
    private UserBasicInfo userBasicInfo;

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
        curUser = AccountManager.getInstance().getUser();
        userBasicInfo = GreenDaoManager.getInstance().queryUserBasic(curUser.getUserId());
        setUserHeadInfo(curUser);
        setUserBasicInfo(userBasicInfo);
        UserExtendInfo userExtendInfo = GreenDaoManager.getInstance().queryUserProperty(curUser.getUserId());
        if (userExtendInfo != null) {
            List<UserPropertyBean> userProperties = JSON.parseObject(userExtendInfo.getData(), new TypeReference<List<UserPropertyBean>>() {
            });
            setExtendInfo(userProperties);
        }
        //getUserDetailInfo();
    }

    private void setUserHeadInfo(UserBean userBean) {
        CommonViewHelper.setUserViewInfo(userBean, ivAvatar, tvNickName, ivSex, tvUserName, true);

    }

    private void setUserBasicInfo(UserBasicInfo u) {
        if (u != null) {
            this.userBasicInfo = u;
            setText(tvEmotion, u.getEmotion());
            setText(tvEducation, u.getEducation());
            setText(tvIndustry, u.getIndustry());
            setText(tvWorkArea, u.getWorkArea());
            setText(tvCompanyInfo, u.getCompanyInfo());
            setText(tvHometownInfo, u.getHometownInfo());
            setText(tvMyHeart, u.getMyHeart());
        }
    }

    public void getUserDetailInfo() {
        HttpActionImpl.getInstance().queryUserData(TAG, curUser.getUserId(), new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                dismissProgressDialog();
                //AccountManager.getInstance().setUser(data);
                List<UserPropertyBean> userProperties = data.getUserProperties();
                CacheDataHelper.putUserLabelCache(curUser.getUserId(), userProperties);
                setExtendInfo(userProperties);
            }

            @Override
            protected void onCodeFailure(String msg) {
                dismissProgressDialog();
                showToast(msg);
            }
        });
    }

    @OnClick(R.id.ivAvatar)
    public void showBigAvatar() {
        Intent intent = new Intent(this, PersonAvatarActivity.class);
        startActivity(intent);
    }

    @Subscriber(tag = EventCode.UPDATE_USER_HEAD)
    private void onDataChanged(UserBean data) {
        setUserHeadInfo(data);

    }

    @Subscriber(tag = EventCode.UPDATE_USER_BASIC)
    private void onDataChanged(UserBasicInfo data) {
        setUserBasicInfo(data);

    }

    @Subscriber(tag = EventCode.UPDATE_USER_LABEL)
    private void onDataChanged(List<UserPropertyBean> data) {
        setExtendInfo(data);
    }

    public void setExtendInfo(final List<UserPropertyBean> extendInfo) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (extendInfo != null) {
                    for (UserPropertyBean u : extendInfo) {
                        List<String> list = StringUtil.String2List(u.value, ",");
                        switch (u.key) {
                            case UserPropertyBean.KEY_MY_LABEL:
                                setLabelData(layoutMyLabel, tagViewMyLabel, list, R.color.tag_my_label, R.color.tag_my_label_bg, tvMyLabel);
                                break;
                            case UserPropertyBean.KEY_INTEREST_SPORT:
                                setLabelData(layoutInterestSport, tagViewInterestSport, list, R.color.tag_sport, R.color.tag_sport_bg, tvInterestSport);
                                break;
                            case UserPropertyBean.KEY_INTEREST_MUSIC:
                                setLabelData(layoutInterestMusic, tagViewInterestMusic, list, R.color.tag_music, R.color.tag_music_bg, tvInterestMusic);
                                break;
                            case UserPropertyBean.KEY_INTEREST_FOOD:
                                setLabelData(layoutInterestFood, tagViewInterestFood, list, R.color.tag_food, R.color.tag_food_bg, tvInterestFood);
                                break;
                            case UserPropertyBean.KEY_INTEREST_MOVIE:
                                setLabelData(layoutInterestMovie, tagViewInterestMovie, list, R.color.tag_movie, R.color.tag_movie_bg, tvInterestMovie);
                                break;
                        }
                    }
                }
            }
        }, 500);
    }

    private void setLabelData(ViewGroup layout, TagView tagView, List<String> list, int tagColor, int tagColorBg, TextView textView) {
        List<Tag> newData = new ArrayList<>();
        if (list != null) {
            for (String s : list) {
                if (!TextUtils.isEmpty(s)) {
                    Tag tag = new Tag(s);
                    tag.radius = 10f;
                    tag.tagTextColor = tagColor;
                    tag.layoutColor = getResources().getColor(tagColorBg);
                    tag.layoutColorPress = getResources().getColor(tagColorBg);
                    newData.add(tag);
                }
            }
        }
        tagView.removeAllTags();
        if (newData.size() > 0) {
            layout.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            tagView.setVisibility(View.VISIBLE);
            tagView.isTagOnClickEnable = false;
            tagView.addTags(newData);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

}
