package com.kk.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by zhangkai on 2017/5/9.
 */

public class UMShareImpl extends IShare {
    private UMShareListener umShareListener;
    private Activity mActivity;

    private static UMShareImpl instance;

    public static UMShareImpl get() {
        synchronized (UMShareImpl.class) {
            if (instance == null)
                instance = new UMShareImpl();
        }
        return instance;
    }


    public UMShareImpl setCallback(Activity activity, UMShareListener umShareListener) {
        this.mActivity = activity;
        this.umShareListener = umShareListener;
        return this;
    }


    public static class Builder {
        private String wxappid;
        private String wxappsecert;
        private String qqappid;
        private String qqappsecert;
        private String wbappid;
        private String wbappsecert;
        private boolean debug;


        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setWeixin(String wxappid, String wxappsecert) {
            this.wxappid = wxappid;
            this.wxappsecert = wxappsecert;
            return this;
        }

        public Builder setQQ(String qqappid, String qqappsecert) {
            this.qqappid = qqappid;
            this.qqappsecert = qqappsecert;
            return this;
        }

        public Builder setWeibo(String wbappid, String wbappsecert) {
            this.wbappid = wbappid;
            this.wbappsecert = wbappsecert;
            return this;
        }

        public void build(Context context) {
            PlatformConfig.setWeixin(wxappid, wxappsecert);
            PlatformConfig.setQQZone(qqappid, qqappsecert);
            PlatformConfig.setSinaWeibo(wbappid, wbappsecert, "http://sns.whalecloud.com");
            Config.DEBUG = debug;
            UMShareAPI.get(context);
        }

    }

    @Override
    public void shareText(String content, SHARE_MEDIA shareMedia) {
        new ShareAction(mActivity).setPlatform(shareMedia)
                .withText(content)
                .setCallback(umShareListener)
                .share();
    }

    @Override
    public void shareImage(String content, String url, SHARE_MEDIA shareMedia) {
        UMImage image = createImage(url);
        new ShareAction(mActivity).setPlatform(shareMedia).withText(content)
                .withMedia(image).setCallback(umShareListener).share();
    }

    @Override
    public void shareImage(String content, Drawable drawable, SHARE_MEDIA shareMedia) {
        UMImage image = createImage(drawable);
        new ShareAction(mActivity).setPlatform(shareMedia).withText(content)
                .withMedia(image).setCallback(umShareListener).share();
    }

    @Override
    public void shareImage(String content, Bitmap bitmap, SHARE_MEDIA shareMedia) {
        UMImage image = createImage(bitmap);
        new ShareAction(mActivity).setPlatform(shareMedia).withText(content)
                .withMedia(image).setCallback(umShareListener).share();
    }

    @Override
    public void shareImage(String content, int drawableId, SHARE_MEDIA shareMedia) {
        UMImage image = createImage(drawableId);
        new ShareAction(mActivity).setPlatform(shareMedia).withText(content)
                .withMedia(image).setCallback(umShareListener).share();
    }

    @Override
    public void shareUrl(String title, String url, String desc, String iconurl, SHARE_MEDIA shareMedia) {
        UMImage image = createImage(iconurl);
        shareUrl(title, url, desc, image, shareMedia);
    }

    @Override
    public void shareUrl(String title, String url, String desc, int drawableId, SHARE_MEDIA shareMedia) {
        UMImage image = createImage(drawableId);
        shareUrl(title, url, desc, image, shareMedia);
    }

    @Override
    public void shareUrl(String title, String url, String desc, Drawable drawable, SHARE_MEDIA shareMedia) {
        UMImage image = createImage(drawable);
        shareUrl(title, url, desc, image, shareMedia);
    }

    @Override
    public void shareUrl(String title, String url, String desc, Bitmap bitmap, SHARE_MEDIA shareMedia) {
        UMImage image = createImage(bitmap);
        shareUrl(title, url, desc, image, shareMedia);
    }

    private void shareUrl(String title, String url, String desc, UMImage image, SHARE_MEDIA shareMedia) {
        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(desc);//描述
        new ShareAction(mActivity).setPlatform(shareMedia)
                .withMedia(web).setCallback(umShareListener)
                .share();
    }

    private UMImage createImage(String url) {
        UMImage image = new UMImage(mActivity, url);
        handleImage(image);
        return image;
    }

    private UMImage createImage(Bitmap bitmap) {
        UMImage image = new UMImage(mActivity, bitmap);
        handleImage(image);
        return image;
    }

    private UMImage createImage(Drawable drawable) {
        UMImage image = new UMImage(mActivity, ((BitmapDrawable) drawable).getBitmap());
        handleImage(image);
        return image;
    }

    private UMImage createImage(int drawableId) {
        UMImage image = new UMImage(mActivity, drawableId);
        handleImage(image);
        return image;
    }

    private void handleImage(UMImage image) {
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
        image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色
    }
}
