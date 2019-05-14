package com.cmd.hit.zhihudaily.ui.Setting;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cmd.hit.zhihudaily.R;
import com.cmd.hit.zhihudaily.other.PhotoCacheHelper;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private Toolbar setting_toolbar;

    private List<String> str2 = new ArrayList<String>();
    private List<String> str3 = new ArrayList<String>();;
    public List<SettingChoice> routine = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_main);

        setting_toolbar = findViewById(R.id.setting_toolbar);
        initToolBar();

        ListView lv=(ListView)findViewById(R.id.listview1);
        ListView lv2=(ListView)findViewById(R.id.listview2);
        ListView lv3=(ListView)findViewById(R.id.listview3);

        routine.add(new SettingChoice("自动离线下载"));
        routine.add(new SettingChoice("无图模式"));
        routine.add(new SettingChoice("大字号"));
        routine.add(new SettingChoice("推送消息"));
        routine.add(new SettingChoice("点评分享到微博"));
        str2.add("清除缓存");
        str2.add("检查更新");
        str3.add("意见反馈");

        AdapterView.OnItemClickListener listItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                SettingAdapter.ViewHolder viewHolder = (SettingAdapter.ViewHolder) view.getTag();
                viewHolder.cb.toggle();// 把CheckBox的选中状态改为当前状态的反,gridview确保是单一选中
                SettingAdapter.getIsSelected().put(position, viewHolder.cb.isChecked());//将CheckBox的选中状况记录下来
            }
        };
        SettingAdapter adapt1 = new SettingAdapter(SettingActivity.this,R.layout.setting_list,routine);
        //ArrayAdapter<String> adapt1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_checked,str1);
        BasicAdapter adapt2 = new BasicAdapter(SettingActivity.this,R.layout.settting_list2,str2);
        BasicAdapter adapt3 = new BasicAdapter(SettingActivity.this,R.layout.settting_list2,str3);



        lv.setAdapter(adapt1);
        lv2.setAdapter(adapt2);
        lv3.setAdapter(adapt3);

        lv.setOnItemClickListener(listItemClickListener);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(str2.get(arg2).equals("检查更新")){
                    Toast.makeText(SettingActivity.this,"功能待开发",Toast.LENGTH_SHORT).show();
                }
                if(str2.get(arg2).equals("清除缓存")){
                    PhotoCacheHelper.getInstance().clearDiskMemory();
                }

            }
        });
        lv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(SettingActivity.this,"功能待开发",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * 设置toolbar
    * */
    private void initToolBar()
    {
        setSupportActionBar(setting_toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("设置");
        }
    }
}
