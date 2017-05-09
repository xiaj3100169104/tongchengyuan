package com.juns.wechat.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.same.city.love.R;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseToolbarActivity;
import com.style.dialog.SimpleEditDialog;
import com.style.dialog.SingleSelectDialog;

import java.util.ArrayList;

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

    private ArrayList<String> sexList;
    private SingleSelectDialog singleSelectDialog;
    private SimpleEditDialog simpleEditDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_edit_base_info;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        setToolbarTitle("个人信息");
        sexList = new ArrayList<>();
        sexList.add("男");
        sexList.add("女");
    }

    @OnClick(R.id.layout_sex)
    public void selectSex() {
        openSelectSex();
    }

    @OnClick(R.id.layout_name)
    public void editName() {
        openEditName();
    }


    @OnClick(R.id.layout_birthday)
    public void selectBirthday() {
        openSelectBirthday();
    }

    private void openSelectSex() {
        if (singleSelectDialog == null) {
            singleSelectDialog = new SingleSelectDialog(this, sexList);
            singleSelectDialog.adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<String>() {
                @Override
                public void onItemClick(int position, String data) {
                    tvSex.setText(data);
                    singleSelectDialog.dismiss();
                }
            });
        }
        singleSelectDialog.show();
    }

    private void openEditName() {
        String name = tvName.getText().toString();
        if (simpleEditDialog == null) {
            simpleEditDialog = new SimpleEditDialog(this, name, "姓名");
            simpleEditDialog.setOnItemClickListener(new SimpleEditDialog.OnItemClickListener() {
                @Override
                public void OnClickOk() {
                    String s = simpleEditDialog.getContent();
                    setText(tvName, s);
                    simpleEditDialog.dismiss();
                }

                @Override
                public void OnClickCancel() {

                }
            });
        }
        simpleEditDialog.show();

    }

    private void openSelectBirthday() {
       /* DatePicker datePicker = new DatePicker(this);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this);*/
    }
}
