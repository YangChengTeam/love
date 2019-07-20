package com.music.player.lib.bean;

/**
 * TinyHung@Outlook.com
 * 2018/1/19
 * 用户设定的播放器配置
 */

public class MusicPlayerConfig {

    //播放模式
    private int playModel;
    //闹钟设置
    private int alarmModel;

    public int getAlarmModel() {
        return alarmModel;
    }

    public void setAlarmModel(int alarmModel) {
        this.alarmModel = alarmModel;
    }

    public int getPlayModel() {
        return playModel;
    }

    public void setPlayModel(int playModel) {
        this.playModel = playModel;
    }
}
