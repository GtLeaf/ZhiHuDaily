package com.cmd.hit.zhihudaily.Setting;
import com.cmd.hit.zhihudaily.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingAdapter extends BaseAdapter {
    private Context mContext;
    private List<SettingChoice> options;
    private List<String> list_title;
    private static HashMap<Integer,Boolean> isSelected;
    private LayoutInflater inflater=null;
    private int resourceId;
    public SettingAdapter(Context context, int textViewResourceId, List<SettingChoice> objects){
        this.mContext = context;
        this.options = objects;
        inflater = LayoutInflater.from(context);
        resourceId = textViewResourceId;
        list_title =  new ArrayList<String>();
        isSelected = new HashMap<Integer, Boolean>();
        initData();
    }
    private void initData(){
        for(int i=0;i<options.size();i++){
           // list_title.pu= options.get(i).getTitle();
            list_title.add(options.get(i).getTitle());
            getIsSelected().put(i, options.get(i).isChecked());
        }
    }
    @Override
    public int getCount()
    {
        return options.size();
    }
    @Override
    public Object getItem(int position) {
        return options.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
        /*
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(resourceId, viewGroup, false);

        return view;
    }*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {//当第一次加载ListView控件时 convertView为空
            convertView = LayoutInflater.from(mContext).inflate(resourceId, parent, false);//所以当ListView控件没有滑动时都会执行这条语句
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv_title);
            holder.cb = (CheckBox) convertView.findViewById(R.id.cb_checkbox);
            convertView.setTag(holder);//为view设置标签
        } else {//取出holder
            holder = (ViewHolder) convertView.getTag();//the Object stored in this view as a tag
        }
        //设置list的textview显示
        holder.tv.setTextColor(Color.BLACK);
        holder.tv.setText(list_title.get(position));
        // 根据isSelected来设置checkbox的选中状况
            holder.cb.setChecked(getIsSelected().get(position));
            return convertView;
        }
    public static class ViewHolder {
        TextView tv;
        CheckBox cb;
    }
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }
    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SettingAdapter.isSelected = isSelected;
    }
}
