package com.yc.verbaltalk.chat.bean.event;

import java.util.List;

/**
 * Created by sunshey on 2019/5/18.
 */

public class NetWorkChangBean {
    public List<String> connectionTypeList;

    public NetWorkChangBean(List<String> connectionTypeList) {
        this.connectionTypeList = connectionTypeList;
    }
}
