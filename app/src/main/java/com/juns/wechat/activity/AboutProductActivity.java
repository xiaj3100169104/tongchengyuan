package com.juns.wechat.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.same.city.love.R;
import com.style.base.BaseToolbarActivity;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;

public class AboutProductActivity extends BaseToolbarActivity {

    @Bind(R.id.tv_content)
    TextView tvContent;
    private static String text;//静态缓存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_about_app;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        setToolbarTitle(R.string.about_app);
        if (TextUtils.isEmpty(text)) {
            try {
                // Return an AssetManager instance for your application's package
                InputStream is = getAssets().open("about_product.txt");
                int size = is.available();
                // Read the entire asset into a local byte buffer.
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                // Convert the buffer into a string.
                text = new String(buffer, "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Finally stick the string into the text view.
        setText(tvContent, text);
    }
}
