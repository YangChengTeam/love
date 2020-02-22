package com.yc.verbaltalk.chat.bean.event;

/**
 * Created by Administrator on 2018/6/28.
 */

public class EventBusWxPayResult {

    public int code;
    public String mess;

    public EventBusWxPayResult(int code, String mess) {
        this.code = code;
        this.mess = mess;
    }
}
