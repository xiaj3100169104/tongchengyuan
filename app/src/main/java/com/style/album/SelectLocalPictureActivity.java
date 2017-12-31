package com.style.album;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;
import com.same.city.love.R;
import com.same.city.love.databinding.ActivitySelectLocalPictureBinding;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseToolbarActivity;
import com.style.constant.FileConfig;
import com.style.constant.Skip;
import com.style.dialog.SelAvatarDialog;
import com.style.utils.CommonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiajun on 2016/10/8.
 */
public class SelectLocalPictureActivity extends BaseToolbarActivity {
    ActivitySelectLocalPictureBinding bd;
    private Media TAG_ADD;
    private DynamicPublishImageAdapter adapter;
    private List<Media> paths;
    protected File photoFile;
    private SelAvatarDialog dialog;
    private boolean haveImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_select_local_picture);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("本地图片选择");
        paths = new ArrayList<>();
        TAG_ADD = new Media();
        paths.add(TAG_ADD);
        adapter = new DynamicPublishImageAdapter(this, paths);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bd.recyclerView.setLayoutManager(gridLayoutManager);
        bd.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<Media>() {
            @Override
            public void onItemClick(int position, Media data) {
                showSelPicPopupWindow(position);
            }
        });

        adapter.setOnDeleteClickListener(new DynamicPublishImageAdapter.OnDeleteClickListener() {
            @Override
            public void onItemClickDelete(int position) {
                paths.remove(position);
                adapter.notifyDataSetChanged();
                setHaveDynamic();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || resultCode == PickerConfig.RESULT_CODE) {
            switch (requestCode) {
                case PickerConfig.CODE_TAKE_ALBUM:
                    if (data != null) {
                        ArrayList<Media> newPaths = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
                        paths.clear();
                        paths.addAll(newPaths);
                        paths.add(TAG_ADD);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case Skip.CODE_TAKE_CAMERA:
                    if (photoFile.exists()) {
                        CommonUtil.notifyUpdateGallary(this, photoFile);// 通知系统更新相册
                        String filePath = photoFile.getAbsolutePath();// 获取相片的保存路径
                        int size = paths.size();
                        if (size >= 10) {
                            showToast("最多上传9张图片");
                        } else {
                            int location = 0;
                            if (size >= 1)
                                location = size - 1;
                            Media media = new Media();
                            media.path = filePath;
                            media.size = photoFile.length();
                            paths.add(location, media);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        showToast(R.string.File_does_not_exist);
                    }
                    break;
            }
            setHaveDynamic();
        }
    }

    private void setHaveDynamic() {
        int number = adapter.getItemCount();
        if (number > 1)
            haveImg = true;
        else
            haveImg = false;
    }

    private void showSelPicPopupWindow(int position) {
        int count = adapter.getItemCount();
        if (position == count - 1) {
            if (dialog == null) {
                dialog = new SelAvatarDialog(this);
                dialog.setOnItemClickListener(new SelAvatarDialog.OnItemClickListener() {
                    @Override
                    public void OnClickCamera() {
                        photoFile = CommonUtil.takePhoto(SelectLocalPictureActivity.this, FileConfig.DIR_IMAGE + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");

                    }

                    @Override
                    public void OnClickPhoto() {
                        int newCount = adapter.getItemCount();
                        ArrayList<Media> cacheList = new ArrayList<>();
                        if (newCount > 1) {
                            for (int i = 0; i < newCount - 1; i++) {
                                cacheList.add(paths.get(i));
                            }
                        }

                        Intent intent = new Intent(SelectLocalPictureActivity.this, PickerActivity.class);
                        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);//default image and video (Optional)
                        long maxSize = 188743680L;//long long long
                        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
                        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 9);  //default 40 (Optional)
                        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, cacheList); // (Optional)
                        SelectLocalPictureActivity.this.startActivityForResult(intent, PickerConfig.CODE_TAKE_ALBUM);
                    }

                    @Override
                    public void OnClickCancel() {

                    }
                });
            }
            dialog.show();
        }
    }
}