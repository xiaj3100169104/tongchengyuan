package com.juns.wechat.dynamic;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juns.wechat.R;
import com.juns.wechat.bean.CommentBean;
import com.juns.wechat.bean.UserBean;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiajun on 2016/5/14.
 */
public class CommentAdapter extends BaseRecyclerViewAdapter {

    public CommentAdapter(Context context, List dataList) {
        super(context, dataList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItem(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.adapter_comment, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindItem(RecyclerView.ViewHolder viewHolder, int position, Object data) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        CommentBean commentBean = (CommentBean) data;
        UserBean user1 = commentBean.getCommentUser();
        UserBean user2 = commentBean.getReplyUser();
        String content = commentBean.getContent();
        SpannableStringBuilder builder;
        if (user2 == null)
            builder = addClickablePart(user1.getShowName(), content);
        else
            builder = addClickablePart(user1.getShowName(), user2.getShowName(), content);
        if (builder != null) {
            holder.tvComment.setText(builder, TextView.BufferType.SPANNABLE);
        } else
            holder.tvComment.setText("");
           /* adapter.setOnDefaultClickListener(new OnDefaultClickListener() {
                @Override
                public void onItemClick(int sub) {
                    if (listener != null) {
                        listener.onItemReplyClick(p, sub);
                    }
                }
            });*/
    }

    private SpannableStringBuilder addClickablePart(String cNike, String content) {
        if (TextUtils.isEmpty(cNike)) {
            return null;
        }
        String str = cNike + ":";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        setSpan(ssb, str.indexOf(cNike), cNike, null);
        return ssb.append(content);
    }

    private SpannableStringBuilder addClickablePart(String rNike, String cNike, String content) {
        if (TextUtils.isEmpty(rNike) || TextUtils.isEmpty(cNike)) {
            return null;
        }
        String split = "回复";
        String str = rNike + split + cNike + ":";
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        setSpan(ssb, str.indexOf(rNike), rNike, null);
        setSpan(ssb, rNike.length() + split.length(), cNike, null);
        return ssb.append(content);
    }

    private void setSpan(SpannableStringBuilder ssb, int start, final String name, final String account) {
        ssb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                int nikeColoer = mContext.getResources().getColor(R.color.blue);
                ds.setColor(nikeColoer); // 设置文本颜色
                // 去掉下划线
                ds.setUnderlineText(false);
            }
        }, start, start + name.length(), 0);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_comment)
        TextView tvComment;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
