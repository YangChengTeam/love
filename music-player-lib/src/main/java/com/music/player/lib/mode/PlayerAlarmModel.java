package com.music.player.lib.mode;

/**
 * TinyHung@Outlook.com
 * 2018/1/20.
 * 定时器闹钟模式选项
 */

public interface PlayerAlarmModel {
    //10分钟
    int PLAYER_ALARM_10=1;
    //20分钟
    int PLAYER_ALARM_20=2;
    //30分钟
    int PLAYER_ALARM_30=3;
    //1小时
    int PLAYER_ALARM_ONE_HOUR=4;
    //无限制时长
    int PLAYER_ALARM_NORMAL=5;

    /**
     * 播放器定时最大时长，单位秒
     */
    class TIME {
        //30分钟
        public static final int DEFAULT_TIME = 1800;
        //一个小时
        public static final int MAX_ONE_HOUR = 3600;
        //2个小时
        public static final int MAX_TWO_HOUR = 7200;
    }
}
