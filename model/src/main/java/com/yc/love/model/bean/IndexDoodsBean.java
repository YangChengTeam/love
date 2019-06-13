package com.yc.love.model.bean;

import java.io.Serializable;

/**
 * Created by mayn on 2019/5/14.
 */

public class IndexDoodsBean  implements Serializable {

    /**
     * app_id : 0
     * desp : 最低0.5月/天
     * id : 4
     * img :
     * m_price : 198
     * name : 购买全年vip
     * pay_price : 198
     * price : 198
     * sort : 0
     * status : 1
     * type_id : 1
     * type_relate_val : 0
     * unit : 月
     * use_time_limit : 12
     * vip_price : 198
     */

    public int app_id;
    public String desp;
    public int id;
    public String img;
    public String m_price;
    public String name;
    public String pay_price;
    public String price;
    public int sort;
    public int status;
    public int type_id;
    public int type_relate_val;
    public String unit;
    public int use_time_limit;
    public String vip_price;

    @Override
    public String toString() {
        return "IndexDoodsBean{" +
                "app_id=" + app_id +
                ", desp='" + desp + '\'' +
                ", id=" + id +
                ", img='" + img + '\'' +
                ", m_price='" + m_price + '\'' +
                ", name='" + name + '\'' +
                ", pay_price='" + pay_price + '\'' +
                ", price='" + price + '\'' +
                ", sort=" + sort +
                ", status=" + status +
                ", type_id=" + type_id +
                ", type_relate_val=" + type_relate_val +
                ", unit='" + unit + '\'' +
                ", use_time_limit=" + use_time_limit +
                ", vip_price='" + vip_price + '\'' +
                '}';
    }
}
