package com.yc.verbaltalk.model.bean.event;

import java.util.List;

/**
 * Created by mayn on 2019/5/18.
 */

public class NetWorkChangT1Bean {
    public List<String> connectionTypeList;

    public NetWorkChangT1Bean(List<String> connectionTypeList) {
        this.connectionTypeList = connectionTypeList;
    }
}
