package cn.tongchengyuan.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.same.city.love.R;
import cn.tongchengyuan.bean.UserBean;
import com.style.manager.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class SearchResultAdapter extends BaseAdapter {
    private Context context;
    private List<UserBean> searchResults;
    public SearchResultAdapter(Context context){
        this.context = context;
    }

    public void setData(List<UserBean> searchResults){
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(searchResults == null) return 0;
        return searchResults.size();
    }

    @Override
    public Object getItem(int position) {
        if(searchResults == null) return null;
        return searchResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_search_result, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvNickName);
            viewHolder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserBean userBean = searchResults.get(position);
        viewHolder.tvName.setText(userBean.getUserName());
        ImageLoader.loadAvatar(context, viewHolder.ivIcon, userBean.getHeadUrl());
        viewHolder.tvDesc.setText(userBean.getSignature() == null ? "" : userBean.getSignature());
        return convertView;
    }

    static class ViewHolder{
        public ImageView ivIcon;
        public TextView tvName;
        public TextView tvDesc;
    }
}
