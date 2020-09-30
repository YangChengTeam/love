package com.yc.verbaltalk.base.utils;

import android.app.Activity;
import android.util.Log;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.verbaltalk.base.listener.ThirdLoginListener;
import com.yc.verbaltalk.base.model.UserAccreditInfo;
import com.yc.verbaltalk.base.view.LoadDialog;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.Map;

import androidx.annotation.Nullable;
import yc.com.rthttplibrary.util.LogUtil;
import yc.com.rthttplibrary.util.ToastUtil;

/**
 * Created by suns  on 2020/6/22 10:18.
 */
public class UserLoginManager {

    private Activity mActivity;
    private ThirdLoginListener mLoginListener;
    private LoadDialog loadingView;

    private static UserLoginManager instance;


    private UserLoginManager() {
    }


    public static UserLoginManager getInstance() {
        synchronized (UserLoginManager.class) {
            if (instance == null) {
                synchronized (UserLoginManager.class) {
                    instance = new UserLoginManager();
                }
            }
        }
        return instance;
    }

    private final UMAuthListener umAuthListener = new UMAuthListener() {
        public void onStart(@NotNull SHARE_MEDIA share_media) {
            showProgressDialog("正在登录中，请稍候...");
        }

        public void onComplete(@NotNull SHARE_MEDIA share_media, int action, @NotNull Map<String, String> data) {


            try {
                switch (action) {
                    case UMAuthListener.ACTION_AUTHORIZE:
                        oauthAndLogin(share_media);
                    default:
                        break;
                    case UMAuthListener.ACTION_GET_PROFILE:
                        if (data.size() > 0) {
                            Log.e("TAG", "onComplete: " + data);
                            UserAccreditInfo userDataInfo = new UserAccreditInfo();
                            userDataInfo.setNickname(data.get("name"));
                            userDataInfo.setCity(data.get("city"));
                            userDataInfo.setIconUrl(data.get("iconurl"));
                            userDataInfo.setGender(data.get("gender"));
                            userDataInfo.setProvince(data.get("province"));
                            userDataInfo.setOpenid(data.get("openid"));
                            userDataInfo.setAccessToken(data.get("accessToken"));
                            userDataInfo.setUid(data.get("uid"));

                            if (mLoginListener != null) {
                                mLoginListener.onLoginResult(userDataInfo);
                            }
                        } else {
                            ToastUtil.toast(mActivity, "登录失败，请重试!");
                        }
                        closeProgressDialog();
                }
            } catch (Exception var5) {
                LogUtil.msg("complete:-->" + var5.getMessage());
                ToastUtil.toast(mActivity, "登录失败，请重试!");
                deleteOauth(share_media);
                closeProgressDialog();
            }

        }

        public void onError(@NotNull SHARE_MEDIA share_media, int action, @NotNull Throwable throwable) {
            closeProgressDialog();
            LogUtil.msg("login error->>" + throwable.getMessage());
            ToastUtil.toast(mActivity, "登录失败,请重试！");
            deleteOauth(share_media);
        }

        public void onCancel(@NotNull SHARE_MEDIA share_media, int action) {

            ToastUtil.toast(mActivity, "登录取消");
            closeProgressDialog();
        }
    };

    @NotNull
    public UserLoginManager setCallBack(@Nullable Activity activity, @Nullable ThirdLoginListener loginListener) {
        this.mActivity = activity;
        this.mLoginListener = loginListener;
        this.loadingView = new LoadDialog(activity);
        return this;
    }

    public void oauthAndLogin(@Nullable SHARE_MEDIA shareMedia) {
        boolean isAuth = UMShareAPI.get(this.mActivity).isAuthorize(this.mActivity, shareMedia);
        if (isAuth) {
            UMShareAPI.get(this.mActivity).getPlatformInfo(this.mActivity, shareMedia, this.umAuthListener);
        } else {
            UMShareAPI.get(this.mActivity).doOauthVerify(this.mActivity, shareMedia, this.umAuthListener);
        }

    }

    public void deleteOauth(@Nullable SHARE_MEDIA shareMedia) {
        if (this.mActivity != null) {
            UMShareAPI.get(this.mActivity).deleteOauth(this.mActivity, shareMedia, (UMAuthListener) null);
        }
    }


    /**
     * 显示进度框
     *
     * @param message
     */
    private void showProgressDialog(String message) {
        try {
            if (mActivity != null && !mActivity.isFinishing()) {

                if (null == loadingView) {
                    loadingView = new LoadDialog(mActivity);
                }
                loadingView.setMessage(message);
                loadingView.show();
            }
        } catch (Exception e) {
            LogUtil.msg("close dialog error->>" + e.getMessage());
        }


    }

    /**
     * 关闭进度框
     */
    private void closeProgressDialog() {
        try {
            if (mActivity != null && !mActivity.isFinishing()) {
                if (loadingView != null && loadingView.isShowing()) {
                    loadingView.dismiss();
                    loadingView = null;
                }
            }

        } catch (Exception e) {
            LogUtil.msg("close dialog error->>" + e.getMessage());
        }
    }

}
