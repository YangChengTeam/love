package com.yc.verbaltalk.base.listener;

import com.yc.verbaltalk.base.model.UserAccreditInfo;

/**
 * Created by suns  on 2020/6/22 11:32.
 */
public interface ThirdLoginListener {
    void onLoginResult(UserAccreditInfo userDataInfo);
}
