package com.yc.verbaltalk.model.bean;

import java.io.Serializable;

/**
 * Created by wanglin  on 2019/7/17 15:57.
 */
public class IndexHotInfo implements Serializable {

    private int id;
    private String search;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
