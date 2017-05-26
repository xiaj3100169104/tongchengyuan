package com.juns.wechat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juns.wechat.bean.UserBean;
import com.juns.wechat.bean.UserPropertyBean;
import com.juns.wechat.dialog.SelectPhotoDialog;
import com.juns.wechat.net.request.HttpActionImpl;
import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;
import com.style.constant.FileConfig;
import com.style.constant.Skip;
import com.style.dialog.EditAlertDialog;
import com.style.net.core.NetDataBeanCallback;
import com.style.utils.CommonUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;

/**
 * Created by xiajun on 2017/5/9.
 */

public class PersonInfoEditExtendActivity extends BaseToolbarActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_person_info_extend;
        super.onCreate(savedInstanceState);
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

    private void saveInfo() {
        List<UserPropertyBean> list = new ArrayList<>();
        list.add(new UserPropertyBean(UserPropertyBean.KEY_MY_LABEL, getTagStr(tagViewMyLabel)));
        list.add(new UserPropertyBean(UserPropertyBean.KEY_INTEREST_SPORT, getTagStr(tagViewInterestSport)));
        list.add(new UserPropertyBean(UserPropertyBean.KEY_INTEREST_MUSIC, getTagStr(tagViewInterestMusic)));
        list.add(new UserPropertyBean(UserPropertyBean.KEY_INTEREST_FOOD, getTagStr(tagViewInterestFood)));
        list.add(new UserPropertyBean(UserPropertyBean.KEY_INTEREST_MOVIE, getTagStr(tagViewInterestMovie)));

        com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
        jsonArray.addAll(list);
        String value = jsonArray.toString();
        logE(TAG, value);
        HttpActionImpl.getInstance().updateUserProperty(TAG, value, new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                dismissProgressDialog();
                //AccountManager.getInstance().setUser(data);
                List<UserPropertyBean> userProperties = data.getUserProperties();

                finish();
            }

            @Override
            protected void onCodeFailure(String msg) {
                dismissProgressDialog();
                showToast(msg);
            }
        });
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

    @Override
    protected void initData() {
        setToolbarTitle(R.string.edit_extend_info);

    }

    @OnClick(R.id.layout_my_label)
    public void modifyInfo11() {
        openMulti("标签", getResources().getStringArray(R.array.my_label), tagViewMyLabel, R.color.tag_my_label, R.color.tag_my_label_bg, tvMyLabel);
    }

    @OnClick(R.id.layout_interest_sport)
    public void modifyInfo12() {
        openMulti("运动", getResources().getStringArray(R.array.sport), tagViewInterestSport, R.color.tag_sport, R.color.tag_sport_bg, tvInterestSport);
    }

    @OnClick(R.id.layout_interest_music)
    public void modifyInfo13() {
        openMulti("音乐", getResources().getStringArray(R.array.music), tagViewInterestMusic, R.color.tag_music, R.color.tag_music_bg, tvInterestMusic);
    }

    @OnClick(R.id.layout_interest_food)
    public void modifyInfo14() {
        openMulti("美食", getResources().getStringArray(R.array.food), tagViewInterestFood, R.color.tag_food, R.color.tag_food_bg, tvInterestFood);
    }

    @OnClick(R.id.layout_interest_movie)
    public void modifyInfo15() {
        openMulti("电影", getResources().getStringArray(R.array.movie), tagViewInterestMovie, R.color.tag_movie, R.color.tag_movie_bg, tvInterestMovie);
    }

    //@TargetApi(23)
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

        AlertDialog multiDialog = new AlertDialog.Builder(this)
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
                        ArrayList<Tag> newData = new ArrayList<Tag>();
                        for (int i = 0; i < checkedItems.length; i++) {
                            if (checkedItems[i]) {
                                Tag tag = new Tag(allData[i]);
                                tag.radius = 10f;
                                tag.tagTextColor = tagColor;
                                tag.layoutColor = getResources().getColor(tagColorBg);
                                tag.layoutColorPress = getResources().getColor(tagColorBg);
                                newData.add(tag);
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
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        multiDialog.show();
    }
}
