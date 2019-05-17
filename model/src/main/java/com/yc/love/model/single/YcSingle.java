package com.yc.love.model.single;

/**
 * Created by mayn on 2019/5/8.
 */

public class YcSingle {

    private YcSingle() {
    }

    private static YcSingle instanceLonginAccount;

    public static YcSingle getInstance() {
        if (instanceLonginAccount == null) {
            instanceLonginAccount = new YcSingle();
        }
        return instanceLonginAccount;
    }


    public String face;
    public String nick_name;
    public String name;
    public String mobile;  //登录界面手机号取SP 其他用内存数据
    public int vip_end_time;
    public int id=0;
    public int vip;

    public void clearAllData() {
        face = "";
        nick_name = "";
        name = "";
        mobile = "";
        vip_end_time = 0;
        id = 0;
        vip = 0;
    }

}
