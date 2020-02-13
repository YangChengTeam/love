package com.music.player.lib.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.music.player.lib.bean.MusicInfo;
import com.music.player.lib.bean.MusicPlayerConfig;
import com.music.player.lib.constants.Constants;
import com.music.player.lib.listener.MusicPlayerServiceConnectionCallback;
import com.music.player.lib.listener.OnPlayerEventListener;
import com.music.player.lib.listener.OnUserPlayerEventListener;
import com.music.player.lib.mode.PlayerAlarmModel;
import com.music.player.lib.mode.PlayerModel;
//import com.music.player.lib.service.MusicPlayerService;
import com.music.player.lib.service.MusicPlayerService;
import com.music.player.lib.util.Logger;
import com.music.player.lib.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 * 统一注册和调度中心
 */

public class MusicPlayerManager implements OnPlayerEventListener {

    private static final String TAG = MusicPlayerManager.class.getSimpleName();
    private static MusicPlayerManager mInstance = null;
    private static Context mContext = null;
    private MusicPlayerServiceConnectionCallback mConnectionCallback = null;
    private List<OnUserPlayerEventListener> mUserCallBackListenerList = null;//方便多界面注册进来
    private static MusicPlayerServiceConnection mMusicPlayerServiceConnection = null;
    private static SubjectObservable mSubjectObservable = null;
    private static MusicPlayerService.MusicPlayerServiceBunder mMusicPlayerServiceBunder = null;

    public static synchronized MusicPlayerManager getInstance() {
        synchronized (MusicPlayerManager.class) {
            if (null == mInstance) {
                mInstance = new MusicPlayerManager();
            }
        }
        return mInstance;
    }

    //标注一个方法过期,方法上面：@Deprecated
    private MusicPlayerManager() {
        mMusicPlayerServiceConnection = new MusicPlayerServiceConnection();
        mSubjectObservable = new SubjectObservable();
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context.getApplicationContext();
        PreferencesUtil.init(context, context.getPackageName() + "music_play_config", Context.MODE_MULTI_PROCESS);
        if (1 != PreferencesUtil.getInstance().getInt(Constants.SP_FIRST_START, 0)) {
            PreferencesUtil.getInstance().putInt(Constants.SP_MUSIC_PLAY_ALARM, PlayerAlarmModel.PLAYER_ALARM_30);
            PreferencesUtil.getInstance().putInt(Constants.SP_FIRST_START, 1);
        }
    }

    /**
     * 必须在init()之后调用
     *
     * @return
     */
    public Context getContext() {
        if (null == mContext) {
            throw new IllegalStateException("MusicPlayerManager：调用getContext()之前请先调用init()");
        }
        return mContext;
    }

    /**
     * 添加观察者
     *
     * @param o 列表播放最好添加
     */
    public void addObservable(Observer o) {
        if (null == mContext) {
            throw new IllegalStateException("MusicPlayerManager：必须先调用init()方法");
        }
        if (null != mSubjectObservable) {
            mSubjectObservable.addObserver(o);
        }
    }

    /**
     * 移除指定观察者
     *
     * @param o
     */
    public void deleteObserver(Observer o) {
        if (null != mSubjectObservable) {
            mSubjectObservable.deleteObserver(o);
        }
    }

    /**
     * 移除所有观察者
     */
    public void deleteObservers() {
        if (null != mSubjectObservable) {
            mSubjectObservable.deleteObservers();
        }
    }

    /**
     * UI组件需要实现的播放器变化监听
     *
     * @param listener
     */
    public void addPlayerStateListener(OnUserPlayerEventListener listener) {
        if (null == mUserCallBackListenerList) {
            mUserCallBackListenerList = new ArrayList<>();
        }
        mUserCallBackListenerList.add(listener);
    }

