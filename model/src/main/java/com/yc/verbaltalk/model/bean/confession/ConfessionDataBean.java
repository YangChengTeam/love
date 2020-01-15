package com.yc.love.model.bean.confession;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mayn on 2019/6/5.
 */

public class ConfessionDataBean  implements MultiItemEntity, Serializable {
    public ConfessionDataBean(int itemType,String title) {
        this.itemType = itemType;
        this.title = title;
    }

    public ConfessionDataBean() {
    }

    /**
     * add_time : 1558583411
     * author : 颜伯聪
     * build_num : 2887
     * c_img : http://nz.qqtn.com/zbsq/Upload/Model/5ce618a58dd1d.jpg
     * c_sub_title :
     * c_title :
     * class_id : 1
     * collect_num : 0
     * desp : 朋友圈爱心发射九宫格图片在线生成
     * field : [{"align":"0","def_val":"","font_color":"","font_name":"0","font_size":"","love_id":"4257","input_type":"4","is_hide":"0","name":"背景图","offset_x":"0","offset_y":"0","orientation":"0","restrain":"0","rotate":"0","sort":"0","temp_id":"2323","text_len_limit":"0","type":"0","val":"","x1":"227","x2":"446","y1":"223","y2":"444"}]
     * flag : 1
     * front_img : http://nz.qqtn.com/zbsq/Upload/Model/5cdb891096455.jpg
     * hot : 0
     * love_id : 2361
     * is_vip : 0
     * price : 0.00
     * small_img : http://nz.qqtn.com/zbsq/Upload/Model/5cdb89109325f.jpg
     * sort : 8
     * status : 1
     * sub_title :
     * tags : 拯救单身
     * template : [{"delay":"60","height":"671","love_id":"2323","img":"http://nz.qqtn.com/zbsq/Upload/Model/5cdb8a2aa2311.png","model_id":"2361","pid":"0","sort":"0","title":"","width":"671"}]
     * title : 九宫格爱心发射表白
     * type : 0
     */



    public int itemType=1;

    public String add_time;
    public String author;
    public String build_num;
    public String c_img;
    public String c_sub_title;
    public String c_title;
    public String class_id;
    public String collect_num;
    public String desp;
    public String flag;
    public String front_img;
    public String hot;
    public String id;
    public String is_vip;
    public String price;
    public String small_img;
    public String sort;
    public String status;
    public String sub_title;
    public String tags;
    public String title;
    public String type;
    public List<ConfessionFieldBean> field;
    public List<ConfessionTemplateBean> template;

    public static final int VIEW_TITLE = 0;
    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROG = 2;
    public static final int VIEW_DATA_OVER = 3;

    @Override
    public String toString() {
        return "ConfessionDataBean{" +
                "add_time='" + add_time + '\'' +
                ", author='" + author + '\'' +
                ", build_num='" + build_num + '\'' +
                ", c_img='" + c_img + '\'' +
                ", c_sub_title='" + c_sub_title + '\'' +
                ", c_title='" + c_title + '\'' +
                ", class_id='" + class_id + '\'' +
                ", collect_num='" + collect_num + '\'' +
                ", desp='" + desp + '\'' +
                ", flag='" + flag + '\'' +
                ", front_img='" + front_img + '\'' +
                ", hot='" + hot + '\'' +
                ", id='" + id + '\'' +
                ", is_vip='" + is_vip + '\'' +
                ", price='" + price + '\'' +
                ", small_img='" + small_img + '\'' +
                ", sort='" + sort + '\'' +
                ", status='" + status + '\'' +
                ", sub_title='" + sub_title + '\'' +
                ", tags='" + tags + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", field=" + field +
                ", template=" + template +
                '}';
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
