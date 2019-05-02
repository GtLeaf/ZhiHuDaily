package com.cmd.hit.zhihudaily.model.bean;

import java.util.List;

/**
 * Created by PC-0775 on 2019/5/2.
 */

public class LatestNews {

    /**
     * date : 20190502
     * stories : [{"images":["https://pic1.zhimg.com/v2-4f0c85ee4584052ac8bea88d1ccf5a6c.jpg"],"type":0,"id":9454158,"ga_prefix":"050209","title":"我们来认识一下，肉眼可见的最古老的恒星"},{"images":["https://pic1.zhimg.com/v2-2d1c6341863268470cf9597ed031bfec.jpg"],"type":0,"id":9710689,"ga_prefix":"050207","title":"NBA 比赛中「三双」数据的价值是什么？"},{"images":["https://pic3.zhimg.com/v2-db0966bec483509f17a659da7bbaaf5e.jpg"],"type":0,"id":9710779,"ga_prefix":"050206","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic4.zhimg.com/v2-7555e56f4ae4be4e7916a3f578f3c20b.jpg","type":0,"id":9710753,"ga_prefix":"050107","title":"为什么很多人讨厌剧透？"},{"image":"https://pic4.zhimg.com/v2-66e3354dec06315d47299d0bf6a8ff7f.jpg","type":0,"id":9710786,"ga_prefix":"043008","title":"迄今为止你见过最惊艳的建筑是哪个？"},{"image":"https://pic2.zhimg.com/v2-0a077825bb0e2b3584ffaf1dc9c68465.jpg","type":0,"id":9710774,"ga_prefix":"042908","title":"呃\u2026\u2026漫威和 DC 的这些角色也太像了吧"},{"image":"https://pic2.zhimg.com/v2-5446dd9cea49630ddc0e193a524df669.jpg","type":0,"id":9710732,"ga_prefix":"042907","title":"漫威是否会毁了电影艺术？"},{"image":"https://pic3.zhimg.com/v2-45d4abb765fbbff50aa88f8c92825376.jpg","type":0,"id":9710722,"ga_prefix":"042909","title":"那些没能回来的小燕子，可能已经变成了烧烤"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic1.zhimg.com/v2-4f0c85ee4584052ac8bea88d1ccf5a6c.jpg"]
         * type : 0
         * id : 9454158
         * ga_prefix : 050209
         * title : 我们来认识一下，肉眼可见的最古老的恒星
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic4.zhimg.com/v2-7555e56f4ae4be4e7916a3f578f3c20b.jpg
         * type : 0
         * id : 9710753
         * ga_prefix : 050107
         * title : 为什么很多人讨厌剧透？
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
