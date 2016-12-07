package com.juns.wechat.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.juns.wechat.R;
import com.juns.wechat.bean.DynamicBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.manager.AccountManager;
import com.style.base.BaseToolbarActivity;
import com.style.base.BaseToolbarBtnActivity;
import com.style.rxAndroid.newwork.callback.RXNetBeanCallBack;
import com.style.rxAndroid.newwork.core.HttpAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import butterknife.Bind;


public class DynamicPublishActivity extends BaseToolbarBtnActivity {
    private static final String TAG_ADD = "addTag";

    @Bind(R.id.et_content)
    EditText etContent;
   /* @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_parent)
    RelativeLayout llParent;
    @BindView(R.id.iv_smile)
    CheckBox ivSmile;
    @BindView(R.id.rl_bottom_smile)
    LinearLayout rlBottomSmile;
    @BindView(R.id.face_pager)
    ViewPager facePager;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.face_ll)
    LinearLayout faceLl;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    private DynamicPublishImageAdapter adapter;
    private List<String> paths;
    protected boolean haveContent;
    private boolean haveImg;
    protected File photoFile;
    private AsyncTask<Void, Void, File[]> dealPicTask;
    private SelAvatarDialog dialog;

    private int mCurrentPage = 0;// 当前表情页
    private List<String> mFaceMapKeys;*/
   private UserBean curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_dynamic_publish;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        curUser = AccountManager.getInstance().getUser();
       /* setTitleCenterText(R.string.send_message);
        initFace();
        paths = null;
        paths = new ArrayList<>();
        paths.add(TAG_ADD);
        adapter = new DynamicPublishImageAdapter(DynamicPublishActivity.this, paths);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
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
        etContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent arg1) {
                // 这句话说的意思告诉父View我自己的事件我自己处理
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        etContent.addTextChangedListener(new TextWatcher() {
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

        //监听软键盘显示状态
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight / 3;
        //添加layout大小发生改变监听器
        llParent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    showToast("监听到软键盘弹起...");
                   *//* new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            faceLl.setVisibility(View.GONE);
                        }
                    }, 100);*//*
                  *//*  new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (rlBottomSmile.getVisibility() == View.GONE)
                                rlBottomSmile.setVisibility(View.VISIBLE);
                        }
                    }, 200);*//*
                    rlBottomSmile.setVisibility(View.VISIBLE);

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    showToast("监听到软件盘关闭...");
                    //如果是切换到表情面板而隐藏流量输入法，需要延迟判断表情面板是否显示，如果表情面板是关闭的，操作栏也关闭
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //如果表情面板是关闭的，操作栏也关闭
                            if (faceLl.getVisibility() == View.GONE)
                                rlBottomSmile.setVisibility(View.GONE);
                        }
                    }, 200);
                }
            }
        });
        ivSmile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (faceLl.getVisibility() == View.GONE) {
                    //隐藏输入法，打开表情面板
                    hideSoftMouse();
                    //延迟显示，先让输入法显示
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            faceLl.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                } else {
                    //隐藏表情面板，打开输入法
                    faceLl.setVisibility(View.GONE);
                    toggleSoftInput();
                }
            }
        });
        etContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ivSmile.setChecked(false);//还原表情状态
                faceLl.setVisibility(View.GONE);
                return false;
            }
        });*/
    }

    private void toggleSoftInput() {
        //CommUtil.toggleSoftInput(DynamicPublishActivity.this, etContent);
    }

    private void showSelPicPopupWindow(int position) {
       /* int count = adapter.getItemCount();
        if (position == count - 1) {
            if (dialog == null) {
                dialog = new SelAvatarDialog(DynamicPublishActivity.this, R.style.Dialog_NoTitle);
                dialog.setOnItemClickListener(new SelAvatarDialog.OnItemClickListener() {
                    @Override
                    public void OnClickCamera() {
                        photoFile = CommUtil.takePhoto(DynamicPublishActivity.this, Constants.DIR_APP_IMAGE_CAMERA, String.valueOf(System.currentTimeMillis()) + ".jpg");

                    }

                    @Override
                    public void OnClickPhoto() {
                        Intent intent = new Intent(DynamicPublishActivity.this, GallaryActivity.class);
                        int newCount = adapter.getItemCount();
                        ArrayList<String> cacheList = new ArrayList<>();
                        if (newCount > 1) {
                            for (int i = 0; i < newCount - 1; i++) {
                                cacheList.add(paths.get(i));
                            }
                        }
                        intent.putStringArrayListExtra("paths", cacheList);
                        intent.putExtra("maxNum", 9);
                        startActivityForResult(intent, Constants.REQUESTCODE_TAKE_LOCAL);
                    }

                    @Override
                    public void OnClickCancel() {

                    }
                });
            }
            dialog.show();
        }*/
    }

    @Override
    protected void onClickTitleRightView() {
        addUserDynamic();
    }

    @Override
    protected void onBackFinish() {
        openDeletePromptDialog();
    }

    private void openDeletePromptDialog() {
        showGiveUpEditDialog(new OnGiveUpEditDialogListener() {
            @Override
            public void onPositiveButton() {
                finish();
            }

            @Override
            public void onNegativeButton() {

            }
        });
    }
