package com.cmd.hit.zhihudaily.viewModel;

import com.cmd.hit.zhihudaily.model.repository.NewsRepository;
import com.cmd.hit.zhihudaily.ui.MainActivity;

/**
 * Created by PC-0775 on 2019/4/26.
 */

public class MainActivityModel {

    //News的数据仓库
    private NewsRepository repository;

    MainActivityModel(NewsRepository repository){
        this.repository = repository;
    }

    //离线缓存
    public void offlineCache(){
        //获取最新消息列表

        //逐个进行缓存
    }

    //自动清除缓存
    public void autoClearCache(){
        //检查缓存是否达到上限

        //用LRU算法找出需要清除的json项

        //清除json同时删除照片
    }
}
