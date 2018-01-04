package cn.tongchengyuan.dynamic;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;

import com.dmcbig.mediapicker.PickerActivity;
import com.dmcbig.mediapicker.PickerConfig;
import com.dmcbig.mediapicker.entity.Media;

import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.manager.AccountManager;
import cn.tongchengyuan.bean.DynamicBean;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityDynamicPublishBinding;
import com.style.album.DynamicPublishImageAdapter;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseToolbarBtnActivity;
import com.style.constant.FileConfig;
import com.style.constant.Skip;
import com.style.dialog.PromptDialog;
import com.style.dialog.SelAvatarDialog;
import com.style.rxAndroid.RXTaskManager;
import com.style.rxAndroid.callback.RXTaskCallBack;
import com.style.utils.BitmapUtil;
import com.style.utils.CommonUtil;
import com.style.utils.MyDateUtil;
import com.style.utils.PictureUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DynamicPublishActivity extends BaseToolbarBtnActivity {
    private Media TAG_ADD;

    ActivityDynamicPublishBinding bd;
    private PublishDynamicHelper faceHelper;
    private DynamicPublishImageAdapter adapter;
    private List<Media> paths;
    protected boolean haveContent;
    private boolean haveImg;
    protected File photoFile;
    private SelAvatarDialog dialog;

    private UserBean curUser;
    PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_dynamic_publish);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    protected void initData() {
        getToolbarRightView().setText("发送");
        getToolbarRightView().setEnabled(false);

        curUser = AccountManager.getInstance().getUser();
        setToolbarTitle("");
        faceHelper = new PublishDynamicHelper(this, bd.etContent);
        faceHelper.onCreate();
        paths = new ArrayList<>();
        TAG_ADD = new Media();
        paths.add(TAG_ADD);
        adapter = new DynamicPublishImageAdapter(DynamicPublishActivity.this, paths);
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

        bd.etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (arg0.length() > 0)
                    haveContent = true;
                else
                    haveContent = false;
                setHaveDynamic();
            }
        });

    }

    @Override
    protected void onClickTitleRightView() {
        addUserDynamic();
    }

    @Override
    protected void onBackFinish() {
        openDeletePromptDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (promptDialog != null)
            promptDialog.dismiss();
    }

    private void openDeletePromptDialog() {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(getContext());
            promptDialog.setMessage("确定要放弃此次编辑吗？");
            promptDialog.setListener(new PromptDialog.OnPromptListener() {
                @Override
                public void onPositiveButton() {
                    finish();
                }

                @Override
                public void onNegativeButton() {

                }
            });
        }
        promptDialog.show();
    }

    private void setHaveDynamic() {
        int number = adapter.getItemCount();
        if (number > 1)
            haveImg = true;
        else
            haveImg = false;
        if (haveContent || haveImg)
            getToolbarRightView().setEnabled(true);
        else
            getToolbarRightView().setEnabled(false);
    }

    private void addUserDynamic() {
        showProgressDialog();
        RXTaskManager.getInstance().runTask(TAG, new RXTaskCallBack<File[]>() {
            @Override
            public File[] doInBackground() {
                return dealPicture();
            }

            @Override
            public void onSuccess(File[] object) {
                //dismissProgressDialog();
                File[] files = (File[]) object;
                if (files != null)
                    logE(TAG, "文件个数==" + files.length);
                startSend(files);
            }

            @Override
            public void onFailed(String message) {
                dismissProgressDialog();
            }
        });
    }

    private void startSend(File[] fileList) {
        String content = bd.etContent.getText().toString();
        /*HttpActionImpl.getInstance().addDynamic(TAG, content, files, new NetDataBeanCallback<DynamicBean>(DynamicBean.class) {
            @Override
            protected void onCodeSuccess(DynamicBean data) {
                dismissProgressDialog();
                data.setUser(curUser);
                setResult(RESULT_OK, new Intent().putExtra("sendDynamic", data));
                finish();
            }

            @Override
            protected void onCodeFailure(String msg) {
                dismissProgressDialog();
            }
        });*/
        DynamicBean data = new DynamicBean();
        data.setDynamicId(CommonUtil.getUUID());
        data.setPublisherId(curUser.getUserId());
        data.setCreateDate(MyDateUtil.longToString(System.currentTimeMillis(), MyDateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
        data.setContent(content);

        StringBuilder b = new StringBuilder("");
        if (fileList != null && fileList.length > 0) {
            for (int i = 0; i < fileList.length; i++) {
                File file = fileList[i];
                b.append(file.getName()).append(",");
            }
        }
        if (b.length() > 0)
            b.deleteCharAt(b.length() - 1);
        String value = b.toString();
        data.setImages(value);
        GreenDaoManager.getInstance().saveDynamic(data);
        dismissProgressDialog();
        data.setUser(curUser);
        setResult(RESULT_OK, new Intent().putExtra("sendDynamic", data));
        finish();
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

    public File[] dealPicture() {
        int num = adapter.getItemCount() - 1;
        File[] files = null;
        if (num > 0) {
            files = new File[num];
            for (int i = 0; i < num; i++) {
                String path = adapter.getData(i).path;
                if (path != null) {
                    int degree = PictureUtils.readPictureDegree(path);
                    logE("degree", degree + "");
                    Bitmap bitmap0 = BitmapUtil.compressImage(path, 960, 540, 1280);
                    Bitmap bitmap = BitmapUtil.rotate(bitmap0, degree);
                    String name = FileConfig.getUniqueFileName();
                    String newPath = FileConfig.DIR_CACHE + "/" + name;
                    logE(TAG, "path==" + newPath);
                    try {
                        BitmapUtil.saveBitmap(newPath, bitmap, 30);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File file = new File(newPath);
                    if (file.exists())
                        files[i] = file;
                }
            }
        }
        return files;
    }

    private void showSelPicPopupWindow(int position) {
        int count = adapter.getItemCount();
        if (position == count - 1) {
            if (dialog == null) {
                dialog = new SelAvatarDialog(DynamicPublishActivity.this);
                dialog.setOnItemClickListener(new SelAvatarDialog.OnItemClickListener() {
                    @Override
                    public void OnClickCamera() {
                        photoFile = CommonUtil.takePhoto(DynamicPublishActivity.this, FileConfig.DIR_IMAGE + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");

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

                        Intent intent = new Intent(DynamicPublishActivity.this, PickerActivity.class);
                        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE);//default image and video (Optional)
                        long maxSize = 188743680L;//long long long
                        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 180MB (Optional)
                        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 9);  //default 40 (Optional)
                        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, cacheList); // (Optional)
                        startActivityForResult(intent, PickerConfig.CODE_TAKE_ALBUM);

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
