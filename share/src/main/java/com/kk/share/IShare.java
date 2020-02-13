package com.kk.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by zhangkai on 2017/5/9.
 */

public abstract class IShare {

    public abstract void shareText(String content, SHARE_MEDIA shareMedia);

    public abstract void shareImage(String content, String url, SHARE_MEDIA shareMedia);

    public abstract void shareImage(String content, Drawable drawable, SHARE_MEDIA shareMedia);

    public abstract void shareImage(String content, Bitmap bitmap, SHARE_MEDIA shareMedia);

    public abstract void shareImage(String content, int drawableId, SHARE_MEDIA shareMedia);

    public abstract void shareUrl(String title, String url, String desc, String iconurl, SHARE_MEDIA shareMedia);

    public abstract void shareUrl(String title, String url, String desc, int drawableId, SHARE_MEDIA shareMedia);

    public abstract void shareUrl(String title, String url, String desc, Drawable drawable, SHARE_MEDIA shareMedia);

    public abstract void shareUrl(String title, String url, String desc, Bitmap bitmap, SHARE_MEDIA shareMedia);

    public abstract void shareMiniParam(String title,String desc, int bitmap, String appid);

}
