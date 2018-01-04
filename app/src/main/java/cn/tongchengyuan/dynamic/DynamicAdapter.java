package cn.tongchengyuan.dynamic;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import cn.tongchengyuan.bean.CommentBean;
import cn.tongchengyuan.bean.DynamicBean;
import cn.tongchengyuan.chat.utils.SmileUtils;
import cn.tongchengyuan.greendao.mydao.GreenDaoManager;
import cn.tongchengyuan.helper.CommonViewHelper;
import cn.tongchengyuan.bean.UserBean;

import com.same.city.love.R;
import com.same.city.love.databinding.AdapterDynamicBinding;
import com.same.city.love.databinding.PopupwindowDiscussOptionBinding;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.manager.ImageLoader;
import com.style.utils.MyDateUtil;
import com.style.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.viewpager.ImagePagerActivity;


public class DynamicAdapter extends BaseRecyclerViewAdapter<DynamicBean> {

    private CommentPopupWindow menuWindow;
    private OnClickDiscussListener mDiscussListener;
    private OnClickImageListener mImageListener;
    private Map<String, UserBean> userMap = new HashMap<>();

    public DynamicAdapter(Context mContext, List<DynamicBean> list) {
        super(mContext, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterDynamicBinding bd = DataBindingUtil.inflate(mInflater, R.layout.adapter_dynamic, parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final int pos = position;
        final ViewHolder holder = (ViewHolder) viewHolder;
        final DynamicBean bean = getData(position);
        UserBean user;
        if (userMap.containsKey(bean.getPublisherId()))
            user = userMap.get(bean.getPublisherId());
        else {
            user = GreenDaoManager.getInstance().findByUserId(bean.getPublisherId());
            userMap.put(bean.getPublisherId(), user);
        }
        CommonViewHelper.setUserViewInfo(user, holder.bd.ivAvatar, holder.bd.tvNike, null, null, false);

        holder.bd.tvContent.setText(SmileUtils.getInstance().getSmiledText(bean.getContent()));
        holder.bd.tvTime.setText(MyDateUtil.getTimeFromNow(bean.getCreateDate(), MyDateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));

        holder.bd.ivDiscuss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuWindow(pos, bean, (ViewGroup) holder.itemView.getRootView(), v);
            }
        });
        //图片数据处理
        List<String> images = StringUtil.String2List(bean.getImages(), ",");
        holder.bd.glImages.removeAllViews();
        if (images != null && images.size() > 0) {
            holder.bd.glImages.setVisibility(View.VISIBLE);
            dealImages(mContext, holder.bd.glImages, images);
        } else {
            holder.bd.glImages.setVisibility(View.GONE);
        }

        //评论数据处理
        List<CommentBean> commentBeanList = bean.getCommentList();
        if (commentBeanList != null && commentBeanList.size() > 0) {
            holder.bd.layoutComment.setVisibility(View.VISIBLE);
            holder.bd.recyclerView.setVisibility(View.VISIBLE);
            CommentAdapter adapter = new CommentAdapter(mContext, commentBeanList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            holder.bd.recyclerView.setLayoutManager(linearLayoutManager);
            holder.bd.recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(int position, Object data) {
                    logE(TAG, pos + "--" + position);
                    mDiscussListener.OnClickReply(pos, position, data);
                }
            });
        } else {
            holder.bd.layoutComment.setVisibility(View.GONE);
            holder.bd.recyclerView.setVisibility(View.GONE);
        }
        holder.bd.executePendingBindings();
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

    private void dealImages(Context mContext, GridLayout glImages, final List<String> images) {

        int imageNum = 0;//pos % 9;
        if (images != null && images.size() > 0) {
            glImages.setVisibility(View.VISIBLE);
            imageNum = images.size();
        } else {
            glImages.setVisibility(View.GONE);
        }
        if (imageNum > 0) {
            if (imageNum == 1) {
                glImages.setRowCount(1);
                glImages.setColumnCount(1);
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
                ImageLoader.loadPictureByName(mContext, image, images.get(0));
                glImages.addView(image, params);
                image.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skip2imageBrowser(0, images);
                    }
                });
            } else if (imageNum > 1 && imageNum < 10) {
                logE("imagenum--", imageNum + "");
                glImages.setRowCount(imageNum / 3 + 1);
                glImages.setColumnCount(3);
                for (int i = 0; i < imageNum; i++) {
                    final int index = i;
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
                    ImageLoader.loadPictureByName(mContext, image, images.get(i));
                    glImages.addView(image, params);
                    image.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            skip2imageBrowser(index, images);
                        }
                    });
                }
            }
        }
    }

    private void skip2imageBrowser(int position, List<String> imgs) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putStringArrayListExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) imgs);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    private void showMenuWindow(final int pos, final Object data, ViewGroup rootView, View anchor) {
        if (menuWindow == null) {
            PopupwindowDiscussOptionBinding bd = DataBindingUtil.inflate(mInflater, R.layout.popupwindow_discuss_option, rootView, false);
            menuWindow = new CommentPopupWindow(mContext, bd);
            menuWindow.setAnimationStyle(R.style.Animations_ExtendsFromLeft);
        }
        //防止popupwindow还在内存中保持原来的位置监听器，都需重置监听器
        menuWindow.bd.llSupport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDiscussListener != null) {
                    menuWindow.dismiss();
                    mDiscussListener.OnClickSupport(pos, data);
                }
            }
        });
        menuWindow.bd.llComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDiscussListener != null) {
                    menuWindow.dismiss();
                    mDiscussListener.OnClickComment(pos, data);
                }
            }
        });
        int xoff = -dip2px(200);
        int yoff = -dip2px(25);
        menuWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public interface OnClickDiscussListener {
        void OnClickSupport(int position, Object data);

        void OnClickComment(int position, Object data);

        void OnClickReply(int position, int subPosition, Object data);
    }

    public void setOnClickDiscussListener(OnClickDiscussListener mDiscussListener) {
        this.mDiscussListener = mDiscussListener;
    }

    public interface OnClickImageListener {
        void OnClickImage(int position, Object data);
    }

    public void setOnClickImageListener(OnClickImageListener mImageListener) {
        this.mImageListener = mImageListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterDynamicBinding bd;

        public ViewHolder(AdapterDynamicBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

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