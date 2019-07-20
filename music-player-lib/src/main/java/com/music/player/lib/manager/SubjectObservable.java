package com.music.player.lib.manager;

import java.util.Observable;

/**
 * TinyHung@Outlook.com
 * 2018/1/18.
 * 被观察者，方便刷新列表
 */

public class SubjectObservable extends Observable {

    public SubjectObservable(){

    }
    public void updataSubjectObserivce(Object data){
        setChanged();
        notifyObservers(data);
    }
}
