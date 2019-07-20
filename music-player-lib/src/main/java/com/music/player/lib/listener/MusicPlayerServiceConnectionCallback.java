package com.music.player.lib.listener;

import com.music.player.lib.service.MusicPlayerService;

/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 */

public interface MusicPlayerServiceConnectionCallback {
    void onServiceConnected(MusicPlayerService musicPlayService);


    void onServiceDisconnected();
}
