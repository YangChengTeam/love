package yc.com.rthttplibrary.bean;

import com.alibaba.fastjson.annotation.JSONField;


public class ResultInfo<T> {
    public int code;

    @JSONField(name = "msg")
    public String message;
    public T data;

}
