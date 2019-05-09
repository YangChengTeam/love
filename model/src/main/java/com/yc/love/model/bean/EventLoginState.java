package com.yc.love.model.bean;

/**
 * Created by mayn on 2019/5/9.
 */

public class EventLoginState {

    public static final int STATE_LOGINED = 1;
    public  static final int STATE_EXIT = 2;
    public  static final int STATE_UPDATE_INFO = 3;

    public int state;

    public EventLoginState(int state) {
        this.state = state;
    }

    //    public boolean isLogined;


}