/*
    private void setHaveDynamic() {
        int number = adapter.getItemCount();
        if (number > 1)
            haveImg = true;
        else
            haveImg = false;
    }*/

    private void addUserDynamic() {
        String content = etContent.getText().toString();
        final DynamicBean dynamicBean = new DynamicBean();
        dynamicBean.setContent(content);
        dynamicBean.setPublisherId(curUser.getId());
        runTask(new RXNetBeanCallBack(DynamicBean.class) {
            @Override
            public Object doInBackground() {
                return HttpAction.addDynamic(dynamicBean);
            }

            @Override
            public void OnSuccess(Object object) {
                dismissProgressDialog();
                DynamicBean bean = (DynamicBean) object;
                bean.setUser(curUser);
            }

            @Override
            public void OnFailed(String message) {
                dismissProgressDialog();
                super.OnFailed(message);
            }
        });
       /* if (!haveContent && !haveImg) {
            showToast(R.string.say_something);
            return;
        }
        if (TextUtils.isEmpty(content)) {
            content = "发表图片";
        }*/
        //showProgressDialog(R.string.publishing);
      /*  UserDynamic ud = new UserDynamic();
        ud.setPublishAccount(curUser.getAccount());
        ud.setGroupId(curUser.getGroupId());
        ud.setContent(content);
        Params params = new Params(ud);
        params.put("mainAccount", curUser.getMainAccount());
        dealPicTask = new DealPicTask(params).execute();*/
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUESTCODE_TAKE_LOCAL:
                    if (data != null) {
                        ArrayList<String> newPaths = data.getStringArrayListExtra("paths");
                        paths.clear();
                        paths.addAll(newPaths);
                        paths.add(TAG_ADD);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case Constants.REQUESTCODE_TAKE_CAMERA:
                    if (photoFile.exists()) {
                        CommUtil.notifyUpdateGallary(this, photoFile);// 通知系统更新相册
                        String filePath = photoFile.getAbsolutePath();// 获取相片的保存路径
                        int size = paths.size();
                        if (size >= 10) {
                            showToast("最多上传9张图片");
                        } else {
                            int location = 0;
                            if (size >= 1)
                                location = size - 1;
                            paths.add(location, filePath);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        showToast(R.string.File_does_not_exist);
                    }
                    break;
            }
            setHaveDynamic();
        }
    }*/

    /*public class DealPicTask extends AsyncTask<Void, Void, File[]> {
        private Params params;

        public DealPicTask(Params params) {
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected File[] doInBackground(Void... params) {
            int num = adapter.getCount() - 1;
            File[] files = null;
            if (num > 0) {
                try {
                    files = new File[num];
                    for (int i = 0; i < num; i++) {
                        String path = (String) adapter.getItem(i);
                        if (path != null) {
                            int degree = PhotoUtil.readPictureDegree(path);
                            logE("degree", degree + "");
                            Bitmap bitmap0 = BitmapUtil.revitionImageSize(path, 960, 540, 1280);
                            Bitmap bitmap = AvatarUtil.rotaingBitmap(bitmap0, degree);
                            String name = String.valueOf(System.currentTimeMillis()) + ".jpg";
                            BitmapUtil.saveBitmap(Constants.DIR_APP_IMAGE_CACHE, name, bitmap, 30, true);
                            String newPath = Constants.DIR_APP_IMAGE_CACHE + "/" + name;
                            File file = new File(newPath);
                            if (file.exists())
                                files[i] = file;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!isCancelled()) {
                return files;
            }
            return null;
        }

        @Override
        protected void onPostExecute(File[] files) {
            params.putFiles(files);
            OkHttpUtil.post(OkHttpUtil.ADD_USER_DYNAMIC, params, new OkHttpJsonHandler() {
                @Override
                public void onSuccess(JSONObject response) {
                    hiddenProgressDialog();
                    try {
                        String code = response.optString("code");
                        if (OkHttpUtil.codeEqualsZero(code)) {
                            UserDynamic ud = JSON.parseObject(response.optString("ud"), UserDynamic.class);
                            if (null != ud) {
                                ud.setUser(curUser);
                                showToast(R.string.publish_success);
                                setResult(RESULT_OK, new Intent().putExtra(Skip.USERDYNAMIC_KEY, ud));
                                finish();
                            }
                        } else {
                            showToastMsg(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IOException e) {
                    hiddenProgressDialog();
                    super.onFailure(e);
                }
            });
        }
    }*/

    /*private void initFace() {
// 将表情map的key保存在数组中
        Set<String> keySet = FaceMap.getInstance().getFaceMap().keySet();
        mFaceMapKeys = new ArrayList<String>();
        mFaceMapKeys.addAll(keySet);

        List<View> lv = new ArrayList<>();
        for (int i = 0; i < FaceMap.NUM_PAGE; ++i)
            lv.add(getGridView(i));
        FacePageAdeapter adapter = new FacePageAdeapter(lv);
        facePager.setAdapter(adapter);
        facePager.setCurrentItem(mCurrentPage);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(Constant
                .getCompentID("id", "indicator"));
        indicator.setViewPager(facePager);
        adapter.notifyDataSetChanged();
        faceLl.setVisibility(View.GONE);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mCurrentPage = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // do nothing
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // do nothing
            }
        });
    }

    private GridView getGridView(int i) {
        GridView gv = new GridView(context);
        gv.setNumColumns(7);
        gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
        gv.setBackgroundColor(Color.TRANSPARENT);
        gv.setCacheColorHint(Color.TRANSPARENT);
        gv.setHorizontalSpacing(1);
        gv.setVerticalSpacing(1);
        gv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        gv.setGravity(Gravity.CENTER);
        gv.setAdapter(new FaceAdapter(context, i));
        gv.setOnTouchListener(forbidenScroll());
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == FaceMap.NUM) {// 删除键的位置
                    int selection = etContent.getSelectionStart();
                    String text = etContent.getText().toString();
                    if (selection > 0) {
                        String text2 = text.substring(selection - 1);
                        if ("]".equals(text2)) {
                            int start = text.lastIndexOf("[");
                            int end = selection;
                            etContent.getText().delete(start, end);
                            return;
                        }
                        etContent.getText().delete(selection - 1, selection);
                    }
                } else {
                    int count = mCurrentPage * FaceMap.NUM + arg2;
                    String emojiStr = mFaceMapKeys.get(count);
                    CharSequence sequence = FaceMap.getEmojiString(DynamicPublishActivity.this, emojiStr, 20);
                    int index = etContent.getSelectionStart();
                    Editable edit = etContent.getEditableText();//获取EditText的文字
                    edit.insert(index, sequence);//光标所在位置插入文字
                }
            }
        });
        return gv;
    }

    // 防止viewpage乱滚动
    private OnTouchListener forbidenScroll() {
        return new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    return true;
                }
                return false;
            }
        };
    }*/

}
