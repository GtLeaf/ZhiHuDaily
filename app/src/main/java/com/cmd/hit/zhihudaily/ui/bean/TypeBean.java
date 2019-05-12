package com.cmd.hit.zhihudaily.ui.bean;

public class TypeBean {
    public static final int DATE = 1001;
    public static final int NEWS = 1002;

    private int type;

    public TypeBean() {
    }

    public TypeBean(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
