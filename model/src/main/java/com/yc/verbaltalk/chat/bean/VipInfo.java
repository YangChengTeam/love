package com.yc.verbaltalk.chat.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/3/7.
 */

public class VipInfo {
    @JSONField(name = "vip_id")
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JSONField(name = "vip_sub_num")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    @JSONField(name = "vip_end_time")
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @JSONField(name = "is_reg")
    private boolean isRegister;

    @JSONField(name = "vip_test_hour")
    private int tryHour;

    private int status;

    public boolean isRegister() {
        return isRegister;
    }

    public void setRegister(boolean register) {
        isRegister = register;
    }

    public int getTryHour() {
        return tryHour;
    }

    public void setTryHour(int tryHour) {
        this.tryHour = tryHour;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
