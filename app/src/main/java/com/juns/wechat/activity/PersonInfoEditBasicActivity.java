package com.juns.wechat.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juns.wechat.bean.UserBasicInfo;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.greendao.mydao.GreenDaoManager;
import com.juns.wechat.manager.AccountManager;
import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;
import com.style.dialog.EditAlertDialog;
import com.style.event.EventCode;
import com.style.event.EventManager;
import com.style.utils.CommonUtil;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by xiajun on 2017/5/9.
 */

public class PersonInfoEditBasicActivity extends BaseToolbarActivity {

    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;

    @Bind(R.id.tv_emotion)
    TextView tvEmotion;
    @Bind(R.id.layout_emotion)
    LinearLayout layoutEmotion;
    @Bind(R.id.tv_education)
    TextView tvEducation;
    @Bind(R.id.layout_education)
    LinearLayout layoutEducation;
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

    private UserBean curUser;

    private String[] sexList = {"男", "女"};
    private String strBirthday;
    private UserBasicInfo userBasicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_person_info_basic;
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

    @Override
    protected void initData() {
        setToolbarTitle(R.string.edit_basic_info);
        curUser = AccountManager.getInstance().getUser();

        String nickName = curUser.getNickName();
        setText(tvName, nickName == null ? "" : nickName);
        String sex = curUser.getSex();
        tvSex.setText(UserBean.Sex.isMan(sex) ? "男" : "女");
        setText(tvBirthday, curUser.getBirthday());

        userBasicInfo = GreenDaoManager.getInstance().queryUserBasic(curUser.getUserId());
        setUserBasicInfo(userBasicInfo);

    }

    private void setUserBasicInfo(UserBasicInfo u) {
        if (u != null) {
            setText(tvEmotion, u.getEmotion());
            setText(tvEducation, u.getEducation());
            setText(tvIndustry, u.getIndustry());
            setText(tvWorkArea, u.getWorkArea());
            setText(tvCompanyInfo, u.getCompanyInfo());
            setText(tvHometownInfo, u.getHometownInfo());
            setText(tvMyHeart, u.getMyHeart());
        }
    }

    @OnClick(R.id.layout_sex)
    public void selectSex() {
        openSelectSex(sexList, tvSex);
    }

    @OnClick(R.id.layout_name)
    public void editName() {
        openEdit(tvName);
    }


    @OnClick(R.id.layout_birthday)
    public void selectBirthday() {
        openSelectBirthday();
    }


    private void openSelectSex(final String[] strings, final TextView textView) {
        int checkedItem = 0;
        String value = textView.getText().toString();
        if (!TextUtils.isEmpty(value))
            for (int i = 0; i < strings.length; i++) {
                if (value.endsWith(strings[i])) {
                    checkedItem = i;
                    break;
                }
            }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(sexList, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int p = which;
                        textView.setText(strings[p]);
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void openSelectBirthday() {
        strBirthday = tvBirthday.getText().toString();
        Calendar calendar = Calendar.getInstance();
        if (TextUtils.isEmpty(strBirthday))
            calendar.set(1990, 0, 1);
        else {
            String[] s = strBirthday.split("-");
            int month = Integer.valueOf(s[1]);
            int month2 = month == 12 ? 0 : month - 1;
            calendar.set(Integer.valueOf(s[0]), month2, Integer.valueOf(s[2]));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this);
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    logE(TAG, "onDateSet-->year=" + year + " month=" + month + " dayOfMonth=" + dayOfMonth);
                    strBirthday = year + "-" + (month + 1) + "-" + dayOfMonth;
                    setBirthday();
                }
            });
            datePickerDialog.show();
        } else {
            DatePicker datePicker = new DatePicker(this);
            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    logE(TAG, "onDateChanged-->year=" + year + " month=" + monthOfYear + " dayOfMonth=" + dayOfMonth);
                    strBirthday = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                }
            });

            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setView(datePicker)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setBirthday();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            alertDialog.show();

        }
    }

    private void setBirthday() {
        tvBirthday.setText(strBirthday);

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

    public int p2 = 0;

    private void openSingleSelect(final String[] strings, final TextView textView) {
        int checkedItem = 0;
        String value = textView.getText().toString();
        if (!TextUtils.isEmpty(value))
            for (int i = 0; i < strings.length; i++) {
                if (value.endsWith(strings[i])) {
                    checkedItem = i;
                    break;
                }
            }
        AlertDialog singleDialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(strings, checkedItem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        p2 = which;
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText(strings[p2]);
                    }
                })
                .setNeutralButton("清空", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textView.setText("");
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        singleDialog.show();
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
        editAlertDialog.setData(name, textView.getHint());
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

    private void saveInfo() {

        String name = tvName.getText().toString();
        String sex = tvSex.getText().toString();
        String birthday = tvBirthday.getText().toString();
        String education = tvEducation.getText().toString();
        String emotion = tvEmotion.getText().toString();
        String industry = tvIndustry.getText().toString();
        String workArea = tvWorkArea.getText().toString();
        String companyInfo = tvCompanyInfo.getText().toString();
        String hometownInfo = tvHometownInfo.getText().toString();
        String myHeart = tvMyHeart.getText().toString();

        curUser.setNickName(name);
        curUser.setSex(sex);
        curUser.setBirthday(birthday);
        AccountManager.getInstance().setUser(curUser);
        EventManager.getDefault().post(EventCode.UPDATE_USER_HEAD, curUser);

        if (userBasicInfo == null)
            userBasicInfo = new UserBasicInfo(CommonUtil.getUUID(), curUser.getUserId(), education, emotion, industry, workArea, companyInfo, hometownInfo, myHeart);
        else
            userBasicInfo.setData(education, emotion, industry, workArea, companyInfo, hometownInfo, myHeart);
        GreenDaoManager.getInstance().save(userBasicInfo);
        EventManager.getDefault().post(EventCode.UPDATE_USER_BASIC, userBasicInfo);
        dismissProgressDialog();

        finish();
        /*curUser.set
        String s = JSON.toJSONString(u);
        logE(TAG, s);*/
        /*HttpActionImpl.getInstance().updateUser(TAG, UserBean.NICKNAME, nickName, new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                dismissProgressDialog();
                AccountManager.getInstance().setUser(data);
                EventManager.getDefault().post(data, EventCode.UPDATE_USER_BASIC);
                finish();
            }

            @Override
            protected void onCodeFailure(String msg) {
                dismissProgressDialog();
                showToast(msg);
            }
        });*/
    }

}
