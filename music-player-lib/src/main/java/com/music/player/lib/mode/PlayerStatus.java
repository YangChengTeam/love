package com.music.player.lib.mode;

/**
 * TinyHung@Outlook.com
 * 2018/1/24.
 * 播放器状态
 */
public interface PlayerStatus {
    int PLAYER_STATUS_EMPOTY=0;
    int PLAYER_STATUS_ASYNCPREPARE=1;
    int PLAYER_STATUS_PLAYING=2;
    int PLAYER_STATUS_PAUSE=3;
    int PLAYER_STATUS_STOP=4;
    int PLAYER_STATUS_ERROR=5;
}
