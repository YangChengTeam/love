package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/5/10.
 */

public class LoveByStagesDetailsBean {

        public int id;
        public Object is_open;
        public String status;
        public int category_id;
        public String category_name;
        public int create_time;
        public int is_collect;
        public Object check;
        public Object image;
        public int is_like;
        public Object is_top;
        public String morepic;
        public String post_content;

        @Override
        public String toString() {
                return "LoveByStagesDetailsBean{" +
                        "id=" + id +
                        ", is_open=" + is_open +
                        ", status='" + status + '\'' +
                        ", category_id=" + category_id +
                        ", category_name='" + category_name + '\'' +
                        ", create_time=" + create_time +
                        ", is_collect=" + is_collect +
                        ", check=" + check +
                        ", image=" + image +
                        ", is_like=" + is_like +
                        ", is_top=" + is_top +
                        ", morepic='" + morepic + '\'' +
                        ", post_content='" + post_content + '\'' +
                        '}';
        }
}
