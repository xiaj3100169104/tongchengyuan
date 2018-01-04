package cn.tongchengyuan.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.same.city.love.R;
import com.same.city.love.databinding.FragmentProfileBinding;
import com.style.base.BaseFragment;

//我
public class Fragment_Profile extends BaseFragment {

    FragmentProfileBinding bd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return bd.getRoot();
    }


    @Override
    protected void initData() {
       getView().setVisibility(View.GONE);

    }

}