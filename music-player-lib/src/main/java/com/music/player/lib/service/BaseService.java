package com.music.player.lib.service;

import android.app.Service;

/**
 * Created by wanglin  on 2018/2/9 16:04.
 */

public abstract class BaseService extends Service {


    public abstract boolean playPause();

    public abstract boolean isPreparing();

    public abstract boolean isPlaying();

    public abstract void stop();

    public abstract void pause();
}
