package com.yc.verbaltalk.chat.bean;

/**
 * Created by sunshey on 2019/5/10.
 */

public class LoveByStagesDetailsBean {



        public int id;
        public String is_open;
        public String status;
        public int category_id; //栏目id
        public String category_name;
        public int create_time;
        public int is_collect;    //是否收藏
        public int check;
        public String image;
        public int is_like; //是否喜欢
        public int is_top;
        public String morepic;
        public String post_content;
        public int collect_num;

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
