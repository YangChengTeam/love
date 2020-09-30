package com.yc.verbaltalk.model.single;

import java.util.List;

/**
 * Created by sunshey on 2019/5/8.
 */

public class YcSingle {

    private YcSingle() {
    }

    private static YcSingle instanceLonginAccount;

    public static YcSingle getInstance() {
        if (instanceLonginAccount == null) {
            synchronized (YcSingle.class) {
                if (instanceLonginAccount == null) {
                    instanceLonginAccount = new YcSingle();
                }
            }
        }
        return instanceLonginAccount;
    }


    public List<String> connectionTypeList;  //用于网络状态改变后，未经历生命周期的UI组件获取当前网络状态



}
