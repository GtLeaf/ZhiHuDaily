package com.cmd.hit.zhihudaily.ui.bean;

public class DateBean extends TypeBean {
    private String date;
    private String week;

    public DateBean() {
    }

    public DateBean(String date, String week) {
        this.date = date;
        this.week = week;
        setType(TypeBean.DATE);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
