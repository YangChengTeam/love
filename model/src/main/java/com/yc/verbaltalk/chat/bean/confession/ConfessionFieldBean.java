package com.yc.verbaltalk.chat.bean.confession;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mayn on 2019/6/5.
 */

public class ConfessionFieldBean implements Serializable {
    /**
     * align : 0
     * def_val :
     * font_color :
     * font_name : 0
     * font_size :
     * id : 4257
     * input_type : 4
     * is_hide : 0
     * name : 背景图
     * offset_x : 0
     * offset_y : 0
     * orientation : 0
     * restrain : 0
     * rotate : 0
     * sort : 0
     * temp_id : 2323
     * text_len_limit : 0
     * type : 0
     * val :
     * x1 : 227
     * x2 : 446
     * y1 : 223
     * y2 : 444
     */

    public String align;
    public String def_val;
    public String font_color;
    public String font_name;
    public String font_size;
    public String id;
    public String input_type;
    public String is_hide;
    public String name;
    public String offset_x;
    public String offset_y;
    public String orientation;
    public String restrain;
    public String rotate;
    public String sort;
    public String temp_id;
    public String text_len_limit;
    public String type;
    public String val;
    public String x1;
    public String x2;
    public String y1;
    public String y2;
    public List<ConfessionSelectBean> select;


    @Override
    public String toString() {
        return "ConfessionFieldBean{" +
                "align='" + align + '\'' +
                ", def_val='" + def_val + '\'' +
                ", font_color='" + font_color + '\'' +
                ", font_name='" + font_name + '\'' +
                ", font_size='" + font_size + '\'' +
                ", id='" + id + '\'' +
                ", input_type='" + input_type + '\'' +
                ", is_hide='" + is_hide + '\'' +
                ", name='" + name + '\'' +
                ", offset_x='" + offset_x + '\'' +
                ", offset_y='" + offset_y + '\'' +
                ", orientation='" + orientation + '\'' +
                ", restrain='" + restrain + '\'' +
                ", rotate='" + rotate + '\'' +
                ", sort='" + sort + '\'' +
                ", temp_id='" + temp_id + '\'' +
                ", text_len_limit='" + text_len_limit + '\'' +
                ", type='" + type + '\'' +
                ", val='" + val + '\'' +
                ", x1='" + x1 + '\'' +
                ", x2='" + x2 + '\'' +
                ", y1='" + y1 + '\'' +
                ", y2='" + y2 + '\'' +
                '}';
    }
}
