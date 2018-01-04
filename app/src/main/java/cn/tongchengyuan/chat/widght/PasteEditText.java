package cn.tongchengyuan.chat.widght;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import cn.tongchengyuan.chat.ui.ChatActivity;

/**
 * 自定义的textview，用来处理复制粘贴的消息
 * 
 */
public class PasteEditText extends EditText {
	private Context context;

	public PasteEditText(Context context) {
		super(context);
		this.context = context;
	}

	public PasteEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public PasteEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTextContextMenuItem(int id) {

		return super.onTextContextMenuItem(id);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		if (!TextUtils.isEmpty(text)
				&& text.toString().startsWith(ChatActivity.COPY_IMAGE)) {
			setText("");
		}
		// else if(!TextUtils.isEmpty(text)){
		// setText(SmileUtils.getSmiledText(getContext(),
		// text),BufferType.SPANNABLE);
		// }
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

}
