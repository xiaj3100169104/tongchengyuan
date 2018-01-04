package cn.tongchengyuan.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import cn.tongchengyuan.bean.UserBasicInfo;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityPersonInfoBasicBinding;
import com.style.base.BaseToolbarActivity;
import com.style.dialog.EditAlertDialog;
import com.style.event.EventCode;
import com.style.event.EventManager;
import com.style.utils.CommonUtil;

import java.util.Calendar;


/**
 * Created by xiajun on 2017/5/9.
 */

public class PersonInfoEditBasicActivity extends BaseToolbarActivity implements View.OnClickListener {

    ActivityPersonInfoBasicBinding bd;
    private UserBean curUser;

    private String[] sexList = {"男", "女"};
    private String strBirthday;
    private UserBasicInfo userBasicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_person_info_basic);
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
        setToolbarTitle(R.string.edit_basic_info);
        curUser = AccountManager.getInstance().getUser();

        String nickName = curUser.getNickName();
        bd.tvName.setText(getNotNullText(nickName));
        String sex = curUser.getSex();
        bd.tvSex.setText(UserBean.Sex.isMan(sex) ? "男" : "女");
        bd.tvBirthday.setText(getNotNullText(curUser.getBirthday()));

        userBasicInfo = GreenDaoManager.getInstance().queryUserBasic(curUser.getUserId());
        setUserBasicInfo(userBasicInfo);

        bd.layoutSex.setOnClickListener(this);
        bd.layoutName.setOnClickListener(this);
        bd.layoutBirthday.setOnClickListener(this);
        bd.basicInfo.layoutEmotion.setOnClickListener(this);
        bd.basicInfo.layoutEducation.setOnClickListener(this);
        bd.basicInfo.layoutIndustry.setOnClickListener(this);
        bd.basicInfo.layoutWorkArea.setOnClickListener(this);
        bd.basicInfo.layoutCompanyInfo.setOnClickListener(this);
        bd.basicInfo.layoutHometownInfo.setOnClickListener(this);
        bd.basicInfo.tvMyHeart.setOnClickListener(this);

    }

    private void setUserBasicInfo(UserBasicInfo u) {
        if (u != null) {
            bd.basicInfo.tvEmotion.setText(getNotNullText(u.getEmotion()));
            bd.basicInfo.tvEducation.setText(getNotNullText(u.getEducation()));
            bd.basicInfo.tvIndustry.setText(getNotNullText(u.getIndustry()));
            bd.basicInfo.tvWorkArea.setText(getNotNullText(u.getWorkArea()));
            bd.basicInfo.tvCompanyInfo.setText(getNotNullText(u.getCompanyInfo()));
            bd.basicInfo.tvHometownInfo.setText(getNotNullText(u.getHometownInfo()));
            bd.basicInfo.tvMyHeart.setText(getNotNullText(u.getMyHeart()));
        }
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
        strBirthday = bd.tvBirthday.getText().toString();
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
        bd.tvBirthday.setText(strBirthday);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_sex:
                openSelectSex(sexList, bd.tvSex);
                break;
            case R.id.layout_name:
                openEdit(bd.tvName);
                break;
            case R.id.layout_birthday:
                openSelectBirthday();
                break;
            case R.id.layout_emotion:
                openSingleSelect(getResources().getStringArray(R.array.emotion), bd.basicInfo.tvEmotion);
                break;
            case R.id.layout_education:
                openSingleSelect(getResources().getStringArray(R.array.education), bd.basicInfo.tvEducation);
                break;
            case R.id.layout_industry:
                openSingleSelect(getResources().getStringArray(R.array.industry), bd.basicInfo.tvIndustry);
                break;
            case R.id.layout_work_area:
                openSingleSelect(getResources().getStringArray(R.array.occupations), bd.basicInfo.tvWorkArea);
                break;
            case R.id.layout_company_info:
                openEdit(bd.basicInfo.tvCompanyInfo);
                break;
            case R.id.layout_hometown_info:
                openEdit(bd.basicInfo.tvHometownInfo);
                break;
            case R.id.layout_my_heart:
                openEdit(bd.basicInfo.tvMyHeart);
                break;
        }
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
        AlertDialog singleDialog = new AlertDialog.Builder(this, R.style.Dialog_alert)
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


    private void openEdit(final TextView textView) {
        String name = textView.getText().toString();
        final EditAlertDialog editAlertDialog = new EditAlertDialog(this);
        editAlertDialog.setData(name, textView.getHint());
        editAlertDialog.show(new EditAlertDialog.OnPromptListener() {
            @Override
            public void onPositiveButton(String s) {
                textView.setText(getNotNullText(s));
                editAlertDialog.dismiss();
            }

            @Override
            public void onNegativeButton() {
                editAlertDialog.dismiss();
            }
        });

    }

    private void saveInfo() {

        String name = bd.tvName.getText().toString();
        String sex = bd.tvSex.getText().toString();
        String birthday = bd.tvBirthday.getText().toString();
        String education = bd.basicInfo.tvEducation.getText().toString();
        String emotion = bd.basicInfo.tvEmotion.getText().toString();
        String industry = bd.basicInfo.tvIndustry.getText().toString();
        String workArea = bd.basicInfo.tvWorkArea.getText().toString();
        String companyInfo = bd.basicInfo.tvCompanyInfo.getText().toString();
        String hometownInfo = bd.basicInfo.tvHometownInfo.getText().toString();
        String myHeart = bd.basicInfo.tvMyHeart.getText().toString();

        curUser.setNickName(name);
        curUser.setSex(sex);
        curUser.setBirthday(birthday);
        GreenDaoManager.getInstance().save(curUser);

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
