package com.music.player.lib.listener;

import android.media.MediaPlayer;

import com.music.player.lib.bean.MusicInfo;

/**
 * 播放进度监听器
 * TinyHung@Outlook.com
 * 2018/1/18.
 */

public interface OnPlayerEventListener {

    /**
     * 播放器所有状态回调
     *
     * @param musicInfo 当前播放的任务，未播放为空
     * @param stateCode 类别Code: 0：未播放 1：准备中 2：正在播放 3：暂停播放, 4：停止播放, 5：播放失败,详见：PlayerStatus类
     */
    void onMusicPlayerState(MusicInfo musicInfo, int stateCode);

    /**
     * 播放器准备好了
     */

    void onPrepared(MediaPlayer mediaPlayer);

    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);

    /**
     * 播放器反馈信息
     *
     * @param i
     * @param i1
     */
    void onInfo(int i, int i1);

    /**
     * 设置进度完成回调
     */
    void onSeekComplete();

    /**
     * 检查当前正在播放的任务，建议在界面的onResume()中调用
     *
     * @param musicInfo
     */

    void checkedPlayTaskResult(MusicInfo musicInfo, MediaPlayer mediaPlayer);


    /**
     * 改变了播放器播放模式回调
     *
     * @param playModel
     */
    void changePlayModelResult(int playModel);

    /**
     * 设定闹钟回调
     *
     * @param playAlarmModel
     */
    void changeAlarmModelResult(int playAlarmModel);

    /**
     * 计时器剩余的时间，回调给播放控制器
     *
     * @param durtion
     */
    void taskRemmainTime(long durtion);
}
