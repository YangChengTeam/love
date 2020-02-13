package com.music.player.lib.bean;

import java.util.List;

/**
 * Created by wanglin  on 2019/7/16 17:38.
 */
public class MusicInfoWrapper {
    private List<MusicInfo> list;
    private MusicInfo info;

    public List<MusicInfo> getList() {
        return list;
    }

    public void setList(List<MusicInfo> list) {

        this.list = list;
    }

    public MusicInfo getInfo() {
        return info;
    }

    public void setInfo(MusicInfo info) {
        this.info = info;
    }
}
