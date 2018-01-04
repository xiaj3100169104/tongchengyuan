package uk.viewpager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.same.city.love.R;
import com.same.city.love.databinding.ActivityPagerImageListBinding;
import com.style.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class ImagePagerActivity extends BaseActivity {
    ActivityPagerImageListBinding bd;
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    private int pagerPosition;

    protected boolean isWrapContentView() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_pager_image_list);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    public void initData() {
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        ArrayList<String> urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
        List<Fragment> dataList = new ArrayList<>();
        for (String url : urls) {
            logE(TAG, "url=" + url);
            ImageDetailFragment f = ImageDetailFragment.newInstance(url);
            dataList.add(f);
        }
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), dataList);
        bd.pager.setAdapter(mAdapter);
        //如果不设置将导致超过3页后原fragment被销毁，数据丢失
        bd.pager.setOffscreenPageLimit(9);
        CharSequence text = getString(R.string.viewpager_indicator, 1, bd.pager.getAdapter().getCount());
        bd.indicator.setText(text);

        // 更新下标
        bd.pager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, bd.pager.getAdapter().getCount());
                bd.indicator.setText(text);
            }

        });
        bd.pager.setCurrentItem(pagerPosition);
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public List<Fragment> fileList;

        public ImagePagerAdapter(FragmentManager fm, List<Fragment> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fileList.get(position);
        }
    }


}