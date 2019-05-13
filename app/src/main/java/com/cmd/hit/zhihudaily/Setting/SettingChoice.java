package com.cmd.hit.zhihudaily.Setting;

import java.io.Serializable;

public class SettingChoice implements Serializable {
    private String Title;//设置选项内容
    private boolean isChecked;//设置选项状态；
    public SettingChoice(String title){
        Title = title;
        isChecked = false;
    }
    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
