package com.style.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.same.city.love.R;

import java.util.List;

public class SingleSelectDialog extends Dialog {
    public List<String> dataList;
    public RecyclerView recyclerView;
    public SingleSelectAdapter adapter;
    private Context context;

    public SingleSelectDialog(Context context, List<String> list) {
        this(context, R.style.Dialog_NoTitle, list);
    }

    public SingleSelectDialog(Context context, int theme, List<String> list) {
        super(context, theme);
        setOwnerActivity((Activity) context);
        this.context = context;
        this.dataList = list;
        init();
    }

    public void init() {
        setContentView(R.layout.dialog_single_select);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SingleSelectAdapter(context, dataList);
        recyclerView.setAdapter(adapter);

        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(dm.widthPixels - 200, window.getAttributes().height);
        // window.setWindowAnimations(R.style.Animations_SlideInFromBottom_OutToBottom);
    }
}
