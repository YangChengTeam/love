package com.yc.verbaltalk.chat.bean.event;

import java.util.List;

/**
 * Created by sunshey on 2019/5/18.
 */

public class NetWorkChangT3Bean {
    public List<String> connectionTypeList;

    public NetWorkChangT3Bean(List<String> connectionTypeList) {
        this.connectionTypeList = connectionTypeList;
    }
}
