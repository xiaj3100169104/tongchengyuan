package com.juns.wechat.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.juns.wechat.R;
import com.juns.wechat.bean.DynamicBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.utils.SmileUtils;
import com.juns.wechat.helper.CommonViewHelper;
import com.juns.wechat.manager.AccountManager;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.manager.ImageLoadManager;
import com.style.utils.DateUtil;
import com.style.utils.MyDateUtil;
import com.style.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DynamicAdapter extends BaseRecyclerViewAdapter {
    private PopupWindow menuWindow;
    private OnClickDiscussListener mDiscussListener;

    public DynamicAdapter(Context mContext, List list) {
        super(mContext, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItem(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.adapter_dynamic, parent, false));
    }

    @Override
    public void onBindItem(RecyclerView.ViewHolder viewHolder, int position, Object data) {
        final int pos = position;
        final ViewHolder holder = (ViewHolder) viewHolder;
        final DynamicBean bean = (DynamicBean) data;
        UserBean user = AccountManager.getInstance().getUser();//bean.getUser();
        CommonViewHelper.setUserViewInfo(user, holder.ivAvatar, holder.tvNike, null, null, false);

        holder.tvContent.setText(SmileUtils.getSmiledText(mContext, bean.getContent()));
        holder.tvTime.setText(MyDateUtil.getTimeFromNow(bean.getCreateDate(), MyDateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));

        holder.ivDiscuss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuWindow(pos, bean, holder.itemView.getRootView(), v);
            }
        });
        holder.glImages.removeAllViews();
        List<String> images = StringUtil.getList(bean.getImages(), ",");
        int imageNum = 0;//pos % 9;
        if (images != null && images.size() > 0)
            imageNum = images.size();
        if (imageNum>0){
            if (imageNum == 1) {
                holder.glImages.setRowCount(1);
                holder.glImages.setColumnCount(1);
                GridLayout.Spec rowSpec = GridLayout.spec(0);     //设置它的行和列
                GridLayout.Spec columnSpec = GridLayout.spec(0);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                //params.setGravity(Gravity.LEFT | Gravity.TOP);
                ImageView image = new ImageView(mContext);
                params.width = dip2px(160);
                params.height = dip2px(100);
                image.setLayoutParams(params);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //image.setImageResource(R.mipmap.empty_photo);
                ImageLoadManager.loadNormalPicture2(mContext, image, images.get(0));
                holder.glImages.addView(image, params);
            } else if (imageNum > 1 && imageNum < 10) {
                logE("imagenum--", imageNum + "");
                holder.glImages.setRowCount(imageNum / 3 + 1);
                holder.glImages.setColumnCount(3);
                for (int i = 0; i < imageNum; i++) {
                    int row = i / 3;
                    int column = (i - row * 3) % 3;
                    logE("row--", row + "");
                    logE("column--", column + "");

                    GridLayout.Spec rowSpec = GridLayout.spec(row);     //设置它的行和列
                    GridLayout.Spec columnSpec = GridLayout.spec(column);
                    GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
                    ImageView image = new ImageView(mContext);
                    params.width = dip2px(80);
                    params.height = dip2px(80);
                    params.leftMargin = dip2px(1);
                    params.topMargin = dip2px(1);
                    params.bottomMargin = dip2px(1);
                    params.rightMargin = dip2px(1);
                    image.setLayoutParams(params);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    //image.setImageResource(R.mipmap.empty_photo);
                    ImageLoadManager.loadNormalPicture2(mContext, image, images.get(i));
                    holder.glImages.addView(image, params);
                }
            }
        }
        /*if (null != testdataList) {
            holder.gvImage.setVisibility(View.VISIBLE);
            DynamicImgAdapter adapterImages = new DynamicImgAdapter(mContext, testdataList);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.gvImage.setLayoutManager(gridLayoutManager);
            holder.gvImage.setAdapter(adapterImages);
            adapterImages.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position, Object data) {
                    showToast("" + position);
                }
            });
        } else {
            holder.gvImage.setVisibility(View.GONE);
        }*/
      /*  CommentAdapter adapter = new CommentAdapter(mContext, testdataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                showToast("" + position);
            }
        });*/
        /*if (viewHolder instanceof Holder) {
            Holder holder = (Holder) viewHolder;
            User user = ud.getUser();
            final User curUser = MyApp.getInstance().getUser(user.getGroupId() - 1);
            if (user.getAccount().equals(curUser.getAccount())) {
                holder.tv_delete.setVisibility(View.VISIBLE);
            } else {
                holder.tv_delete.setVisibility(View.GONE);
            }
            UserHelper.inflateUserInfo(mContext, user, holder.iv_avatar, holder.iv_sex, holder.tv_nike);
            CommonUtil.setText(holder.tv_content, ud.getContent());
            CommonUtil.setTextGone(holder.tv_type, ud.getType());
            String time = MyDateUtil.getTimeFromNow(ud.getCreateTime(), MyDateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss);
            CommonUtil.setTextGone(holder.tv_time, time);
            boolean isZan = false;
            if (null != ud.getUserList()) {
                String account = curUser.getAccount();
                for (User user1 : ud.getUserList()) {
                    if (user1.getAccount().equals(account))
                        isZan = true;
                }
                holder.tv_zan.setText(String.valueOf(ud.getUserList().size()));
            } else {
                holder.tv_zan.setText(String.valueOf(0));
            }
            holder.tv_zan.setSelected(isZan);
            if (null != ud.getcList()) {
                CommonUtil.setText(holder.tv_cmt, String.valueOf(ud.getcList().size()));
            }
            final List<String> imgs = StringUtil.getStringList(ud.getImg(), ",");
            if (null != imgs) {
                holder.gv_images.setVisibility(View.VISIBLE);
                DynamicImgAdapter adapterImages = new DynamicImgAdapter(mContext, imgs);
                holder.gv_images.setAdapter(adapterImages);
            } else {
                holder.gv_images.setVisibility(View.GONE);
            }

            final boolean isSupport = holder.tv_zan.isSelected();
            holder.tv_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    openDeletePromptDialog(ud);
                }
            });
            holder.tv_zan.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    SupportNumber sn = new SupportNumber();
                    sn.setGroupId(ud.getUser().getGroupId());
                    sn.setMyAccount(ud.getUser().getAccount());
                    sn.setType(SupportNumber.TYPE_DYNAMIC);
                    sn.setAccount(curUser.getAccount());
                    sn.setId(ud.getId());
                    if (isSupport)
                        cancleSupport(ud, sn);
                    else
                        addSupport(ud, sn, curUser);
                }
            });
            holder.iv_avatar.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mContext.startActivity(new Intent(mContext, UserInfoActivity.class).putExtra(Skip.ACCOUNT_KEY, ud.getUser().getAccount()));
                }
            });
        }*/
    }

    private void showMenuWindow(final int pos, final Object data, View rootView, View v) {
        if (menuWindow == null) {
            View view = mInflater.inflate(R.layout.popupwindow_discuss_option, (ViewGroup) rootView, false);
            menuWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            menuWindow.setBackgroundDrawable(mContext.getResources().getDrawable(
                    R.drawable.divider_transparent));
            menuWindow.setAnimationStyle(R.style.Animations_ExtendsFromLeft);
            final MainMenuViewHolder mainMenu = new MainMenuViewHolder(view);
            mainMenu.llSupport.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDiscussListener != null) {
                        mDiscussListener.OnClickSupport(pos, data);
                        menuWindow.dismiss();
                    }
                }
            });
            mainMenu.llComment.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDiscussListener != null) {
                        mDiscussListener.OnClickComment(pos, data);
                        menuWindow.dismiss();
                    }
                }
            });
            mainMenu.llCollection.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDiscussListener != null) {
                        mDiscussListener.OnClickCollection(pos, data);
                        menuWindow.dismiss();
                    }
                }
            });
        }
        int xoff = -dip2px(200);
        int yoff = -dip2px(25);
        menuWindow.showAsDropDown(v, xoff, yoff);
    }

    class MainMenuViewHolder {
        @Bind(R.id.tv_support)
        TextView tvSupport;
        @Bind(R.id.ll_support)
        LinearLayout llSupport;
        @Bind(R.id.ll_comment)
        LinearLayout llComment;
        @Bind(R.id.ll_collection)
        LinearLayout llCollection;

        public MainMenuViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickDiscussListener {
        void OnClickSupport(int position, Object data);

        void OnClickComment(int position, Object data);

        void OnClickCollection(int position, Object data);
    }

    public void setOnClickDiscussListener(OnClickDiscussListener mDiscussListener) {
        this.mDiscussListener = mDiscussListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_avatar)
        ImageView ivAvatar;
        @Bind(R.id.tv_nike)
        TextView tvNike;
        @Bind(R.id.tv_content)
        TextView tvContent;
        /*@BindView(R.id.gv_image)
        RecyclerView gvImage;*/
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_delete)
        TextView tvDelete;
        @Bind(R.id.iv_discuss)
        ImageView ivDiscuss;
        @Bind(R.id.gl_images)
        GridLayout glImages;
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
/*
    private void openDeletePromptDialog(final UserDynamic ud) {
        final DlgDelNormal dlg = new DlgDelNormal(mContext, R.style.Dialog_NoActionBar);
        dlg.setMessage("确定要删除这条内容吗？");
        dlg.bt_positive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                delUserDynamic(ud);
                dlg.dismiss();
            }
        });
        dlg.show();
    }

    protected void delUserDynamic(final UserDynamic ud) {
        OkHttpUtil.post(OkHttpUtil.DEL_USER_DYNAMIC, ud, new OkHttpJsonHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String code = response.optString("code");
                    if (OkHttpUtil.codeEqualsZero(code)) {
                        list.remove(ud);
                        notifyDataSetChanged();
                    } else {
                        showToast(R.string.delete_fail);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void cancleSupport(final UserDynamic ud, final SupportNumber sn) {
        OkHttpUtil.post(OkHttpUtil.CANCLE_SUPPORT, sn, new OkHttpJsonHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String code = response.optString("code");
                    if (OkHttpUtil.codeEqualsZero(code)) {
                        List<User> users = ud.getUserList();
                        String account = sn.getAccount();
                        if (null != users) {
                            for (User u : users) {
                                if (u.getAccount().equals(account)) {
                                    users.remove(u);
                                    notifyDataSetChanged();
                                }
                            }
                        }
                    } else {
                        showToastMsg(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void addSupport(final UserDynamic ud, final SupportNumber sn, final User curUser) {
        OkHttpUtil.post(OkHttpUtil.ADD_SUPPORT, sn, new OkHttpJsonHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String code = response.optString("code");
                    if (OkHttpUtil.codeEqualsZero(code)) {
                        List<User> users = ud.getUserList();
                        if (null == users) {
                            List<User> newUsers = new ArrayList<User>();
                            newUsers.add(curUser);
                            ud.setUserList(newUsers);
                            notifyDataSetChanged();
                            return;
                        }
                        ud.getUserList().add(0, curUser);
                        notifyDataSetChanged();
                    } else {
                        showToastMsg(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }*/
}