    /**
     * 注销播放器变化监听
     *
     * @param listener
     */
    public void detelePlayerStateListener(OnUserPlayerEventListener listener) {
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            if (mUserCallBackListenerList.contains(listener)) {
                mUserCallBackListenerList.remove(listener);
            }
        }
    }

    /**
     * 移除所有的播放器变化监听
     */
    public void deteleAllPlayerStateListener() {
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            mUserCallBackListenerList.clear();
            mUserCallBackListenerList = null;
        }
    }


    /**
     * 检查播放器配置
     */
    public void checkedPlayerConfig() {
        MusicPlayerConfig musicPlayerConfig = new MusicPlayerConfig();
        musicPlayerConfig.setPlayModel(PreferencesUtil.getInstance().getInt(Constants.SP_MUSIC_PLAY_MODEL, PlayerModel.PLAY_MODEL_SEQUENCE_FOR));
        musicPlayerConfig.setAlarmModel(PreferencesUtil.getInstance().getInt(Constants.SP_MUSIC_PLAY_ALARM, PlayerAlarmModel.PLAYER_ALARM_30));
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onUserPlayerEventListener : mUserCallBackListenerList) {
                onUserPlayerEventListener.onMusicPlayerConfig(musicPlayerConfig);
            }
        }
    }

    /**
     * 检查当前正在播放的任务，在播放器控件初始化或列表初始画完成后调用
     */
    public void onResumeChecked() {
        if (null == mContext) {
            throw new IllegalStateException("MusicPlayerManager：必须先调用init()方法");
        }
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.checkedPlayTask();
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }

    /**
     * 播放新的音乐
     *
     * @param musicInfo * 此方法已被playPauseMusic()替代，新增暂停、播放特性
     */
    @Deprecated
    public void playMusic(MusicInfo musicInfo) {
        if (null == mContext) {
            throw new IllegalStateException("MusicPlayerManager：必须先调用init()方法");
        }
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.playMusic(musicInfo);
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }

    /**
     * 播放指定位置音乐
     *
     * @param pistion * 此方法已被playPauseMusic()替代，新增暂停、播放特性
     */
    @Deprecated
    public void playMusic(int pistion) {
        if (null == mContext) {
            throw new IllegalStateException("MusicPlayerManager：必须先调用init()方法");
        }
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.playMusic(pistion);
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }

    /**
     * 播放一个全新的列表,并指定位置
     *
     * @param pistion    指定播放的位置
     * @param musicInfos 任务列表
     *                   此方法已被playPauseMusic()替代，新增暂停、播放特性
     *                   此方法只会播放新的任务
     */
    @Deprecated
    public void playMusic(List<MusicInfo> musicInfos, int pistion) {
        if (null == mContext) {
            throw new IllegalStateException("MusicPlayerManager：必须先调用init()方法");
        }
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.playMusic(musicInfos, pistion);
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }


    public MusicInfo getCurrentMusicInfo() {
        if (serviceIsNoEmpty()) {
            return mMusicPlayerServiceBunder.getCurrentMusic();
        }

        return null;
    }


    /**
     * 播放或者暂停
     *
     * @param data
     * @param position
     */
    public void playPauseMusic(List<MusicInfo> data, int position) {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.playPauseMusic(data, position);
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }

    /**
     * 开始\暂停播放
     *
     * @return
     */
    public boolean playPause() {
        if (null == mContext) {
            throw new IllegalStateException("MusicPlayerManager：必须先调用init()方法");
        }
        if (serviceIsNoEmpty()) {
            return mMusicPlayerServiceBunder.onPlayPause();
        }
        return false;
    }

    /**
     * 结束播放
     */
    public void stop() {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.stop();

        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }


    public void clearNotifycation() {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.cancelNotification();
        }
    }

    public void showNotification(MusicInfo musicInfo, boolean isPlay) {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.showNotification(musicInfo, isPlay);
        }
    }

    /**
     * 返回播放器正在播放的位置
     *
     * @return
     */
    public long getPlayDurtion() {
        if (serviceIsNoEmpty()) {
            return mMusicPlayerServiceBunder.getPlayerCurrentPosition();
        }
        return 0;
    }

    /**
     * 播放上一首
     */
    public void playLast() {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.playLast();
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }

    /**
     * 播放下一首
     */
    public void playNext() {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.playNext();
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }


    /**
     * 获取正在播放的角标位置
     *
     * @return
     */
    public int getPlayingPosition() {
        if (serviceIsNoEmpty()) {
            return mMusicPlayerServiceBunder.getPlayingPosition();
        }
        return -1;
    }

    /**
     * 设置是否重复播放
     *
     * @param flag
     */
    public void setLoop(boolean flag) {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.setLoop(flag);
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }


    /**
     * 改变播放模式
     *
     * @return
     */
    public void changePlayModel() {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.changePlayModel();
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }

    /**
     * 改变闹钟定时模式
     */
    public void changeAlarmModel() {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.changeAlarmModel();
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }

    /**
     * 获取当前设置的播放模式
     *
     * @return
     */
    public int getPlayModel() {
        if (serviceIsNoEmpty()) {
            return mMusicPlayerServiceBunder.getPlayModel();
        }
        return PlayerModel.PLAY_MODEL_SEQUENCE_FOR;//默认列表循环播放
    }


    /**
     * 获取当前设置的闹钟定时时长
     *
     * @return 固定档次，不是时间
     */
    public int getPlayerAlarmModel() {
        if (serviceIsNoEmpty()) {
            return mMusicPlayerServiceBunder.getPlayAlarmModel();
        }
        return PlayerAlarmModel.PLAYER_ALARM_NORMAL;//默认不限制时间播放
    }

    /**
     * 获取当前设置的闹钟定时时长
     *
     * @return 具体的时间，单位秒
     */
    public long getPlayerAlarmDurtion() {
        if (serviceIsNoEmpty()) {
            return mMusicPlayerServiceBunder.getPlayerAlarmDurtion();
        }
        return 0;
    }

    /**
     * 设置定时关闭播放器 具体时间，不在PlayerAlarmModel提供的档次内
     *
     * @param durtion 具体的时间，单位秒
     */
    public void setPlayerDurtion(long durtion) {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.setPlayerDurtion(durtion);
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }

    /**
     * 直接设置定时关闭播放器的闹钟档次
     * 参考：PlayerAlarmModel定义的档次
     */
    public void setAralmFiexdTimer(int fiexdTimerAlarmModel) {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.setAralmFiexdTimer(fiexdTimerAlarmModel);
        } else {
            throw new IllegalStateException("MusicPlayerManager：你确定已经在此之前调用了bindService()方法？");
        }
    }

    /**
     * 自动开始播放任务
     * 调用这个方法后会回调给所有已实现OnUserPlayerEventListener 接口的UI组建，用来创建播放任务
     *
     * @param viewTupe 需要执行回调的View类型，默认定义了两个，主页和详情界面
     * @param position 播放器要求从哪个位置开始播放
     */
    public void autoStartNewPlayTasks(int viewTupe, int position) {
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onUserPlayerEventListener : mUserCallBackListenerList) {
                onUserPlayerEventListener.autoStartNewPlayTasks(viewTupe, position);
            }
        }
    }

    /**
     * 返回mMusicPlayerServiceBunder的实例是否为空
     *
     * @return
     */
    private boolean serviceIsNoEmpty() {
        return null != mMusicPlayerServiceBunder;
    }

    /**
     * 是否开启调试模式,默认不开启
     *
     * @param flag
     */
    public void setDebug(boolean flag) {
        Logger.IS_DEBUG = flag;
    }


    /**
     * 检查收藏结果回调
     *
     * @param icon
     * @param isCollect
     * @param musicID
     */
    public void changeCollectResult(int icon, boolean isCollect, String musicID) {
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onUserPlayerEventListener : mUserCallBackListenerList) {
                onUserPlayerEventListener.changeCollectResult(icon, isCollect, musicID);
            }
        }
    }


    /**
     * bindService()必需
     */
    private class MusicPlayerServiceConnection implements ServiceConnection {


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected: " + service);

            if (null != service) {
                mMusicPlayerServiceBunder = (MusicPlayerService.MusicPlayerServiceBunder) service;
                mMusicPlayerServiceBunder.setOnPlayerEventListener(MusicPlayerManager.this);//注册播放状态Event状态监听
                mMusicPlayerServiceBunder.setPlayMode(PreferencesUtil.getInstance().getInt(Constants.SP_MUSIC_PLAY_MODEL, PlayerModel.PLAY_MODEL_SEQUENCE_FOR));
                mMusicPlayerServiceBunder.setPlayAlarmMode(PreferencesUtil.getInstance().getInt(Constants.SP_MUSIC_PLAY_ALARM, PlayerAlarmModel.PLAYER_ALARM_NORMAL));
                if (null != mConnectionCallback) {
                    mConnectionCallback.onServiceConnected(mMusicPlayerServiceBunder.getService());
                }
            } else {
                Logger.d(TAG, "绑定服务失败");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.d(TAG, "服务已断开");
            //注销监听
            if (null != mMusicPlayerServiceBunder) {
                mMusicPlayerServiceBunder.removePlayerEventListener();
            }
            mMusicPlayerServiceBunder = null;
            if (null != mConnectionCallback) {
                mConnectionCallback.onServiceDisconnected();
            }
        }
    }

    /**
     * 绑定服务
     *
     * @param context
     * @param serviceConnectionCallBack
     */
    public void bindService(Context context, MusicPlayerServiceConnectionCallback serviceConnectionCallBack) {
        if (null == mContext) {
            throw new IllegalStateException("请先在Application中调用init()方法");
        }


        if (null != mMusicPlayerServiceConnection) {
            Logger.d(TAG, "binService");
//            Logger.e(TAG, "mMusicPlayerServiceBunder  " + mMusicPlayerServiceBunder);
            this.mConnectionCallback = serviceConnectionCallBack;
            Intent intent = new Intent(context, MusicPlayerService.class);
            context.startService(intent);
            context.bindService(intent, mMusicPlayerServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public void bindService(final Context context) {
        bindService(context, null);
    }

    /**
     * 解绑服务
     *
     * @param context
     */
    public void unBindService(Context context) {
        if (serviceIsNoEmpty() && null != context) {
            if (null != mMusicPlayerServiceConnection) {
                context.unbindService(mMusicPlayerServiceConnection);
                mMusicPlayerServiceBunder.destory();

            }
            context.stopService(new Intent(context, MusicPlayerService.class));

        }
        mConnectionCallback = null;
    }

    //=================================播放状态回调，回调至调用者=====================================

    /**
     * 播放器所有状态回调
     *
     * @param musicInfo 当前播放的任务，未播放为空
     * @param stateCode 类别Code: 0：未播放 1：准备中 2：正在播放 3：暂停播放, 4：停止播放, 5：播放失败,详见：PlayerStatus类
     */
    @Override
    public void onMusicPlayerState(MusicInfo musicInfo, int stateCode) {
        if (null != mSubjectObservable) {
            mSubjectObservable.updataSubjectObserivce(musicInfo);
        }
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onMusicPlayerState(musicInfo, stateCode);
            }
        }
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {

                onPlayerEventListener.onPrepared(mediaPlayer);
            }
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.onBufferingUpdate(percent);
            }
        }
    }

    @Override
    public void onInfo(int i, int i1) {

    }

    @Override
    public void onSeekComplete() {

    }



    @Override
    public void checkedPlayTaskResult(MusicInfo musicInfo, MediaPlayer mediaPlayer) {
        if (null != mSubjectObservable) {
            mSubjectObservable.updataSubjectObserivce(musicInfo);
        }
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.checkedPlayTaskResult(musicInfo, mediaPlayer);
            }
        }
    }

    @Override
    public void changePlayModelResult(int playModel) {
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.changePlayModelResult(playModel);
            }
        }
    }

    @Override
    public void changeAlarmModelResult(int playAlarmModel) {
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.changeAlarmModelResult(playAlarmModel);
            }
        }
    }

    @Override
    public void taskRemmainTime(long durtion) {
        if (null != mUserCallBackListenerList && mUserCallBackListenerList.size() > 0) {
            for (OnUserPlayerEventListener onPlayerEventListener : mUserCallBackListenerList) {
                onPlayerEventListener.taskRemmainTime(durtion);
            }
        }
    }

    public void seekTo(int progrss) {
        if (serviceIsNoEmpty()) {
            mMusicPlayerServiceBunder.seekTo(progrss);
        }
    }
}
