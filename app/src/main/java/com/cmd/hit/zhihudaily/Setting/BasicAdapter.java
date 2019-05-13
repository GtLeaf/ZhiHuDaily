package com.cmd.hit.zhihudaily.Setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cmd.hit.zhihudaily.R;

import java.util.ArrayList;
import java.util.List;

public class BasicAdapter extends ArrayAdapter<String> {
    private int resourceId;
    private List<String> temp;
    public BasicAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
        temp = new ArrayList<String>();
        for(int i=0;i<objects.size();i++){
            temp.add(objects.get(i));
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView Title = (TextView) view.findViewById(R.id.tv_title1);
        Title.setText(temp.get(position));
        return view;
    }
}
