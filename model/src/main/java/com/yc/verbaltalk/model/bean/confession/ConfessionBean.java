package com.yc.love.model.bean.confession;

import java.io.Serializable;
import java.util.List;

public class ConfessionBean implements Serializable {
    public String message;
    public boolean status;
    public List<ConfessionDataBean> data;

    @Override
    public String toString() {
        return "ConfessionBean{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }
}