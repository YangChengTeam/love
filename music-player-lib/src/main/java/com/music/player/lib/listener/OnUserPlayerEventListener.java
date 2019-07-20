package com.music.player.lib.listener;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;

/**
 * UI界面组件需要注册的播放Event事件
 * TinyHung@Outlook.com
 * 2018/1/18.
 */

public interface OnUserPlayerEventListener {
    /**
     * 播放器所有的播放状态回调至此
     * @param musicInfo 当前播放的任务，未播放为空
     * @param stateCode 类别Code: 0：未播放 1：准备中 2：正在播放 3：暂停播放, 4：停止播放
     */
    void onMusicPlayerState(MusicInfo musicInfo, int stateCode);

    /**
     * 检查当前正在播放的任务回调，在播放器控件初始化或列表初始画完成调用
     * @param musicInfo
     */
    void checkedPlayTaskResult(MusicInfo musicInfo, KSYMediaPlayer mediaPlayer);
    /**
     * 改变了播放器播放模式回调
     * @param playModel
     */
    void changePlayModelResult(int playModel);
    /**
     * 改变了闹钟模式回调
     * @param model
     */
    void changeAlarmModelResult(int model);
    /**
     * 播放器配置
     * @param musicPlayerConfig
     */
    void onMusicPlayerConfig(MusicPlayerConfig musicPlayerConfig);
    /**
     * 缓冲百分比
     */
    void onBufferingUpdate(int percent);
    /**
     * 播放器准备好了
     */
    void onPrepared(IMediaPlayer mediaPlayer);
    /**
     * 自动创建播放任务
     * @param viewTupe
     * @param position
     */
    void autoStartNewPlayTasks(int viewTupe, int position);
    /**
     * 播放时间倒计时，注意：该方法在子线程回调
     * @param durtion
     */
    void taskRemmainTime(long durtion);
    /**
     * 一处点赞，所有同步
     * @param icon
     * @param isCollect
     * @param musicID 双向校验目标对象
     */
    void changeCollectResult(int icon, boolean isCollect,String musicID);
}
