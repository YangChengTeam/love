package com.yc.verbaltalk.model.bean;

/**
 * Created by suns  on 2019/11/18 08:47.
 */
public class CourseInfo {


    /**
     * id : 191
     * title : 小白聊天特训
     * img : http://love.bshu.com/uploads/20191118/d5556981ca7ab3084431e58258f36e1c.png
     * goods_id : 31
     * price : 398.00
     * m_price : 0.66
     * vip_price : 0.66
     */

    private int id;
    private String title;
    private String img;
    private int goods_id;
    private String price;
    private String m_price;
    private String vip_price;
    private String content;
    private String contact_info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getM_price() {
        return m_price;
    }

    public void setM_price(String m_price) {
        this.m_price = m_price;
    }

    public String getVip_price() {
        return vip_price;
    }

    public void setVip_price(String vip_price) {
        this.vip_price = vip_price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }
}
