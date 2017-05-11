package com.juns.wechat.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TextView;

import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;
import com.style.dialog.EditAlertDialog;

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
        //DatePickerDialog datePicker = new DatePickerDialog(this);

    }
}
