package com.yc.verbaltalk.chat.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by sunshey on 2019/5/21.
 */

public class OpenApkPkgInfo {
    public String pkg="";
    public String name;
    public Drawable icon;
    public int id;

    public OpenApkPkgInfo(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public OpenApkPkgInfo(int id,String pkg, String name, Drawable icon) {
        this.pkg = pkg;
        this.name = name;
        this.icon = icon;
        this.id = id;
    }
}
