package cn.tongchengyuan.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.tongchengyuan.bean.UserExtendInfo;
import cn.tongchengyuan.bean.UserPropertyBean;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityPersonInfoExtendBinding;
import com.style.base.BaseToolbarActivity;
import com.style.event.EventCode;
import com.style.event.EventManager;
import com.style.utils.CommonUtil;
import com.style.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;

/**
 * Created by xiajun on 2017/5/9.
 */

public class PersonInfoEditExtendActivity extends BaseToolbarActivity implements View.OnClickListener {

    ActivityPersonInfoExtendBinding bd;

    private UserBean curUser;
    private UserExtendInfo userExtendInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_person_info_extend);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_with_text, menu);
        menu.getItem(0).setTitle(R.string.save);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_text_only:
                saveInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {
        setToolbarTitle(R.string.edit_extend_info);
        curUser = AccountManager.getInstance().getUser();
        //setExtendInfo(CacheDataHelper.getUserLabelCache(curUser.getUserId()));
        userExtendInfo = GreenDaoManager.getInstance().queryUserProperty(curUser.getUserId());
        if (userExtendInfo != null) {
            List<UserPropertyBean> userProperties = JSON.parseObject(userExtendInfo.getData(), new TypeReference<List<UserPropertyBean>>() {
            });
            setExtendInfo(userProperties);
        }
        bd.extend.layoutMyLabel.setOnClickListener(this);
        bd.extend.layoutInterestSport.setOnClickListener(this);
        bd.extend.layoutInterestMusic.setOnClickListener(this);
        bd.extend.layoutInterestFood.setOnClickListener(this);
        bd.extend.layoutInterestMovie.setOnClickListener(this);
    }

    private void saveInfo() {
        if (userExtendInfo == null) {
            userExtendInfo = new UserExtendInfo();
            userExtendInfo.setUserExtendId(CommonUtil.getUUID());
            userExtendInfo.setUserId(curUser.getUserId());
        }
        List<UserPropertyBean> list = new ArrayList<>();
        list.add(new UserPropertyBean(UserPropertyBean.KEY_MY_LABEL, getTagStr(bd.extend.tagViewMyLabel)));
        list.add(new UserPropertyBean(UserPropertyBean.KEY_INTEREST_SPORT, getTagStr(bd.extend.tagViewInterestSport)));
        list.add(new UserPropertyBean(UserPropertyBean.KEY_INTEREST_MUSIC, getTagStr(bd.extend.tagViewInterestMusic)));
        list.add(new UserPropertyBean(UserPropertyBean.KEY_INTEREST_FOOD, getTagStr(bd.extend.tagViewInterestFood)));
        list.add(new UserPropertyBean(UserPropertyBean.KEY_INTEREST_MOVIE, getTagStr(bd.extend.tagViewInterestMovie)));

        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        jsonArray.addAll(list);
        String value = jsonArray.toString();
        userExtendInfo.setData(value);
        logE(TAG, value);

        dismissProgressDialog();
        GreenDaoManager.getInstance().save(userExtendInfo);
        //CacheDataHelper.putUserLabelCache(curUser.getUserId(), list);
        EventManager.getDefault().post(EventCode.UPDATE_USER_LABEL, list);
        finish();
        /*HttpActionImpl.getInstance().updateUserProperty(TAG, value, new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                dismissProgressDialog();
                //AccountManager.getInstance().setUser(data);
                CacheDataHelper.putUserLabelCache(curUser.getUserId(), list);
                EventManager.getDefault().post(list, EventCode.UPDATE_USER_LABEL);
                finish();
            }

            @Override
            protected void onCodeFailure(String msg) {
                dismissProgressDialog();
                showToast(msg);
            }
        });*/
    }

    private String getTagStr(TagView tagView) {
        List<Tag> tags = tagView.getTags();
        StringBuilder b = new StringBuilder("");
        for (Tag tag : tags) {
            b.append(tag.text).append(",");
        }
        if (b.length() > 0)
            b.deleteCharAt(b.length() - 1);
        String value = b.toString();
        return value;
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
                                setLabelData(bd.extend.tagViewMyLabel, list, R.color.tag_my_label, R.color.tag_my_label_bg, bd.extend.tvMyLabel);
                                break;
                            case UserPropertyBean.KEY_INTEREST_SPORT:
                                setLabelData(bd.extend.tagViewInterestSport, list, R.color.tag_sport, R.color.tag_sport_bg, bd.extend.tvInterestSport);
                                break;
                            case UserPropertyBean.KEY_INTEREST_MUSIC:
                                setLabelData(bd.extend.tagViewInterestMusic, list, R.color.tag_music, R.color.tag_music_bg, bd.extend.tvInterestMusic);
                                break;
                            case UserPropertyBean.KEY_INTEREST_FOOD:
                                setLabelData(bd.extend.tagViewInterestFood, list, R.color.tag_food, R.color.tag_food_bg, bd.extend.tvInterestFood);
                                break;
                            case UserPropertyBean.KEY_INTEREST_MOVIE:
                                setLabelData(bd.extend.tagViewInterestMovie, list, R.color.tag_movie, R.color.tag_movie_bg, bd.extend.tvInterestMovie);
                                break;
                        }
                    }
                }
            }
        }, 200);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_my_label:
                openMulti("标签", getResources().getStringArray(R.array.my_label), bd.extend.tagViewMyLabel, R.color.tag_my_label, R.color.tag_my_label_bg, bd.extend.tvMyLabel);
                break;
            case R.id.layout_interest_sport:
                openMulti("运动", getResources().getStringArray(R.array.sport), bd.extend.tagViewInterestSport, R.color.tag_sport, R.color.tag_sport_bg, bd.extend.tvInterestSport);
                break;
            case R.id.layout_interest_music:
                openMulti("音乐", getResources().getStringArray(R.array.music), bd.extend.tagViewInterestMusic, R.color.tag_music, R.color.tag_music_bg, bd.extend.tvInterestMusic);
                break;
            case R.id.layout_interest_food:
                openMulti("美食", getResources().getStringArray(R.array.food), bd.extend.tagViewInterestFood, R.color.tag_food, R.color.tag_food_bg, bd.extend.tvInterestFood);
                break;
            case R.id.layout_interest_movie:
                openMulti("电影", getResources().getStringArray(R.array.movie), bd.extend.tagViewInterestMovie, R.color.tag_movie, R.color.tag_movie_bg, bd.extend.tvInterestMovie);
        }
    }

    private void openMulti(final String title, final String[] allData, final TagView tagView, final int tagColor, final int tagColorBg, final TextView textView) {
        final boolean[] checkedItems = new boolean[allData.length];
        List<Tag> oldData = tagView.getTags();

        for (int i = 0; i < allData.length; i++) {
            for (Tag tag : oldData) {
                if (allData[i].equals(tag.text)) {
                    checkedItems[i] = true;
                    break;
                }
            }
        }

        AlertDialog multiDialog = new AlertDialog.Builder(this, R.style.Dialog_alert)
                .setTitle(title)
                .setMultiChoiceItems(allData, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> newData = new ArrayList<>();
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                newData.add(allData[i]);
                            }
                        }
                        setLabelData(tagView, newData, tagColor, tagColorBg, textView);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        multiDialog.show();
    }

    private void setLabelData(TagView tagView, List<String> list, int tagColor, int tagColorBg, TextView textView) {
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
            textView.setVisibility(View.GONE);
            tagView.setVisibility(View.VISIBLE);
            tagView.isTagOnClickEnable = false;
            tagView.addTags(newData);
        } else {
            tagView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

}
