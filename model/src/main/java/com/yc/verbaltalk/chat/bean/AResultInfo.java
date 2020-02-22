package com.yc.verbaltalk.chat.bean;

/**
 * Created by mayn on 2019/5/8.
 */

public class AResultInfo <T>  {
    public int code;

//    @JSONField(name = "msg")
    public String msg;
    public T data;

    @Override
    public String toString() {
        return "AResultInfo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}