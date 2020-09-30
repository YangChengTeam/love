package com.yc.verbaltalk.base.fragment;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.player.lib.util.ToastUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yc.verbaltalk.R;
import com.yc.verbaltalk.base.engine.LoveEngine;
import com.yc.verbaltalk.base.utils.UserInfoHelper;
import com.yc.verbaltalk.base.utils.UserLoginManager;
import com.yc.verbaltalk.chat.bean.UserInfo;
import com.yc.verbaltalk.chat.bean.event.EventLoginState;
import com.yc.verbaltalk.index.ui.fragment.UserPolicyFragment;
import com.yc.verbaltalk.mine.ui.activity.LoginRegisterActivity;
import com.yc.verbaltalk.mine.ui.activity.PrivacyStatementActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.observers.DisposableObserver;
import yc.com.rthttplibrary.bean.ResultInfo;
import yc.com.rthttplibrary.config.HttpConfig;

/**
 * Created by suns  on 2020/6/22 09:58.
 */
public class WxLoginFragment extends BaseBottomSheetDialogFragment {

    private ImageView ivWxClose;
    private ImageView ivWxLogin;
    private TextView tvUserPolicy;
    private TextView tvPrivacy;
    private TextView tvOtherLogin;

    private UserLoginManager manager;
    private LoveEngine loveEngine;

    protected int getLayoutId() {
        return R.layout.fragment_wx_login;
    }

    public void initViews() {

        ivWxClose = rootView.findViewById(R.id.iv_wx_close);

        ivWxLogin = rootView.findViewById(R.id.iv_wx_login);

        tvUserPolicy = rootView.findViewById(R.id.tv_user_policy);

        tvPrivacy = rootView.findViewById(R.id.tv_privacy);

        tvOtherLogin = rootView.findViewById(R.id.tv_other_login);
        manager = UserLoginManager.getInstance();
        loveEngine = new LoveEngine(mContext);
        initListener();
    }

    private void initListener() {

        ivWxLogin.setOnClickListener(v -> manager.setCallBack(mContext, userDataInfo -> thirdLogin(userDataInfo.getOpenid(), userDataInfo.getIconUrl(), userDataInfo.getGender(), userDataInfo.getNickname())).oauthAndLogin(SHARE_MEDIA.WEIXIN));


        ivWxClose.setOnClickListener(it -> dismiss());


        tvUserPolicy.setOnClickListener(it -> {
            UserPolicyFragment userPolicyFragment = new UserPolicyFragment();
            userPolicyFragment.show(getChildFragmentManager(), "");
        });


        tvPrivacy.setOnClickListener(v -> {
            startActivity(new Intent(mContext, PrivacyStatementActivity.class));
        });


        tvOtherLogin.setOnClickListener(v -> {
            startActivity(new Intent(mContext, LoginRegisterActivity.class));
            dismiss();
        });


    }

    private void thirdLogin(String access_token, String face, String sex, String nick_name) {

        loveEngine.thirdLogin(access_token, 2, face, sex, nick_name).subscribe(new DisposableObserver<ResultInfo<UserInfo>>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<UserInfo> userInfoResultInfo) {
                if (userInfoResultInfo != null) {
                    if (userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                        UserInfoHelper.saveUserInfo(userInfoResultInfo.data);
                        thirdLoginSuccess(userInfoResultInfo.data);
                    } else {
                        ToastUtils.showCenterToast(userInfoResultInfo.message);
                    }

                } else {
                    ToastUtils.showCenterToast(HttpConfig.NET_ERROR);
                }
            }
        });


    }

    private void thirdLoginSuccess(UserInfo data) {
        EventBus.getDefault().post(new EventLoginState(EventLoginState.STATE_LOGINED, data.nick_name));
        dismiss();
    }
}
