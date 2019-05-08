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
    public String mobile;
    public int vip_end_time;
    public int id;
    public int vip;

}
