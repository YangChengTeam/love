package com.yc.love.model.constant;

public class StatusCode {
	//状态码（成功）
    public static final String STATUS_CODE_SUCCESS = "0";
    
    //状态码（失败）
    public static final String STATUS_CODE_FAIL = "-1";
    
    //状态码（无此服务名）
    public static final String STATUS_CODE_NO_SERVER = "10001";
    
    //状态码（json参数转换为java对象出错）
    public static final String STATUS_CODE_JSON_ERROR = "10002";
    
    //状态码（用户名密码错误）
    public static final String STATUS_CODE_USER_PWD_ERROR = "10003";
    
    //状态码（无此调用些服务的权限）
    public static final String STATUS_CODE_NO_SERVER_PERMISSION = "10004";
    
    //状态码（请求内容包体的有效性验证错误）
    public static final String STATUS_CODE_CONTENT_ERROR = "10005";
    
    //状态码（系统未知错误）
    public static final String STATUS_CODE_UNKNOWN_ERROR = "99999";
    
    //状态码（"您选择的车辆在"+beginTime+"——"+endTime+"时间范围内已被别人预约,请修改您的预约时间！"）
    public static final String STATUS_CODE_TIME_ERROR = "90001";
}
