package cn.tongchengyuan.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityAboutAppBinding;
import com.style.base.BaseToolbarActivity;

import java.io.IOException;
import java.io.InputStream;


public class AboutProductActivity extends BaseToolbarActivity {

    ActivityAboutAppBinding bd;
    private static String text;//静态缓存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_about_app);
        super.setContentView(bd.getRoot());
        initData();
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
        bd.tvContent.setText(getNotNullText(text));
    }
}
