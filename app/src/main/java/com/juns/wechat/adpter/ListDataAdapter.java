package com.juns.wechat.adpter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by 王者 on 2016/8/7.
 */
public abstract class ListDataAdapter<T> extends BaseAdapter {
    protected List<T> listData;

    protected Context mContext;

    public ListDataAdapter(Context context){
        mContext = context;
    }

    public void setData(List<T> data){
        this.listData = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if(listData == null){
            return 0;
        }
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        if(listData == null){
            return null;
        }
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
