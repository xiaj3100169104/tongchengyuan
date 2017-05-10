package com.juns.wechat.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;
import com.style.dialog.EditAlertDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by xiajun on 2017/5/9.
 */

public class EditPersonInfoActivity extends BaseToolbarActivity {


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

    public int p = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_edit_userinfo;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        setToolbarTitle("编辑个人资料");
    }

    @OnClick(R.id.layout_base_info)
    public void modifyBaseInfo() {
        skip(EditBaseInfoActivity.class);
    }

    @OnClick(R.id.layout_emotion)
    public void modifyInfo3() {
        openSingleSelect(getResources().getStringArray(R.array.emotion), tvEmotion);
    }

    @OnClick(R.id.layout_education)
    public void modifyInfo4() {
        openSingleSelect(getResources().getStringArray(R.array.education), tvEducation);
    }

    @OnClick(R.id.layout_industry)
    public void modifyInfo() {
        openSingleSelect(getResources().getStringArray(R.array.industry), tvIndustry);
    }

    @OnClick(R.id.layout_work_area)
    public void modifyInfo2() {
        openSingleSelect(getResources().getStringArray(R.array.occupations), tvWorkArea);
    }

    private void openSingleSelect(final String[] strings, final TextView textView) {
        int checkedItem = 0;
        String sex = textView.getText().toString();
        if (!TextUtils.isEmpty(sex))
            for (int i = 0; i < strings.length; i++) {
                if (sex.endsWith(strings[i])) {
                    checkedItem = i;
                    break;
                }
            }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(strings, checkedItem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        p = which;
                    }
                }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(strings[p]);
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    @OnClick(R.id.layout_company_info)
    public void modifyInfo5() {
        openEdit(tvCompanyInfo);
    }

    @OnClick(R.id.layout_hometown_info)
    public void modifyInfo6() {
        openEdit(tvHometownInfo);
    }

    @OnClick(R.id.layout_my_heart)
    public void modifyInfo7() {
        openEdit(tvMyHeart);
    }

    private void openEdit(final TextView textView) {
        String name = textView.getText().toString();
        final EditAlertDialog editAlertDialog = new EditAlertDialog(this);
        editAlertDialog.setData(name, textView.getHint().toString());
        editAlertDialog.show(new EditAlertDialog.OnPromptListener() {
            @Override
            public void onPositiveButton(String s) {
                setText(textView, s);
                editAlertDialog.dismiss();
            }

            @Override
            public void onNegativeButton() {
                editAlertDialog.dismiss();
            }
        });

    }
}
