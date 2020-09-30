package com.yc.verbaltalk.chat.bean.event;


import com.yc.verbaltalk.chat.bean.UserInfo;

public class EventLoginState {

    public static final int STATE_LOGINED = 1;
    public  static final int STATE_EXIT = 2;
    public  static final int STATE_UPDATE_INFO = 3;

    public String nick_name;

    public int state;
    public UserInfo userInfo;

    public EventLoginState(int state) {
        this.state = state;
    }

    public EventLoginState( int state,String nick_name) {
        this.nick_name = nick_name;
        this.state = state;
    }

    //    public boolean isLogined;


}
