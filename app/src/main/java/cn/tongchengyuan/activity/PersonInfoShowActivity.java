package cn.tongchengyuan.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.tongchengyuan.net.request.HttpActionImpl;
import cn.tongchengyuan.bean.UserBasicInfo;
import cn.tongchengyuan.bean.UserBean;
import cn.tongchengyuan.bean.UserExtendInfo;
import cn.tongchengyuan.bean.UserPropertyBean;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.helper.CacheDataHelper;
import cn.tongchengyuan.helper.CommonViewHelper;
import cn.tongchengyuan.manager.AccountManager;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityPersonInfoShowBinding;
import com.style.base.BaseToolbarActivity;
import com.style.event.EventCode;
import com.style.net.core.NetDataBeanCallback;
import com.style.utils.StringUtil;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;

public class PersonInfoShowActivity extends BaseToolbarActivity {
    ActivityPersonInfoShowBinding bd;

    private UserBean curUser;
    private UserBasicInfo userBasicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_person_info_show);
        super.setContentView(bd.getRoot());
        initData();
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
        bd.ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBigAvatar();
            }
        });
    }

    private void setUserHeadInfo(UserBean userBean) {
        CommonViewHelper.setUserViewInfo(userBean, bd.ivAvatar, bd.tvNickName, bd.ivSex, bd.tvUserName, true);

    }

    private void setUserBasicInfo(UserBasicInfo u) {
        if (u != null) {
            this.userBasicInfo = u;
            bd.basic.tvEmotion.setText(getNotNullText(u.getEmotion()));
            bd.basic.tvEducation.setText(getNotNullText(u.getEducation()));
            bd.basic.tvIndustry.setText(getNotNullText(u.getIndustry()));
            bd.basic.tvWorkArea.setText(getNotNullText(u.getWorkArea()));
            bd.basic.tvCompanyInfo.setText(getNotNullText(u.getCompanyInfo()));
            bd.basic.tvHometownInfo.setText(getNotNullText(u.getHometownInfo()));
            bd.basic.tvMyHeart.setText(getNotNullText(u.getMyHeart()));
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
                                setLabelData(bd.extend.layoutMyLabel, bd.extend.tagViewMyLabel, list, R.color.tag_my_label, R.color.tag_my_label_bg, bd.extend.tvMyLabel);
                                break;
                            case UserPropertyBean.KEY_INTEREST_SPORT:
                                setLabelData(bd.extend.layoutInterestSport, bd.extend.tagViewInterestSport, list, R.color.tag_sport, R.color.tag_sport_bg, bd.extend.tvInterestSport);
                                break;
                            case UserPropertyBean.KEY_INTEREST_MUSIC:
                                setLabelData(bd.extend.layoutInterestMusic, bd.extend.tagViewInterestMusic, list, R.color.tag_music, R.color.tag_music_bg, bd.extend.tvInterestMusic);
                                break;
                            case UserPropertyBean.KEY_INTEREST_FOOD:
                                setLabelData(bd.extend.layoutInterestFood, bd.extend.tagViewInterestFood, list, R.color.tag_food, R.color.tag_food_bg, bd.extend.tvInterestFood);
                                break;
                            case UserPropertyBean.KEY_INTEREST_MOVIE:
                                setLabelData(bd.extend.layoutInterestMovie, bd.extend.tagViewInterestMovie, list, R.color.tag_movie, R.color.tag_movie_bg, bd.extend.tvInterestMovie);
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
