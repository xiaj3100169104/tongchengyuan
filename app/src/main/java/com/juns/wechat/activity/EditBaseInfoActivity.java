package com.juns.wechat.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TextView;

import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;
import com.style.dialog.EditAlertDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by xiajun on 2017/5/9.
 */

public class EditBaseInfoActivity extends BaseToolbarActivity {

    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;

    private String[] sexList = {"男", "女"};
    private EditAlertDialog editAlertDialog;
    public int p = 0;
    private String strBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_edit_base_info;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        setToolbarTitle("个人信息");
    }

    @OnClick(R.id.layout_sex)
    public void selectSex() {
        openSelectSex(sexList, tvSex);
    }

    @OnClick(R.id.layout_name)
    public void editName() {
        openEditName();
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
                        //if (which == -1)
                        p = which;
                        textView.setText(strings[p]);
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void openEditName() {
        String name = tvName.getText().toString();
        editAlertDialog = new EditAlertDialog(this);
        editAlertDialog.setData(name, "姓名");
        editAlertDialog.show(new EditAlertDialog.OnPromptListener() {
            @Override
            public void onPositiveButton(String s) {
                setText(tvName, s);
                editAlertDialog.dismiss();
            }

            @Override
            public void onNegativeButton() {
                editAlertDialog.dismiss();
            }
        });

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
}
