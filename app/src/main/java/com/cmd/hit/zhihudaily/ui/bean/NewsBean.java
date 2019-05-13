package com.cmd.hit.zhihudaily.ui.bean;

public class NewsBean extends TypeBean{
    private Integer id;
    private String title;
    private String image;

    public NewsBean() {
        setType(TypeBean.NEWS);
    }

    public NewsBean(String title, String image) {
        this.title = title;
        this.image = image;
        setType(TypeBean.NEWS);
    }

    public NewsBean(Integer id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
        setType(TypeBean.NEWS);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
