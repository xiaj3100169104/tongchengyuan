package cn.tongchengyuan.chat.ui;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import cn.tongchengyuan.chat.adpter.ExpressionAdapter2;
import cn.tongchengyuan.chat.utils.SmileUtils;
import com.same.city.love.R;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by xiajun on 2017/5/24.
 */

public class CommonExpressionView {
    private static final String TAG = "CommonExpressionView";

    public static View getStickerView(final Context context, final EditText etInputText) {
        View view = View.inflate(context, R.layout.expression_gridview, null);
        RecyclerView gv = (RecyclerView) view.findViewById(R.id.rcv_sticker);
        gv.setLayoutManager(new GridLayoutManager(context, 7));
        List<SmileUtils.SmileBean> list = SmileUtils.getInstance().getSmileData2(context);
        ExpressionAdapter2 expressionAdapter = new ExpressionAdapter2(context, list);
        gv.setAdapter(expressionAdapter);
        expressionAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<SmileUtils.SmileBean>() {
            @Override
            public void onItemClick(int position, SmileUtils.SmileBean data) {
                CharSequence sequence = SmileUtils.getInstance().getSmiledText(data.key);
                int index = etInputText.getSelectionStart();
                Editable edit = etInputText.getEditableText();//获取EditText的文字
                edit.insert(index, sequence);//光标所在位置插入文字
            }
        });
        return view;
    }
}
