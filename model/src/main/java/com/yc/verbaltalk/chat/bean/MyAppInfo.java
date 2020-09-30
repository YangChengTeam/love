package com.yc.verbaltalk.chat.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by sunshey on 2019/5/21.
 */

public class MyAppInfo {
    private String appName;
    private Drawable image;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "MyAppInfo{" +
                "appName='" + appName + '\'' +
                ", image=" + image +
                '}';
    }
}
